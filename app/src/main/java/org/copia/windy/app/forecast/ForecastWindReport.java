package org.copia.windy.app.forecast;

import java.util.Date;

public class ForecastWindReport implements Comparable {

    private Date time;

    private GeoPoint place;

    private Double windSpeed;

    public ForecastWindReport( GeoPoint getPoint, Double windSpeed ) {
        this.time = new Date();
        this.place = getPoint;
        this.windSpeed = windSpeed;
    }

    public ForecastWindReport( Date time, GeoPoint place, Double windSpeed ) {
        this.time = time;
        this.place = place;
        this.windSpeed = windSpeed;
    }

    public Date getTime() {
        return time;
    }


    public GeoPoint getPlace() {
        return place;
    }

    public void setPlace( GeoPoint place ) {
        this.place = place;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed( Double windSpeed ) {
        this.windSpeed = windSpeed;
    }

    @Override
    public int compareTo( Object another ) {
        if ( another == null ) {
            return 1;
        }
        return time.compareTo( ((ForecastWindReport) another).getTime() );
    }
}
