package com.example.leertaakt_two;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Robin on 8-12-2016.
 */
class WeatherdataService {

    WeatherdataService(ServerSocket serverSocket, BlockingQueue<Weatherdata> weatherdata_queue) throws IOException {

        ExecutorService receiver = Executors.newCachedThreadPool();

        while (true) {
            Socket socket = serverSocket.accept();
            receiver.submit(new WeatherdataReceiverThread(socket, weatherdata_queue));
        }
    }
}