package com.example.leertaakt_two;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Robin on 8-12-2016.
 */
class WeatherdataReceiverThread implements Runnable {
    private WeatherdataStreamReader weatherdataStreamReader;

    WeatherdataReceiverThread(Socket socket, BlockingQueue<Weatherdata> weatherdata_queue){
        try {
           this.weatherdataStreamReader = new WeatherdataStreamReader(socket.getInputStream(), weatherdata_queue);
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
