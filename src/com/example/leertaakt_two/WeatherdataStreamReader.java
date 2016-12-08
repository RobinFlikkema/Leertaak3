package com.example.leertaakt_two;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Robin on 8-12-2016.
 */
class WeatherdataStreamReader {
    private BufferedReader bufferedReader = null;

    WeatherdataStreamReader(InputStream input) {
        System.out.println("Streamreader created");
        this.bufferedReader = new BufferedReader(new InputStreamReader(input));
    }

    void receiveWeatherdata() {
        System.out.println("receiveWeatherdata");
        try {
            int counter = 0;
            while (true) {
                String line = bufferedReader.readLine();
                System.out.println(line);
                //TODO: Split Measurements
                //TODO: Process measurements

                if (line.contains("</WEATHERDATA>")){
                    System.out.println("XML finished");
                    counter++;
                    if (counter > 9) {
                        return;
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }
}