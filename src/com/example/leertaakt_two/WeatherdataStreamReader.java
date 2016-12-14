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

    Boolean receiveWeatherdata() {
        System.out.println("receiveWeatherdata");
        try {
            int counter = 0;
            Weatherdata weatherdata = new Weatherdata();
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    System.out.println(line);
                    weatherdata.addLine(line);

                    if (line.contains("</WEATHERDATA>")) {
                        weatherdata.printWeatherdata();
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