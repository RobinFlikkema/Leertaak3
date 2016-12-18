package com.example.leertaakt_two;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Robin on 8-12-2016.
 */
class WeatherdataReceiverThread implements Runnable {
    private BufferedReader bufferedReader = null;
    private BlockingQueue<Measurement> queue;

    WeatherdataReceiverThread(Socket socket, BlockingQueue<Measurement> queue){
        System.out.println("new Thread");
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.queue = queue;
    }

    @Override public void run() {
        while (true){
            if (!this.receiveWeatherdata()){
                break;
            }
        }
    }

    // TODO: Needs refactoring
    private Boolean receiveWeatherdata() {
        try {
            int counter = 0;
            Weatherdata weatherdata = new Weatherdata();
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    weatherdata.addLine(line);

                    if (line.contains("</WEATHERDATA>")) {
                        System.out.println("XML Closed");
                        for (Measurement measurement : weatherdata.getMeasurements()){
                            this.queue.put(measurement);
                        }
                        weatherdata = new Weatherdata();
                        counter++;
                        if (counter > 9) {
                            return true;
                        }
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
