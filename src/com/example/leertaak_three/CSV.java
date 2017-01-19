package com.example.leertaak_three;

import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class CSV {
    /**
     * TODO: Refactoring
     * TODO: Tweak CsvWriterSettings
     *
     */

    CSV() {
    }

    void insertMeasurements(ArrayList<Measurement> measurements) {
        FileWriter writer = null;
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        try {
            writer = new FileWriter(date + ".csv", true);
        } catch (IOException e) {
            // TODO:
            // This is useful, but stalls the Thread when it can't open the CSV. Therefore
            // the application can't process anymore data resulting in a backlog
            e.printStackTrace();
            PrintWriter pw = null;
            try {
                pw = new PrintWriter("log.txt");
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            pw.println(e.toString());
        }

        CsvWriter csvwriter = new CsvWriter(writer, new CsvWriterSettings());

        for (Measurement measurement : measurements) {
            csvwriter.writeRow(measurement.getValues());
        }

        try {
            assert writer != null;
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
