package com.example.leertaak_three;

import java.util.ArrayList;

/**
 * Created by Robin on 21-12-2016.
 */
class Station {
    private final ArrayList<Measurement> measurements = new ArrayList<>();
    private double extrapolatedTemperature;

    Station() {

    }

    void addMeasurement(Measurement measurement) {
        if (measurements.size() >= 30) {
            measurements.remove(0);
        }
        measurements.add(measurement);
    }

    private void calculateExtrapolatedTemperature() {
        if (measurements.size() > 1) {
            double totalTemperature = 0.0;
            for (Measurement measurement : measurements) {
                totalTemperature += measurement.getTemperature();
            }
            double averageTemperature = totalTemperature / measurements.size();
            this.extrapolatedTemperature = Math.round(averageTemperature);
        }
        this.extrapolatedTemperature = 0.0;
    }

    double getExtrapolatedTemperature() {
        return this.extrapolatedTemperature;
    }

    double getExtrapolatedValue(int pos) {
            if (measurements.size() > 1) {
                double total = 0.0;
                for (Measurement measurement : measurements) {
                    total += measurement.getValueAsDouble(pos);
                }
                double average = total / measurements.size();
                return Math.round(average);
            }
        return 0.0;
    }

    boolean isTemperaturePlausible(double temperature) {
        calculateExtrapolatedTemperature();
        return (getExtrapolatedTemperature() * 1.20) < temperature && temperature > (getExtrapolatedTemperature() * 0.80);
    }
}
