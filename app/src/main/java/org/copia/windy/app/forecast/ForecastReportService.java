package org.copia.windy.app.forecast;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ForecastReportService {

    @Inject
    ForecastClient forecastClient;

    /**
     * @return list of reports. Not null
     */
    public List<ForecastWindReport> getForecast( List<GeoPoint> points ) throws IOException {
        return forecastClient.getForecast( points );
    }

    /**
     *
     * @param points
     * @return
     * @throws IOException
     */
    public List<ForecastWindReport> getNotifications( List<GeoPoint> points ) throws IOException {
        List<ForecastWindReport> notifications = new ArrayList<ForecastWindReport>();
        for ( ForecastWindReport report : forecastClient.getForecast( points ) ) {
            if (report.getWindSpeed() > 5) {
                notifications.add( report );
            }
        }
        return notifications;
    }

    /**
     * @return list of reports. Not null
     */
    public List<ForecastWindReport> getCurrentState( List<GeoPoint> points ) throws IOException {
        return forecastClient.getCurrent( points );
    }
}
