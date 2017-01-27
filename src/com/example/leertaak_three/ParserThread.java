package com.example.leertaak_three;

import jdk.nashorn.internal.ir.Block;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Robin on 8-12-2016.
 */
class ParserThread implements Runnable {
    private BlockingQueue<ArrayList<String>> incomingQueue;
    private BlockingQueue<Measurement> outgoingQueue;
    private AtomicInteger counter;

    ParserThread(BlockingQueue<Measurement> queue, AtomicInteger counter, BlockingQueue<ArrayList<String>> incomingQueue) {
        this.incomingQueue = incomingQueue;
        this.outgoingQueue = queue;
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

                //ArrayList<String> take = incomingQueue.take();
                ArrayList<ArrayList<String>> newList = new ArrayList<>();
                this.incomingQueue.drainTo(newList, 1000);

                for (ArrayList<String> itemTakenFromIncomingQueue : newList) {
                    this.counter.getAndIncrement();
                    for (String weatherdataString : itemTakenFromIncomingQueue) {
                        if (weatherdataString.equals("</WEATHERDATA>")) {
                            // <Weatherdata> was just closed. Put it in the processing queue and create a new Weatherdata object for the next <Weatherdata>
                            for (Measurement measurement : weatherdata.getMeasurements()) {
                                this.outgoingQueue.put(measurement);
                            }
                            weatherdata = new Weatherdata();
                        } else {
                            weatherdata.addLine(weatherdataString);

                        }
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
    }
}
