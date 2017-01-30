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
        BlockingQueue<Measurement> checkQueue = new ArrayBlockingQueue<>(100000, true);
        // This queue hold Measurements waiting to be processed (checked for missing values etc)
        BlockingQueue<Measurement> storeQueue = new ArrayBlockingQueue<>(100000, true);
        // This queue hold Measurements waiting to be processed (checked for missing values etc)
        BlockingQueue<ArrayList<String>> incomingQueue = new ArrayBlockingQueue<>(100000, true);
        AtomicInteger parseCounter = new AtomicInteger(0);

        // This Station Array is used to hold all Stations. This is later used to calculate missing values.
        Station[] stationList = new Station[1000000];
        Arrays.fill(stationList, new Station());
        // Receiver Threads
        ExecutorService receiverThreadPool = Executors.newCachedThreadPool();
        // Parser Threads
        ExecutorService parserThreadPool = Executors.newFixedThreadPool(2);
        parserThreadPool.submit(new ParserThread(checkQueue, parseCounter, incomingQueue));
        parserThreadPool.submit(new ParserThread(checkQueue, parseCounter, incomingQueue));
        // Check Thread
        ExecutorService checkerThreadPool = Executors.newFixedThreadPool(2);
        checkerThreadPool.submit(new CheckThread(checkQueue, storeQueue, stationList));
        // Store Thread
        ExecutorService storeThreadPool = Executors.newFixedThreadPool(2);
        storeThreadPool.submit(new StoreThread(storeQueue));


        // Queue Watcher Thread
        ScheduledExecutorService[] threadPools2 = new ScheduledExecutorService[1];
        threadPools2[0] = Executors.newScheduledThreadPool(1);
        threadPools2[0].scheduleAtFixedRate(new QueueWatcher(checkQueue, incomingQueue, storeQueue, parseCounter), 0, 10, TimeUnit.SECONDS);


        // Loop to handle all incoming connections.
        //noinspection InfiniteLoopStatement                                     // This is just there for IntelliJ
        while (true) {
            // This accepts connections and spawns a thread per connection. The thread is automatically deleted / reused when it dies.
            Socket socket = serverSocket.accept();
            receiverThreadPool.submit(new ReceiverThread(socket, incomingQueue));
        }
    }

    public static void main(String[] args)
            throws IOException {
        new WeatherdataService(new ServerSocket(SERVER_PORT));
    }
}
