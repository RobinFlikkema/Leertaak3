package com.example.leertaakt_two;

import java.util.ArrayList;

/**
 * Created by Robin on 9-12-2016.
 */
class Weatherdata {
    private Measurement measurement = new Measurement();
    private ArrayList<Measurement> weather_data = new ArrayList<>();
    private Database db = new Database();

    Weatherdata(){

    }

    void addLine(String line){
        if (!line.contains("<?xml") && !line.contains("WEATHERDATA")) {
            if (line.contains("<MEASUREMENT>")){
                measurement = new Measurement();
            } else if (line.contains("</MEASUREMENT>")){
                weather_data.add(measurement);
            } else {
                measurement.addValue(line.trim());
            }
        }
    }

    void printWeatherdata(){
        for (Measurement row: weather_data) {
            System.out.println("printWeatherdata");
            db.insertMeasurement(row);
            // TODO: Change this to use queueing system (?) (max connections)
            // TODO: Fix Booleans inserting (1 is not 1)
            // TODO: Improve processing system
            // TODO: Correct values accordingly
            System.out.println(row.toString());
        }
    }
}
