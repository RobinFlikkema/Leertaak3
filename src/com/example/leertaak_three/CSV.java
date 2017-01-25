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

        try (FileWriter pw = new FileWriter(date + ".csv", true)){
            for (Measurement measurement : measurements){
                pw.append(measurement.newGetValuesForCSV()).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
