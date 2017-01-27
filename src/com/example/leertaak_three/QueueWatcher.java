package com.example.leertaak_three;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Robin on 15-12-2016.
 */
class QueueWatcher implements Runnable {
    private final BlockingQueue<Measurement> processingQueue;
    private final BlockingQueue<ArrayList<String>> incomingQueue;
    private final AtomicInteger counter;
    private long PreviousCounter = 0;
    private final Date now;

    QueueWatcher(BlockingQueue<Measurement> processingQueue, BlockingQueue<ArrayList<String>> incomingQueue, AtomicInteger counter) {
        this.processingQueue = processingQueue;
        this.counter = counter;
        this.incomingQueue = incomingQueue;
        this.now = new java.util.Date();
    }

    @Override public void run() {
        long diff = (Math.abs(now.getTime() - (new java.util.Date().getTime())) / 1000);
        System.out.println("Begonnen: " + now.toString());
        System.out.println("Tijd bezig: " + (diff / 60) + ":" + (diff % 60));
        System.out.println();
        System.out.println("Weatherdata parsed in last 10 seconds: " + (this.counter.get() - PreviousCounter));
        System.out.println("Weatherdata parsed (total): " + this.counter.get());
        System.out.println("");
        System.out.println("Grootte van queues: " + incomingQueue.size() + "," + processingQueue.size());
        System.out.println("=============================================");
        this.PreviousCounter = this.counter.get();

    }

}
