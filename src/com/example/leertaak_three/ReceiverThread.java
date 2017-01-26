package com.example.leertaak_three;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Robin on 8-12-2016.
 */
class ReceiverThread implements Runnable {
    private BufferedReader bufferedReader = null;
    private BlockingQueue<Measurement> queue;
    private AtomicInteger counter;

    ReceiverThread(Socket socket, BlockingQueue<Measurement> queue, AtomicInteger counter) {
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(socket.getInputStream()), "UTF-8"));
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
            Weatherdata weatherdata = new Weatherdata();
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    weatherdata.addLine(line);
                    this.counter.getAndIncrement();
                    if (line.equals("</WEATHERDATA>")) {
                        // <Weatherdata> was just closed. Put it in the processing queue and create a new Weatherdata object for the next <Weatherdata>
                        for (Measurement measurement : weatherdata.getMeasurements()) {
                            this.queue.put(measurement);
                        }
                        weatherdata = new Weatherdata();
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
