package org.copia.windy.app.forecast;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.FIODaily;
import com.github.dvdme.ForecastIOLib.FIOHourly;
import com.github.dvdme.ForecastIOLib.ForecastIO;
import dagger.Module;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.copia.windy.app.utils.InmemoryCache;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;

import android.net.Uri.Builder;

import java.util.*;
import java.util.logging.Logger;

@Module
public class ForecastApiClient implements ForecastClient {

    public static final String FORECAST_CACHE_NS = "forecast";

    private Logger log = Logger.getAnonymousLogger();

    private static final String API_KEY = "some-api";

    @Inject
    InmemoryCache cache;

    @Override
    public List<ForecastWindReport> getForecast( List<GeoPoint> points ) throws IOException {
        List<ForecastWindReport> reports = new ArrayList<ForecastWindReport>();
        for ( GeoPoint point : points ) {

            Map<Date, Double> cachedForecast = (Map<Date, Double>) cache.get( FORECAST_CACHE_NS, point );
            if ( cachedForecast == null ) {
                cachedForecast = getWindSpeedPrediction( point );
                cache.put( FORECAST_CACHE_NS, point, cachedForecast );
            }

            for ( Date date : cachedForecast.keySet() ) {
                reports.add(
                        new ForecastWindReport( date, point, cachedForecast.get( date ) )
                );
            }
        }
        return reports;
    }

    @Override
    public List<ForecastWindReport> getCurrent( List<GeoPoint> points ) throws IOException {
        List<ForecastWindReport> reports = new ArrayList<ForecastWindReport>();
        for ( GeoPoint point : points ) {
            ForecastWindReport cachedReport = (ForecastWindReport) cache.get( point );
            if ( cachedReport == null ) {
                cachedReport = new ForecastWindReport( point, getCurrentWindSpeed( point ) );
                cache.put( point, cachedReport );
            }
            log.info( cachedReport.toString() );
            reports.add( cachedReport );
        }
        return reports;
    }

    private Double getCurrentWindSpeed( GeoPoint point ) throws IOException {
        ForecastIO fio = new ForecastIO( API_KEY );
        fio.setUnits( ForecastIO.UNITS_SI );// get speed in m/s
        fio.setExcludeURL( "[minutely,hourly,daily,alerts,flags]" );
        try {
            log.info( "url is " + fio.getUrl( point.getLatitude().toString(), point.getLongitude().toString() ) );
            String response = requestForecast(
                    fio.getUrl(
                            point.getLatitude().toString(), point.getLongitude().toString()
                    )
            );

            fio.getForecast( response );
            FIOCurrently currently = new FIOCurrently( fio );

            return currently.get().windSpeed();
        } catch ( Exception e ) {
            log.fine( "Error executing request " + e.toString() );
        }
        throw new IOException( "Forecast is unavalable" );
    }

    private Map<Date, Double> getWindSpeedPrediction( GeoPoint point ) throws IOException {
        ForecastIO fio = new ForecastIO( API_KEY );
        fio.setUnits( ForecastIO.UNITS_SI );// get speed in m/s
        fio.setExcludeURL( "[minutely,alerts,flags]" );

        Map<Date, Double> result = new TreeMap<Date, Double>();
        try {
            String response = requestForecast(
                    fio.getUrl( point.getLatitude().toString(), point.getLongitude().toString() )
            );
            fio.getForecast( response );

            FIOHourly hourly = new FIOHourly( fio );
            SimpleDateFormat dfm = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );

            for ( int i = 0; i < hourly.hours(); i++ ) {
                result.put(
                        dfm.parse( hourly.getHour( i ).time() ), hourly.getHour( i ).windSpeed()
                );
            }

            FIODaily daily = new FIODaily( fio );
            for ( int i = 0; i < daily.days(); i++ ) {
                if ( !result.containsKey( dfm.parse( daily.getDay( i ).time() ) ) ) {
                    result.put(
                            dfm.parse( daily.getDay( i ).time() ), daily.getDay( i ).windSpeed()
                    );
                }
            }

            return result;
        } catch ( Exception e ) {
            log.fine( "Error executing request " + e.toString() );
        }
        throw new IOException( "Forecast is unavalable" );
    }

    private String requestForecast( String url ) {
        String responseAsString = "";
        BufferedReader in;
        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet request = new HttpGet();
            URI website = new URI( url );
            request.setURI( website );
            HttpResponse response = httpclient.execute( request );
            in = new BufferedReader(
                    new InputStreamReader(
                            response.getEntity().getContent()
                    )
            );

            String line = in.readLine();
            while ( line != null ) {
                responseAsString += line;
                line = in.readLine();
            }
        } catch ( Exception e ) {
            Logger.getAnonymousLogger().info( "Fetching forecast data: Error in http connection " + e.toString() );
        }
        return responseAsString;
    }

}
