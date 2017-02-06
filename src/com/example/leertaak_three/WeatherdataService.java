package com.example.leertaak_three;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WeatherdataService
 * This class is used to spawn the different threads, keep track of queues and accept new connections.
 * The constructor of this class takes a ServerSocket which is used to listen to new connections.
 *
 * @author Robin Flikkema
 */
class WeatherdataService {
    // The port used for receiving weatherdata.
    private static final int SERVER_PORT = 7789;

    private WeatherdataService(ServerSocket serverSocket) {
        // This queue hold Measurements waiting to be processed (checked for missing values etc)
        BlockingQueue<Measurement> checkQueue = new ArrayBlockingQueue<>(100000, true);
        // This queue hold Measurements waiting to be stored
        BlockingQueue<Measurement> storeQueue = new ArrayBlockingQueue<>(100000, true);
        // This queue hold Measurements waiting to be parsed
        BlockingQueue<ArrayList<String>> parseQueue = new ArrayBlockingQueue<>(100000, true);
        // This integer holds the amount of weatherdata that is parsed.
        AtomicInteger parseCounter = new AtomicInteger(0);

        // This Station Array is used to hold all Stations. This is later used to calculate missing values.
        Station[] stationList = new Station[1000000];
        for (int i = 0; i < 1000000; i++) {
            stationList[i] = new Station();
        }
        // Receiver Threads
        ExecutorService receiverThreadPool = Executors.newCachedThreadPool();
        // Parser Threads
        ExecutorService parserThreadPool = Executors.newFixedThreadPool(2);
        parserThreadPool.submit(new ParserThread(checkQueue, parseCounter, parseQueue));
        // Check Thread
        ExecutorService checkerThreadPool = Executors.newFixedThreadPool(2);
        checkerThreadPool.submit(new CheckThread(checkQueue, storeQueue, stationList));
        // Store Thread
        ExecutorService storeThreadPool = Executors.newFixedThreadPool(2);
        storeThreadPool.submit(new StoreThread(storeQueue));
        // Queue Watcher Thread (which we aren't using at the moment)
        //ScheduledExecutorService queueWatcherPool = Executors.newScheduledThreadPool(1);
        //queueWatcherPool.scheduleAtFixedRate(new QueueWatcher(checkQueue, incomingQueue, storeQueue, parseCounter), 0, 10, TimeUnit.SECONDS);

        while (true) {
            try {
                // This accepts connections and spawns a thread per connection.
                Socket socket = serverSocket.accept();
                receiverThreadPool.submit(new ReceiverThread(socket, parseQueue));
            } catch (IOException e) {
                // This only happens when an I/O Error occurs while waiting for a connection.
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("Bezig met starten van server op poort " + SERVER_PORT);
            new WeatherdataService(new ServerSocket(SERVER_PORT));
        } catch (IOException e) {
            System.out.println("Error opening socket");
        }
    }
}
