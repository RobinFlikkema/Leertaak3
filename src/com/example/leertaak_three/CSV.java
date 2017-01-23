package com.example.leertaak_three;

import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class CSV {

    CSV() {
    }

    void insertMeasurements(ArrayList<Measurement> measurements) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        try (FileWriter writer = new FileWriter(date + ".csv", true)) {

            CsvWriter csvwriter = new CsvWriter(writer, new CsvWriterSettings());
            for (Measurement measurement : measurements) {
                csvwriter.writeRow(measurement.getValuesForCSV());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
