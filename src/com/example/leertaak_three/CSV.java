package com.example.leertaak_three;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class CSV {

    CSV() {
    }

    void insertMeasurements(ArrayList<Measurement> measurements) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String fileLocation = "";
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("linux")) {
            fileLocation = "/mnt/csv-storage/" + date + ".csv";
        } else {
            fileLocation = date + ".csv";
        }

        try (FileWriter pw = new FileWriter(fileLocation, true)) {
            for (Measurement measurement : measurements) {
                pw.append(measurement.getValuesForCSV()).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
