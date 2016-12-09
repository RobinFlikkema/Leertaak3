package com.example.leertaakt_two;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Robin on 8-12-2016.
 */
class WeatherdataReceiverThread implements Runnable {
    private Socket socket;
    private WeatherdataStreamReader weatherdataStreamReader;

    WeatherdataReceiverThread(Socket socket){
        System.out.println("New Thread");
        this.socket = socket;
        try {
           this.weatherdataStreamReader = new WeatherdataStreamReader(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void run() {
        while (true){
            if (!this.weatherdataStreamReader.receiveWeatherdata()){
                return;
            }
        }
    }
}
