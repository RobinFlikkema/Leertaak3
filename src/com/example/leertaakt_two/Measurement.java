package com.example.leertaakt_two;

import java.util.Arrays;

/**
 * Created by Robin on 9-12-2016.
 */
public class Measurement {
    private String[] values = new String[14];
    private final String[] index = {"STN", "DATE", "TIME", "TEMP", "DEWP", "STP", "SLP", "VISIB", "WDSP", "PRCP", "SNDP", "CLDC", "WNDDIR", "FRSHTT"};

    Measurement() {
    }

    void addValue(String line) {
        int i;
        for (i = 0; i < index.length; i++)
            if (line.contains(index[i])) break;

        values[i] = stripTags(line);
    }

    public String toString() {
        return Arrays.toString(values);
    }

    private String stripTags(String input){
        return input.replaceAll("<.*?>", "");
    }

}
