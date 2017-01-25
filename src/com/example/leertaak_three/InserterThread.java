package com.example.leertaak_three;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Robin on 17-12-2016.
 */
class InserterThread implements Runnable {
    private CSV CSV;
    private BlockingQueue<Measurement> queue;

    InserterThread(BlockingQueue<Measurement> queue) {
        this.CSV = new CSV();
        this.queue = queue;
    }

    @Override public void run() {
        while (true) {
            ArrayList<Measurement> listOfMeasurements = new ArrayList<Measurement>();
            try {
                    for (int i = 0; i < 100; i++) {
                        listOfMeasurements.add(queue.take());
                    }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Queue");
            CSV.insertMeasurements(listOfMeasurements);
        }
    }
}
