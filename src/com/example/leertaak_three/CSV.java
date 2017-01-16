package com.example.leertaak_three;

import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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

        try {
            writer = new FileWriter("test.csv", true);
        } catch (IOException e) {
            // This is usefull, but stalls the Thread when it can't open the CSV. Therefore
            // the application can't process anymore data resulting in a backlog
            e.printStackTrace();
        }

        CsvWriter csvwriter = new CsvWriter(writer, new CsvWriterSettings());

        for (Measurement measurement : measurements) {
            csvwriter.writeRow(measurement);
        }

        try {
            assert writer != null;
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
