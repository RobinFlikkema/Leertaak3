package com.example.leertaakt_two;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Robin on 15-12-2016.
 */
public class QueueWatcher implements Runnable {
    private BlockingQueue<Measurement> processingQueue;
    private BlockingQueue<Measurement> storageQueue;

    QueueWatcher(BlockingQueue<Measurement> processingQueue, BlockingQueue<Measurement> storageQueue) {
        this.processingQueue = processingQueue;
        this.storageQueue = storageQueue;
    }

    @Override public void run() {
        // TODO: Change this to count the amount of Measurements handled
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(processingQueue.size() + ", " + storageQueue.size());
        }
    }

}
