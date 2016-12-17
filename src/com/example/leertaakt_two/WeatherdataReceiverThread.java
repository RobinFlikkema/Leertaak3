package com.example.leertaakt_two;

import java.net.Socket;

/**
 * Created by Robin on 8-12-2016.
 */
class WeatherdataReceiverThread implements Runnable {
    private WeatherdataStreamReader weatherdataStreamReader;

    WeatherdataReceiverThread(Socket socket){
        System.out.println("new Thread");
        try {
           this.weatherdataStreamReader = new WeatherdataStreamReader(socket.getInputStream());
        } catch (Exception e) {
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
