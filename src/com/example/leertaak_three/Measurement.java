package com.example.leertaak_three;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class Measurement {
    private String[] values = new String[19];
    private static final String[] index = {"STN", "DATE", "TIME", "TEMP", "DEWP", "STP", "SLP", "VISIB", "WDSP", "PRCP", "SNDP", "CLDC", "WNDDIR", "FRSHTT"};

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
            for (int j = 0; j < 6; j++ ){
                values[i+j] = String.valueOf(strippedLine.charAt(j));
            }
        }
    }

    int getStationNumber(){
        return Integer.valueOf(this.getValue(0));
    }

    double getTemperature(){
        return Double.valueOf(this.getValue(3));
    }

    private String getValue(int pos) {
        return this.values[pos];
    }

    //TODO: We can probbly refactor this
    String newGetValuesForCSV(){
        String timestamp = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date date = format.parse(values[1] + " " + values[2]);
            long timestampAsLong = date.getTime() / 1000;
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
        returnString += values[11] + ",";
        returnString += values[12] + ",";
        returnString += values[13] + ",";
        returnString += values[14] + ",";
        returnString += values[15] + ",";
        returnString += values[16] + ",";
        returnString += values[17] + ",";
        returnString += values[18];

        return returnString;
    }

    double getValueAsDouble(int pos) {
        return Double.valueOf(this.values[pos]);
    }

    void setValue(int pos, String value){
        values[pos] = value;
    }

    public String toString() {
        return Arrays.toString(values);
    }

    private String stripTags(String input) {
        return input.replaceAll("<.*?>", "");
    }

    int valueIsMissing(){
        int index = 0;
        for (String value : values) {
            if (Objects.equals(value, "")){
                return index;
            }
            index++;
        }
        return -1;
    }
}
