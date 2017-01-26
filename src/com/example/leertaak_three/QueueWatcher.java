package com.example.leertaak_three;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Robin on 15-12-2016.
 */
class QueueWatcher implements Runnable {
    private BlockingQueue<Measurement> processingQueue;
    private BlockingQueue<Measurement> storageQueue;
    private AtomicInteger counter;

    QueueWatcher(BlockingQueue<Measurement> processingQueue, BlockingQueue<Measurement> storageQueue, AtomicInteger counter) {
        this.processingQueue = processingQueue;
        this.storageQueue = storageQueue;
        this.counter = counter;
    }

    @Override public void run() {
        Date now = new java.util.Date();
        long PreviousCounter = 0;
        long anotherCounter = 0;

        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long diff = (Math.abs(now.getTime() - (new java.util.Date().getTime())) / 1000);
            System.out.println("Begonnen: " + now.toString());
            System.out.println("Tijd bezig: " + (diff / 60) + ":" + (diff % 60));
            System.out.println();
            System.out.println("Weatherdata handled per second: " + (this.counter.get() - PreviousCounter));
            System.out.println("Weatherdata handled: " + this.counter.get());
            System.out.println("Grootte van queues: " + processingQueue.size() + ", " + storageQueue.size());
            System.out.println("=============================================");
            PreviousCounter = this.counter.get();
        }
    }

}
