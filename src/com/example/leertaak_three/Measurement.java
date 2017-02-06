package com.example.leertaak_three;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Robin
 */
class Measurement {
    private final String[] values = new String[19];
    private int lineCounter = 0;
    private final static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Measurement() {
    }

    /**
     * This function adds lines to the Measurement.
     * @param line , the line to be added
     */
    void addValue(String line) {
        line = stripTags(line, this.lineCounter);

        if (line.isEmpty()) {
            line = "000000";
        }

        this.values[this.lineCounter] = line;
        this.lineCounter++;
    }

    /**
     * @return the station number of the Measurement
     */
    int getStationNumber() {
        return Integer.valueOf(this.getValue(0));
    }

    /**
     * @return the Temperate of the Measurement
     */
    double getTemperature() {
        return Double.valueOf(this.getValue(3));
    }

    /**
     * @param pos the position of the value to be returned
     *
     * @return the value on the given position
     */
    private String getValue(int pos) {
        return this.values[pos];
    }

    /**
     * getCSVRowAsString
     * This function formats the values from the values-array to a string. The values  are comma-separated and ready to
     * be appended to a CSV
     * The Date and Time (value[1] and value[2] are converted to a UNIX Timestamp, this is because it used less
     * characters than the full date + time.
     * "FRSHTT" is separated into 6 values. Although this takes more space (because of the comma separation), this makes
     * it more readable IMO
     *
     * @return a string which contains all values, comma-separated
     */
    String getCSVRowAsString() {
        // We'll need to initialize this String, because if the parsing of the Date fails, the String isn't initialized.
        String timestamp = null;
        try {
            // This is used to generate a UNIX Timestamp from the Date and Time, provided by the weatherstations.
            // We use / 1000 because date.getTime() returns milliseconds, and we need seconds. The + 3600 is used because
            // the weatherstations are 1 hour off.
            Date date = format.parse(values[1] + " " + values[2]);
            long timestampAsLong = date.getTime() / 1000 + 3600;
            timestamp = String.valueOf(timestampAsLong);
        } catch (ParseException e) {
            // Well, theoretically this shouldn't be needed but you never know what the weatherstations are doing ;)
            e.printStackTrace();
        }

        // This just returns all the different values. The CharAt() is used to split the FRSHTT into 6 values
        return values[0] + "," +
                (timestamp + ",") +
                (values[3] + ",") +
                (values[4] + ",") +
                (values[5] + ",") +
                (values[6] + ",") +
                (values[7] + ",") +
                (values[8] + ",") +
                (values[9] + ",") +
                (values[10] + ",") +
                (values[12] + ",") +
                (values[13] + ",") +
                (values[11].charAt(0) + ",") +
                (values[11].charAt(1) + ",") +
                (values[11].charAt(2) + ",") +
                (values[11].charAt(3) + ",") +
                (values[11].charAt(4) + ",") +
                values[11].charAt(5);
    }

    /**
     * @param pos the position of the value to be returned
     *
     * @return the value on the given position
     */
    double getValueAsDouble(int pos) {
        return Double.valueOf(this.values[pos]);
    }

    /**
     * @param pos the position of the value to be set
     * @param value the value, that needs to be set
     */
    void setValue(int pos, String value) {
        values[pos] = value;
    }

    public String toString() {
        return Arrays.toString(values);
    }

    /**
     * This functions strips the tags of a String
     *
     * @param input the line containing XML Tags
     * @param position the rowcount of the line
     *
     * @return , stripped line
     */
    private String stripTags(String input, int position) {
        String output = null;
        switch (position) {
            case 0:
            case 5:
            case 6:
                output = input.substring(0, input.length() - 6).substring(7);
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 8:
            case 9:
            case 10:
            case 12:
                output = input.substring(0, input.length() - 7).substring(8);
                break;
            case 7:
                output = input.substring(0, input.length() - 8).substring(9);
                break;
            case 11:
            case 13:
                output = input.substring(0, input.length() - 9).substring(10);
                break;
        }
        return output;
    }

    /**
     * @return the position of the missing Value, or -1 when no value is missing.
     */
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
