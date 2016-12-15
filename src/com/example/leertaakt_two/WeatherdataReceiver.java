package com.example.leertaakt_two;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Robin on 8-12-2016.
 */
class WeatherdataReceiver {
    WeatherdataReceiver(ServerSocket serverSocket)
            throws IOException {
        int counter = 0;
        while (true) {
            Socket socket = serverSocket.accept();
//            ProcessWeatherdata(socket.getInputStream());

            Thread weatherdataProcessor = new Thread(new WeatherdataReceiverThread(socket));
            weatherdataProcessor.start();

            counter++;
            System.out.println(counter);
        }
    }
}
