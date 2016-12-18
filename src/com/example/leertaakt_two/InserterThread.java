package com.example.leertaakt_two;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Robin on 17-12-2016.
 */
public class InserterThread implements Runnable {
    private Database database;
    private BlockingQueue<Measurement> queue;

    InserterThread(BlockingQueue<Measurement> queue) {
        this.database = new Database();
        this.queue = queue;
    }

    @Override public void run() {
        while (true) {
            ArrayList<Measurement> listOfMeasurements = new ArrayList<Measurement>();
            try {
                    for (int i = 0; i < 10; i++) {
                        listOfMeasurements.add(queue.take());
                    }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Queue");
            database.insertMeasurements(listOfMeasurements);
        }
    }
}
