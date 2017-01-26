package com.example.leertaak_three;

import java.util.ArrayList;

/**
 * Created by Robin on 9-12-2016.
 */
class Weatherdata {
    private Measurement measurement = new Measurement();
    private ArrayList<Measurement> weather_data = new ArrayList<>();

    Weatherdata(){
    }

    void addLine(String line){
        if (!line.equals("<?xml version=\"1.0\"?>") && !line.equals("<WEATHERDATA>") && !line.equals("</WEATHERDATA>")) {
            if (line.equals("\t<MEASUREMENT>")){
                measurement = new Measurement();
            } else if (line.equals("\t</MEASUREMENT>")){
                weather_data.add(measurement);
            } else {
                measurement.addValue(line.trim());
            }
        }
    }

    ArrayList<Measurement> getMeasurements(){
        return weather_data;
    }
}
