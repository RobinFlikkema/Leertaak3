package com.example.leertaakt_two;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * WeatherdataService
 * This service is home for all Threads and Threadpools which handle:
 * - The receiving of Measurements
 * - TODO: The processing of data and correcting it
 * All threadPools live within ExecutorSerivce[] threadPools.
 * threadPools[0] houses all Threads which handle incoming connections
 * threadPools[1] TODO:
 * threadPools[2] TODO:
 */
class WeatherdataService {

    WeatherdataService(ServerSocket serverSocket)
            throws IOException {
        BlockingQueue<Measurement> processingQueue = new ArrayBlockingQueue<Measurement>(2500);
        BlockingQueue<Measurement> storageQueue = new ArrayBlockingQueue<Measurement>(2500);

        ExecutorService[] threadPools = new ExecutorService[4];                  // Create Executor service (e.g. Threadpools)
                                                                                 // https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html
        threadPools[0] = Executors.newCachedThreadPool();                        // Add Threads to Receiver threadpool
        threadPools[1] = Executors.newFixedThreadPool(5);               // Add Threads to Worker threadpool
        threadPools[2] = Executors.newFixedThreadPool(15);              // Add Threads to MySQL threadpool
        threadPools[3] = Executors.newFixedThreadPool(1);               // Add Threads to Counter threadpool

        threadPools[1].submit(new ProcessorThread(processingQueue, storageQueue));//
        for (int i = 0; i < 15; i++){
            threadPools[2].submit(new InserterThread(storageQueue));             //
        }
        threadPools[3].submit(new QueueWatcher(processingQueue, storageQueue));  // DIT IS TIJDELIJK ofzo!
        // Needs to count the amount of requests that were handled


        //noinspection InfiniteLoopStatement                                     // This is just there for IntelliJ
        while (true) {                                                           // Keep listening for clients
            Socket socket = serverSocket.accept();                               // Accept new clients on socket
            threadPools[0].submit(new WeatherdataReceiverThread(socket, processingQueue));   // Submit task (handle client) to a Thread in Threadpool
        }
    }
}
