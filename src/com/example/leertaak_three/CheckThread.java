package com.example.leertaak_three;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * This class can be used to create a Thread which checks Measurements and puts them into another Queue
 *
 * @author Robin Flikkema
 */
class CheckThread implements Runnable {
    private final BlockingQueue<Measurement> checkQueue;
    private final BlockingQueue<Measurement> storeQueue;
    private static Station[] stations;

    /**
     * @param checkQueue , the Queue with Measurements to be Checked
     * @param storeQueue , the Queue with Measurements that are Checked
     * @param stations , the Station Lists with the latest Measurements
     */
    CheckThread(BlockingQueue<Measurement> checkQueue, BlockingQueue<Measurement> storeQueue, Station[] stations) {
        this.checkQueue = checkQueue;
        this.storeQueue = storeQueue;
        CheckThread.stations = stations;
    }

    /**
     * This functions loops to check for new Measurements to check, then checks them and puts them onto the StoreQueue.
     */
    @Override public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            ArrayList<Measurement> incomingListOfMeasurements = new ArrayList<>();
            ArrayList<Measurement> outgoingListOfMeasurements = new ArrayList<>();
            if (checkQueue.size() > 100) {
                checkQueue.drainTo(incomingListOfMeasurements, 25000);
                for (Measurement measurement : incomingListOfMeasurements) {
                    measurement = this.checkMeasurement(measurement);
                    stations[measurement.getStationNumber()].addMeasurement(measurement);
                    outgoingListOfMeasurements.add(measurement);
                }
                storeQueue.addAll(outgoingListOfMeasurements);
            } else {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param measurement , The Measurement to be checked
     *
     * @return , The checked version of the Measurement.
     */
    private Measurement checkMeasurement(Measurement measurement) {
        int indexOfMissingValue = measurement.valueIsMissing();
        int stationNumber = measurement.getStationNumber();
        double temp = measurement.getTemperature();

        // If there's no missing value the index will be -1
        // If the missing value is a binary value it will be 11 or larger
        if ( indexOfMissingValue < 11 && indexOfMissingValue > 0) {
            String newValue = String.valueOf(stations[stationNumber].getExtrapolatedValue(indexOfMissingValue));
            measurement.setValue(indexOfMissingValue, newValue);
        }
        if (!stations[stationNumber].isTemperaturePlausible(temp)) {
            measurement.setValue(3, String.valueOf(stations[stationNumber].getExtrapolatedTemperature()));
        }
        return measurement;
    }
}
