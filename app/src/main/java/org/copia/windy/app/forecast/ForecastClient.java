package org.copia.windy.app.forecast;


import java.io.IOException;
import java.util.List;

public interface ForecastClient {

    public List<ForecastWindReport> getForecast( List<GeoPoint> points ) throws IOException;

    public List<ForecastWindReport> getCurrent( List<GeoPoint> points ) throws IOException;

}
