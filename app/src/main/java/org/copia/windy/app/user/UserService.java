package org.copia.windy.app.user;

import org.copia.windy.app.forecast.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    /**
     * Not null!
     * @return
     */
    public List<GeoPoint> getGeoPoints() {
        List<GeoPoint> points = new ArrayList<GeoPoint>();
        points.add( new GeoPoint( "FunSurf - Вышгород", 50.624973, 30.525142 ) );
        points.add( new GeoPoint( "FunSurf - Троещина", 50.503381, 30.568194 ) );
        return points;
    }
}
