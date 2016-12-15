package com.example.leertaakt_two;

import java.util.Arrays;

/**
 * Created by Robin on 9-12-2016.
 */
public class Measurement {
    private String[] values = new String[19];
    private final String[] index = {"STN", "DATE", "TIME", "TEMP", "DEWP", "STP", "SLP", "VISIB", "WDSP", "PRCP", "SNDP", "CLDC", "WNDDIR", "FRSHTT"};

    Measurement() {
    }

    void addValue(String line) {
        int i;
        for (i = 0; i < index.length; i++)
            if (line.contains(index[i])) break;

        if (i != 13) {
            values[i] = stripTags(line);
        } else {
            String strippedLine = stripTags(line);
            System.out.println(strippedLine);
            values[i] = String.valueOf(strippedLine.charAt(0));
            System.out.println(values[i]);
            values[i+1] = String.valueOf(strippedLine.charAt(1));
            values[i+2] = String.valueOf(strippedLine.charAt(2));
            values[i+3] = String.valueOf(strippedLine.charAt(3));
            values[i+4] = String.valueOf(strippedLine.charAt(4));
            values[i+5] = String.valueOf(strippedLine.charAt(5));
        }
    }

    String getValue(int pos) {
        return values[pos];
    }

    public String toString() {
        return Arrays.toString(values);
    }

    private String stripTags(String input) {
        return input.replaceAll("<.*?>", "");
    }

}
