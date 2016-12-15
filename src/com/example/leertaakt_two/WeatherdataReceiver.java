package com.example.leertaakt_two;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Robin on 8-12-2016.
 */
class WeatherdataReceiver {

    private ExecutorService receiver = Executors.newFixedThreadPool(5);

    WeatherdataReceiver(ServerSocket serverSocket)
            throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            receiver.submit(new WeatherdataReceiverThread(socket));

        }
    }
}
