package com.example.leertaakt_two;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;

class Database {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://controlpanel.bennink.me/leertaak2?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Amsterdam";

    private static final String USER = "leertaak2";
    private static final String PASS = "Wyup&960";

    private Connection con;
    private static final String query = "INSERT INTO measurements ("
            + " stn,"
            + " date,"
            + " time,"
            + " temp,"
            + " dew,"
            + " air_pressure_sea,"
            + " air_pressure_stn,"
            + " visibility,"
            + " wind_speed,"
            + " rain_fall,"
            + " snow_fall,"
            + " cloudly_per,"
            + " wind_dir,"
            + " freeze,"
            + " rain,"
            + " snow,"
            + " hail,"
            + " thunder,"
            + " tornado)"
            + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


    Database() {
//        this.connect();
    }

    private Connection connect() {
        try {
            Class.forName(JDBC_DRIVER);
            this.con = DriverManager.getConnection(DB_URL, USER, PASS);
            if (this.con == null) {
                System.out.println("Connection cannot be established.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    void disconnect() {
        if (this.con != null) {
            try {
                this.con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    void insertMeasurement(Measurement measurement) {

        try {
            this.connect();
            String query = "INSERT INTO measurements ("
                    + " stn,"
                    + " date,"
                    + " time,"
                    + " temp,"
                    + " dew,"
                    + " air_pressure_sea,"
                    + " air_pressure_stn,"
                    + " visibility,"
                    + " wind_speed,"
                    + " rain_fall,"
                    + " snow_fall,"
                    + " cloudly_per,"
                    + " wind_dir,"
                    + " freeze,"
                    + " rain,"
                    + " snow,"
                    + " hail,"
                    + " thunder,"
                    + " tornado)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = this.con.prepareStatement(query);

            statement.setInt(1, Integer.valueOf(measurement.getValue(0)));
            statement.setDate(2, Date.valueOf(measurement.getValue(1)));
            statement.setTime(3, Time.valueOf(measurement.getValue(2)));
            statement.setBigDecimal(4, new BigDecimal(measurement.getValue(3)));
            statement.setBigDecimal(5, new BigDecimal(measurement.getValue(4)));
            statement.setBigDecimal(6, new BigDecimal(measurement.getValue(5)));
            statement.setBigDecimal(7, new BigDecimal(measurement.getValue(6)));
            statement.setBigDecimal(8, new BigDecimal(measurement.getValue(7)));
            statement.setBigDecimal(9, new BigDecimal(measurement.getValue(8)));
            statement.setBigDecimal(10, new BigDecimal(measurement.getValue(9)));
            statement.setBigDecimal(11, new BigDecimal(measurement.getValue(10)));
            statement.setBigDecimal(12, new BigDecimal(measurement.getValue(11)));
            statement.setInt(13, Integer.valueOf(measurement.getValue(12)));
            statement.setInt(14, Integer.valueOf(measurement.getValue(13)));
            statement.setInt(15, Integer.valueOf(measurement.getValue(14)));
            statement.setInt(16, Integer.valueOf(measurement.getValue(15)));
            statement.setInt(17, Integer.valueOf(measurement.getValue(16)));
            statement.setInt(18, Integer.valueOf(measurement.getValue(17)));
            statement.setInt(19, Integer.valueOf(measurement.getValue(18)));
            statement.execute();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            this.disconnect();
        }

    }

    void insertMeasurements(ArrayList<Measurement> measurements) {

        try {
            this.connect();
            PreparedStatement statement = this.con.prepareStatement(query);
            for (Measurement measurement : measurements) {
                statement.setInt(1, Integer.valueOf(measurement.getValue(0)));
                statement.setDate(2, Date.valueOf(measurement.getValue(1)));
                statement.setTime(3, Time.valueOf(measurement.getValue(2)));
                statement.setBigDecimal(4, new BigDecimal(measurement.getValue(3)));
                statement.setBigDecimal(5, new BigDecimal(measurement.getValue(4)));
                statement.setBigDecimal(6, new BigDecimal(measurement.getValue(5)));
                statement.setBigDecimal(7, new BigDecimal(measurement.getValue(6)));
                statement.setBigDecimal(8, new BigDecimal(measurement.getValue(7)));
                statement.setBigDecimal(9, new BigDecimal(measurement.getValue(8)));
                statement.setBigDecimal(10, new BigDecimal(measurement.getValue(9)));
                statement.setBigDecimal(11, new BigDecimal(measurement.getValue(10)));
                statement.setBigDecimal(12, new BigDecimal(measurement.getValue(11)));
                statement.setInt(13, Integer.valueOf(measurement.getValue(12)));
                statement.setInt(14, Integer.valueOf(measurement.getValue(13)));
                statement.setInt(15, Integer.valueOf(measurement.getValue(14)));
                statement.setInt(16, Integer.valueOf(measurement.getValue(15)));
                statement.setInt(17, Integer.valueOf(measurement.getValue(16)));
                statement.setInt(18, Integer.valueOf(measurement.getValue(17)));
                statement.setInt(19, Integer.valueOf(measurement.getValue(18)));
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            this.disconnect();
        }

    }
}
