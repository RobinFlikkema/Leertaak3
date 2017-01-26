package com.example.leertaak_three;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Robin on 8-12-2016.
 */
class WeatherdataReceiverThread implements Runnable {
    private BufferedReader bufferedReader = null;
    private BlockingQueue<Measurement> queue;
    private AtomicInteger counter;

    WeatherdataReceiverThread(Socket socket, BlockingQueue<Measurement> queue, AtomicInteger counter) {
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.queue = queue;
        this.counter = counter;
    }

    @Override public void run() {
        while (true) {
            if (!this.receiveWeatherdata()) {
                break;
            }
        }
    }

    // TODO: Needs refactoring
    private Boolean receiveWeatherdata() {
        try {
//            int counter = 0;
            Weatherdata weatherdata = new Weatherdata();
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    weatherdata.addLine(line);

                    this.counter.getAndIncrement();
//                    System.out.println("++" + this.counter);

                    if (line.contains("</WEATHERDATA>")) {
                        // <Weatherdata> was just closed. Put it in the processing queue and create a new Weatherdata object for the next <Weatherdata>
                        for (Measurement measurement : weatherdata.getMeasurements()) {
                            this.queue.put(measurement);
                        }
                        weatherdata = new Weatherdata();
                        //TODO: Waar gebruiken we deze counter ook alweer voor?
//                        counter++;
//                        if (counter > 9) {
//                            return true;
//                        }
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
