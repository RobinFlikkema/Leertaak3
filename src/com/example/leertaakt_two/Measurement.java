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

    String getValue(int pos) {
        return values[pos];
    }

    public String toString() {
        return Arrays.toString(values);
    }

    private String stripTags(String input){
        return input.replaceAll("<.*?>", "");
    }

    private String[] processValues(String[] input){
        String[] output = new String[19];
        // Copy values into new Array for the time being
        // TODO: Process values by looking at previous data
        System.arraycopy(input, 0, output, 0, input.length);

    return output;
    }

}
