package com.example.leertaak_three;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
        BlockingQueue<Measurement> storageQueue = new ArrayBlockingQueue<>(25000);
        AtomicInteger weatherdataCounter = new AtomicInteger(0);

        // This Station Array is used to hold all Stations. This is later used to calculate missing values.
        Station[] stationList = new Station[1000000];
        Arrays.fill(stationList, new Station());
        // As the name says, this holds the ThreadPools.
        ExecutorService[] threadPools = new ExecutorService[4];
        threadPools[0] = Executors.newCachedThreadPool();                        // Add Threads to Receiver threadpool
        threadPools[1] = Executors.newFixedThreadPool(5);               // Add Threads to Worker threadpool
        threadPools[2] = Executors.newFixedThreadPool(1);               // Add Threads to Inserter threadpool
        threadPools[3] = Executors.newFixedThreadPool(1);               // Add Threads to Counter threadpool

        threadPools[2].submit(new CheckAndStoreThread(storageQueue, stationList));

        // TODO: DIT KAN ER LANGZAMERHAND OOK UIT TOCH?
        threadPools[3].submit(new QueueWatcher(storageQueue, weatherdataCounter));  // DIT IS TIJDELIJK ofzo!
        // Needs to count the amount of requests that were handled


        // Loop to handle all incoming connections.
        //noinspection InfiniteLoopStatement                                     // This is just there for IntelliJ
        while (true) {
            // This accepts connections and spawns a thread per connection. The thread is automatically deleted / reused when it dies.
            Socket socket = serverSocket.accept();
            threadPools[0].submit(new ReceiverThread(socket, storageQueue, weatherdataCounter));
        }
    }

    public static void main(String[] args)
            throws IOException {
        new WeatherdataService(new ServerSocket(SERVER_PORT));
    }
}
