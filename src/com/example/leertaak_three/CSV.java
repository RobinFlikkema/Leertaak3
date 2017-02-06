package com.example.leertaak_three;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class can be used to store Measurements into a CSV. We chose to make this a seperate class so that it can be
 * easily exchanged with something else like MySQL for example.
 *
 * @author Robin Flikkema
 */
class CSV {

    CSV() {
    }

    /**
     * This function inserts Measurements. Since the machines we use to develop this application run Windows and the
     * production server runs Linux we make a simple check to make sure the files got stored in the right place while
     * developing.
     * The name of the CSV will be yyyy-MM-dd, representing the current date
     *
     * @param measurements , A List of Measurements which need to be inserted into the CSV
     */
    void insertMeasurements(ArrayList<Measurement> measurements) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String fileLocation;
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("linux")) {
            fileLocation = "/mnt/csv-storage/" + date + ".csv";
        } else {
            fileLocation = date + ".csv";
        }

        try (FileWriter pw = new FileWriter(fileLocation, true)) {
            for (Measurement measurement : measurements) {
                pw.append(measurement.getCSVRowAsString()).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
