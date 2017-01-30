package com.example.leertaak_three;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Robin on 17-12-2016.
 */
class CheckThread implements Runnable {
    private final BlockingQueue<Measurement> checkQueue;
    private final BlockingQueue<Measurement> storeQueue;
    private Station[] stations;

    CheckThread(BlockingQueue<Measurement> checkQueue, BlockingQueue<Measurement> storeQueue, Station[] stations) {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        this.checkQueue = checkQueue;
        this.storeQueue = storeQueue;
        this.stations = stations;
    }

    @Override public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            ArrayList<Measurement> incomingListOfMeasurements = new ArrayList<>();
            ArrayList<Measurement> outgoingListOfMeasurements = new ArrayList<>();
            if (checkQueue.size() > 100) {
                checkQueue.drainTo(incomingListOfMeasurements, 100000);
                for (Measurement measurement : incomingListOfMeasurements) {
                    measurement = this.checkMeasurement(measurement);
                    stations[measurement.getStationNumber()].addMeasurement(measurement);
                    outgoingListOfMeasurements.add(measurement);
                }
                storeQueue.addAll(outgoingListOfMeasurements);
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
