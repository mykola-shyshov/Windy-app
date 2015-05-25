package org.copia.windy.app.forecast;

import java.util.Date;
import java.util.List;
import java.util.SortedMap;

public class ForecasetWindReportPrediction {

    private SortedMap<Date, List<ForecastWindReport>> prediction;

    public SortedMap<Date, List<ForecastWindReport>> getPrediction() {
        return prediction;
    }

    public void setPrediction( SortedMap<Date, List<ForecastWindReport>> prediction ) {
        this.prediction = prediction;
    }
}
