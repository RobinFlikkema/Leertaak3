package com.example.leertaak_three;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * Store Thread
 * This class is used to store Measurements from the storageQueue into a CSV. This file creates a CSV object, which
 * inserts measurements into a file
 *
 * @author Robin Flikkema
 * @see CSV
 */
class StoreThread implements Runnable {
    private final CSV CSV;
    private final BlockingQueue<Measurement> storeQueue;

    /**
     * @param storeQueue is the Queue that this class uses to take Measurements and store them.
     */
    StoreThread(BlockingQueue<Measurement> storeQueue) {
        this.CSV = new CSV();
        this.storeQueue = storeQueue;
    }

    /**
     * This is a while loop which checks if there are more than 500 items in the Queue to be stored. We use this check
     * to make sure that this thread doesn't run for a small amount of items as it involes opening and closing files
     * which might be very expensive
     * If there are more then 500 items on the Queue this method will take a maximum of 5.000 items and store them
     */
    @Override public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            ArrayList<Measurement> incomingListOfMeasurements = new ArrayList<>();

            if (storeQueue.size() > 500) {
                storeQueue.drainTo(incomingListOfMeasurements, 5000);
                CSV.insertMeasurements(incomingListOfMeasurements);

            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
