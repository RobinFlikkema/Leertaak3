package com.example.leertaakt_two;

import java.util.Arrays;

/**
 * Created by Robin on 9-12-2016.
 */
public class Measurement {
    String[] input = new String[14];
    private String[] values = new String[19];
    private final String[] index = {"STN", "DATE", "TIME", "TEMP", "DEWP", "STP", "SLP", "VISIB", "WDSP", "PRCP", "SNDP", "CLDC", "WNDDIR", "FRSHTT"};
    private boolean processed = false;

    Measurement() {
    }

    void addValue(String line) {
        int i;
        for (i = 0; i < index.length; i++)
            if (line.contains(index[i])) break;

        input[i] = stripTags(line);
    }

    String getValue(int pos) {
        if (this.processed = false) {
            processValues(this.input);
        }
        return values[pos];
    }

    public String toString() {
        return Arrays.toString(input);
    }

    private String stripTags(String input) {
        return input.replaceAll("<.*?>", "");
    }

    boolean processValues(String[] input) {
        String[] output = new String[19];
        // STN
        output[0] = input[0];
        // DATE
        output[1] = input[1];
        // TIME
        output[2] = input[2];
        // TEMP
        output[3] = input[3];
        // DEWP
        output[4] = input[4];
        // STP
        output[5] = input[5];
        // SLP
        output[6] = input[6];
        // VISIB
        output[7] = input[7];
        // WDSP
        output[8] = input[8];
        // PRCP
        output[9] = input[9];
        // SNDP
        output[10] = input[10];
        // CLDC
        output[11] = input[11];
        // WINDDIR
        output[12] = input[12];
        // FRSHTT: F
        output[13] = String.valueOf(input[13].charAt(0));
        // FRSHTT: R
        output[14] = String.valueOf(input[13].charAt(1));
        // FRSHTT: S
        output[15] = String.valueOf(input[13].charAt(2));
        // FRSHTT: H
        output[16] = String.valueOf(input[13].charAt(3));
        // FRSHTT: T
        output[17] = String.valueOf(input[13].charAt(4));
        // FRSHTT: T
        output[18] = String.valueOf(input[13].charAt(5));
        this.processed = true;
        this.values = output;
        return true;
    }

}
