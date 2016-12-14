package com.example.leertaakt_two;

import java.util.ArrayList;

/**
 * Created by Robin on 9-12-2016.
 */
class Weatherdata {
    private ArrayList<String> XML = new ArrayList<>();
    private Measurement measurement = new Measurement();
    private ArrayList<Measurement> weatherdata = new ArrayList<>();
    private Database db = new Database();

    Weatherdata(){

    }

    void addLine(String line){
        if (!line.contains("<?xml") && !line.contains("WEATHERDATA")) {
            if (line.contains("<MEASUREMENT>")){
                measurement = new Measurement();
            } else if (line.contains("</MEASUREMENT>")){
                weatherdata.add(measurement);
            } else {
                measurement.addValue(line.trim());
            }
        }
    }

    void printWeatherdata(){
        for (Measurement row:weatherdata) {
            db.insertMeasurement(row);
            System.out.println(row.toString());
        }
    }
}
