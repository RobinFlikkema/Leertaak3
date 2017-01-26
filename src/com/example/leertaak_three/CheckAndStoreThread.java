package com.example.leertaak_three;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Robin on 17-12-2016.
 */
class CheckAndStoreThread implements Runnable {
    private CSV CSV;
    private BlockingQueue<Measurement> queue;
    private Station[] stations;

    CheckAndStoreThread(BlockingQueue<Measurement> queue, Station[] stations) {
        this.CSV = new CSV();
        this.queue = queue;
        this.stations = stations;
    }

    @Override public void run() {
        while (true) {
            ArrayList<Measurement> listOfMeasurements = new ArrayList<>();
            try {
                    for (int i = 0; i < 10000; i++) {
                        Measurement measurement = queue.take();
                        //measurement = this.checkMeasurement(measurement);
                        //stations[measurement.getStationNumber()].addMeasurement(measurement);
                        //listOfMeasurements.add(measurement);
                    }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CSV.insertMeasurements(listOfMeasurements);
        }
    }

    private Measurement checkMeasurement(Measurement measurement){
        int indexOfMissingValue = measurement.valueIsMissing();
        int stationNumber = measurement.getStationNumber();
        double temp = measurement.getTemperature();
        if (indexOfMissingValue > 0 && 12 > indexOfMissingValue){
            measurement.setValue(indexOfMissingValue, String.valueOf(stations[stationNumber].getExtrapolatedValue(indexOfMissingValue)));
        } else if (indexOfMissingValue > 11){
            measurement.setValue(indexOfMissingValue, "0");
        }
        if (!stations[stationNumber].isTemperaturePlausible(temp)){
            measurement.setValue(3 ,String.valueOf(stations[stationNumber].getExtrapolatedTemperature()));
        }
        return measurement;
    }
}
