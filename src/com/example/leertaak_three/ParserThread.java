package com.example.leertaak_three;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Robin on 8-12-2016.
 */
class ParserThread implements Runnable {
    private final BlockingQueue<ArrayList<String>> incomingQueue;
    private final BlockingQueue<Measurement> outgoingQueue;
    private final AtomicInteger counter;

    ParserThread(BlockingQueue<Measurement> queue, AtomicInteger counter, BlockingQueue<ArrayList<String>> incomingQueue) {
        this.incomingQueue = incomingQueue;
        this.outgoingQueue = queue;
        this.counter = counter;
    }

    @Override public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            this.receiveWeatherdata();
        }
    }

    // TODO: Needs refactoring
    private void receiveWeatherdata() {
        try {
            if (incomingQueue.size() > 100) {
                ArrayList<ArrayList<String>> newList = new ArrayList<>();
                this.incomingQueue.drainTo(newList, 10000);

                for (ArrayList<String> itemTakenFromIncomingQueue : newList) {
                    // New Weatherdata
                    this.counter.getAndIncrement();
                    int lineCounter = 0;
                    int measurementLineCounter = 0;
                    Measurement measurement = new Measurement();

                    for (String line : itemTakenFromIncomingQueue) {

                        // New Line of weatherdata
                        if (lineCounter < 162) {
                            // Line does not contain </WEATHERDATA>
                            if ((lineCounter > 1)) {
                                if (measurementLineCounter == 0) {
                                    // LINE == <MEASUREMENT>
                                    measurementLineCounter++;
                                    lineCounter++;
                                } else if (measurementLineCounter < 15) {
                                    // Line is Measurement Value
                                    measurement.addValue(line);
                                    measurementLineCounter++;
                                    lineCounter++;
                                } else {
                                    // LINE == </MEASUREMENT>
                                    this.outgoingQueue.put(measurement);
                                    measurement = new Measurement();
                                    measurementLineCounter = 0;
                                    lineCounter++;
                                }
                            } else {
                                // LINE == <WEATHERDATA> OR LINE == <?XML ...?>
                                lineCounter++;
                            }
                        } else {
                            lineCounter = 0;
                        }
                    }
                }
            } else {
                Thread.sleep(1000);
            }

        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }
}
