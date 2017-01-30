package com.example.leertaak_three;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Robin on 17-12-2016.
 */
class StoreThread implements Runnable {
    private final CSV CSV;
    private final BlockingQueue<Measurement> storeQueue;

    StoreThread(BlockingQueue<Measurement> storeQueue) {
        this.CSV = new CSV();
        this.storeQueue = storeQueue;
    }

    @Override public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            ArrayList<Measurement> incomingListOfMeasurements = new ArrayList<>();

            if (storeQueue.size() > 1000) {
                storeQueue.drainTo(incomingListOfMeasurements, 10000);
                CSV.insertMeasurements(incomingListOfMeasurements);

            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
