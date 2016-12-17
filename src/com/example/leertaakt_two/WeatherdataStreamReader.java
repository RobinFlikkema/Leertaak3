package com.example.leertaakt_two;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Robin on 8-12-2016.
 */
class WeatherdataStreamReader {
    private BufferedReader bufferedReader = null;
    private BlockingQueue<Weatherdata> weatherdata_queue;

    WeatherdataStreamReader(InputStream input, BlockingQueue<Weatherdata> weatherdata_queue) {
        this.bufferedReader = new BufferedReader(new InputStreamReader(input));
        this.weatherdata_queue = weatherdata_queue;
    }

    Boolean receiveWeatherdata() {
        try {
            int counter = 0;
            Weatherdata weatherdata = new Weatherdata();
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
//                    System.out.println(line);
                    weatherdata.addLine(line);

                    if (line.contains("</WEATHERDATA>")) {
                        weatherdata_queue.add(weatherdata);
                        weatherdata = new Weatherdata();
                        counter++;
                        if (counter > 9) {
                            return true;
                        }
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }
}