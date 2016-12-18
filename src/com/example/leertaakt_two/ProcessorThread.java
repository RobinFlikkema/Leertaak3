package com.example.leertaakt_two;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Robin on 17-12-2016.
 */
public class ProcessorThread implements Runnable {
    private BlockingQueue<Measurement> processingQueue;
    private BlockingQueue<Measurement> storageQueue;

    ProcessorThread(BlockingQueue<Measurement> processingQueue, BlockingQueue<Measurement> storageQueue) {
        this.processingQueue = processingQueue;
        this.storageQueue = storageQueue;
    }

    @Override public void run() {
        while (true) {
            synchronized (this) {
                // TODO: Some form of map which stores the last values
                try {
                    storageQueue.put(processingQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
