package com.example.leertaak_three;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 *
 * @author Robin
 */
class Measurement {
    private final String[] values = new String[19];
    private int lineCounter = 0;
    private final static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


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

    /**
     * getCSVRowAsString
     * This function formats the values from the values-array to a string. The values  are comma-seperated and ready to
     * be appended to a CSV
     * The Date and Time (value[1] and value[2] are converted to a UNIX Timestamp, this is because it used less
     * characters than the full date + time.
     * FRSHTT is serpated into 6 values. Although this takes more space (because of the comma separation), this makes
     * it more readable IMO
     *
     * @return a string which contains all values, commaseperated
     */
    String getCSVRowAsString() {
        // We'll need to initialize this String, because if the parsing of the Date fails, the String isn't initialized.
        String timestamp = null;
        try {
            // This is used to generate a UNIX Timestamp from the Date and Time, provided by the weatherstations.
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

    double getValueAsDouble(int pos) {
        return Double.valueOf(this.values[pos]);
    }

    void setValue(int pos, String value) {
        values[pos] = value;
    }

    // TODO: Are we even using this?
    public String toString() {
        return Arrays.toString(values);
    }

    // TODO: This is bad! Use substring!
    // TODO: If we aren't going to use SubString, We need to use a Matcher with Pattern!
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
