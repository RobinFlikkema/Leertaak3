package com.example.leertaak_three;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WeatherdataService
 * This class is used to spawn the different threads, keep track of queues and accept new connections.
 * The constructor of this class takes a ServerSocket which is used to listen to new connections.
 */
class WeatherdataService {
    // The port used for receiving weatherdata
    private static final int SERVER_PORT = 7789;

    private WeatherdataService(ServerSocket serverSocket)
            throws IOException {
        // This queue hold Measurements waiting to be processed (checked for missing values etc)
        BlockingQueue<Measurement> storageQueue = new ArrayBlockingQueue<>(50000, true);
        // This queue hold Measurements waiting to be processed (checked for missing values etc)
        BlockingQueue<ArrayList<String>> incomingQueue = new ArrayBlockingQueue<>(50000, true);
        AtomicInteger parseCounter = new AtomicInteger(0);

        // This Station Array is used to hold all Stations. This is later used to calculate missing values.
        Station[] stationList = new Station[1000000];
        Arrays.fill(stationList, new Station());
        // As the name says, this holds the ThreadPools.
        ExecutorService[] threadPools = new ExecutorService[5];
        ScheduledExecutorService[] threadPools2 = new ScheduledExecutorService[1];
        threadPools[0] = Executors.newFixedThreadPool(800);              // Add Threads to Receiver threadpool
        threadPools[2] = Executors.newFixedThreadPool(1);               // Add Threads to Inserter threadpool
        threadPools2[0] = Executors.newScheduledThreadPool(1);               // Add Threads to Counter threadpool
        threadPools[4] = Executors.newFixedThreadPool(75);               // Add Threads to

        threadPools2[0].scheduleAtFixedRate(new QueueWatcher(storageQueue, incomingQueue, parseCounter), 0, 10, TimeUnit.SECONDS);
        threadPools[2].submit(new CheckAndStoreThread(storageQueue, stationList));
        threadPools[4].submit(new ParserThread(storageQueue, parseCounter, incomingQueue));
        threadPools[4].submit(new ParserThread(storageQueue, parseCounter, incomingQueue));


        // Loop to handle all incoming connections.
        //noinspection InfiniteLoopStatement                                     // This is just there for IntelliJ
        while (true) {
            // This accepts connections and spawns a thread per connection. The thread is automatically deleted / reused when it dies.
            Socket socket = serverSocket.accept();
            threadPools[0].submit(new ReceiverThread(socket, incomingQueue));
        }
    }

    public static void main(String[] args)
            throws IOException {
        new WeatherdataService(new ServerSocket(SERVER_PORT));
    }
}
