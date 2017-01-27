package com.example.leertaak_three;

import com.sun.xml.internal.bind.v2.TODO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class Measurement {
    private final String[] values = new String[19];
    private int lineCounter = 0;

    Measurement() {
    }

    // TODO: We can probably refactor this to be in a seperate Parser thingy
    void addValue(String line) {
        line = stripTags(line);

        if (line.isEmpty()) {
            line = "000000";
        }

        this.values[this.lineCounter] = line;
        this.lineCounter++;
    }

    int getStationNumber() {
        return Integer.valueOf(this.getValue(0));
    }

    double getTemperature() {
        return Double.valueOf(this.getValue(3));
    }

    private String getValue(int pos) {
        return this.values[pos];
    }

    String getValuesForCSV() {
        String timestamp = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(values[1] + " " + values[2]);
            long timestampAsLong = date.getTime() / 1000 + 3600;
            timestamp = String.valueOf(timestampAsLong);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String returnString = values[0] + ",";
        returnString += timestamp + ",";
        returnString += values[3] + ",";
        returnString += values[4] + ",";
        returnString += values[5] + ",";
        returnString += values[6] + ",";
        returnString += values[7] + ",";
        returnString += values[8] + ",";
        returnString += values[9] + ",";
        returnString += values[10] + ",";
        returnString += values[12] + ",";
        returnString += values[13] + ",";
        returnString += values[11].charAt(0) + ",";
        returnString += values[11].charAt(1) + ",";
        returnString += values[11].charAt(2) + ",";
        returnString += values[11].charAt(3) + ",";
        returnString += values[11].charAt(4) + ",";
        returnString += values[11].charAt(5);

        return returnString;
    }

    double getValueAsDouble(int pos) {
        return Double.valueOf(this.values[pos]);
    }

    void setValue(int pos, String value) {
        values[pos] = value;
    }

    public String toString() {
        return Arrays.toString(values);
    }

    // TODO: This is bad! Use substring!
    private String stripTags(String input) {
        return input.replaceAll("<.*?>", "").trim();
    }

    int valueIsMissing() {
        int index = 0;
        for (String value : values) {
            if (Objects.equals(value, "")) {
                return index;
            }
            index++;
        }
        return -1;
    }
}
