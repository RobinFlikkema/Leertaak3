package com.example.leertaakt_two;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Robin on 17-12-2016.
 */
public class ProcessorThread implements Runnable {
    private BlockingQueue<Measurement> processingQueue;
    private BlockingQueue<Measurement> storageQueue;
    private Station[] stations;

    ProcessorThread(BlockingQueue<Measurement> processingQueue, BlockingQueue<Measurement> storageQueue, Station[] stations) {
        this.processingQueue = processingQueue;
        this.storageQueue = storageQueue;
        this.stations = stations;
    }

    @Override public void run() {
        while (true) {
                // TODO: Some form of map which stores the last values
                try {
                    Measurement measurement = processingQueue.take();
                    measurement = this.checkMeasurement(measurement);
                    stations[measurement.getStationNumber()].addMeasurement(measurement);
                    storageQueue.put(measurement);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
