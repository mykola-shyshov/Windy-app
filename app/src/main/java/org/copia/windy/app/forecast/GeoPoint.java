package org.copia.windy.app.forecast;

public class GeoPoint {

    private String title;

    // Широта
    private Double latitude;

    private Double longitude;

    public GeoPoint( Double latitude, Double longitude ) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GeoPoint( String title, Double latitude, Double longitude ) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude( Double latitude ) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude( Double longitude ) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( !( o instanceof GeoPoint ) ) {
            return false;
        }

        GeoPoint geoPoint = (GeoPoint) o;

        if ( title != null ? !title.equals( geoPoint.title ) : geoPoint.title != null ) {
            return false;
        }
        if ( latitude != null ? !latitude.equals( geoPoint.latitude ) : geoPoint.latitude != null ) {
            return false;
        }
        return !( longitude != null ? !longitude.equals( geoPoint.longitude ) : geoPoint.longitude != null );

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + ( latitude != null ? latitude.hashCode() : 0 );
        result = 31 * result + ( longitude != null ? longitude.hashCode() : 0 );
        return result;
    }
}
