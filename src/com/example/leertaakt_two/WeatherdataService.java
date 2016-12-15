package com.example.leertaakt_two;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Robin on 8-12-2016.
 */
class WeatherdataService {

    private ExecutorService receiver = Executors.newFixedThreadPool(5);

    WeatherdataService(ServerSocket serverSocket)
            throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            Future future = receiver.submit(new WeatherdataReceiverThread(socket));
            System.out.println(future.isDone());
        }
    }
}
