package com.example.leertaak_three;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by Robin on 9-12-2016.
 */
class Measurement {
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

    String getValue(int pos) {
        return this.values[pos];
    }

    String[] getValues(){
        return this.values;
    }

    double getValueAsDouble(int pos) {
        return Double.valueOf(this.values[pos]);
    }

    public void setValue(int pos, String value){
        values[pos] = value;
    }

    public String toString() {
        return Arrays.toString(values);
    }

    private String stripTags(String input) {
        return input.replaceAll("<.*?>", "");
    }

    public int valueIsMissing(){
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
