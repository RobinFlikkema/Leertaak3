package com.example.leertaak_three;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Robin on 17-12-2016.
 */
class CheckAndStoreThread implements Runnable {
    private final CSV CSV;
    private final BlockingQueue<Measurement> queue;
    private final Station[] stations;

    CheckAndStoreThread(BlockingQueue<Measurement> queue, Station[] stations) {
        this.CSV = new CSV();
        this.queue = queue;
        this.stations = stations;
    }

    @Override public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            ArrayList<Measurement> incomingListOfMeasurements = new ArrayList<>();
            ArrayList<Measurement> outgoingListOfMeasurements = new ArrayList<>();

            if (queue.size() > 1000) {
                queue.drainTo(incomingListOfMeasurements, 10000);
                for (Measurement measurement : incomingListOfMeasurements) {
                    measurement = this.checkMeasurement(measurement);
                    stations[measurement.getStationNumber()].addMeasurement(measurement);
                    outgoingListOfMeasurements.add(measurement);
                }
                CSV.insertMeasurements(outgoingListOfMeasurements);

            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Measurement checkMeasurement(Measurement measurement) {
        int indexOfMissingValue = measurement.valueIsMissing();
        int stationNumber = measurement.getStationNumber();
        double temp = measurement.getTemperature();
        if (indexOfMissingValue > 11 && 11 > indexOfMissingValue) {
            measurement.setValue(indexOfMissingValue, String.valueOf(stations[stationNumber].getExtrapolatedValue(indexOfMissingValue)));
        }
        if (!stations[stationNumber].isTemperaturePlausible(temp)) {
            measurement.setValue(3, String.valueOf(stations[stationNumber].getExtrapolatedTemperature()));
        }
        return measurement;
    }
}
