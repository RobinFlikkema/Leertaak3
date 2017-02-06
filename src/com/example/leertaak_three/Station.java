package com.example.leertaak_three;

import java.util.ArrayList;

/**
 * Created by Robin on 21-12-2016.
 */
class Station {
    private ArrayList<Measurement> measurements = new ArrayList<>();
    private double extrapolatedTemperature;

    Station() {

    }

    void addMeasurement(Measurement measurement) {
        if (this.measurements.size() >= 30) {
            this.measurements.remove(0);
        }
        this.measurements.add(measurement);
    }

    private void calculateExtrapolatedTemperature() {
        if (this.measurements.size() > 1) {
            double totalTemperature = 0.0;
            for (Measurement measurement : this.measurements) {
                totalTemperature += measurement.getTemperature();
            }
            double averageTemperature = totalTemperature / this.measurements.size();
            this.extrapolatedTemperature = Math.round(averageTemperature);
        } else {
            this.extrapolatedTemperature = 0.0;
        }
    }

    double getExtrapolatedTemperature() {
        return this.extrapolatedTemperature;
    }

    double getExtrapolatedValue(int pos) {
        if (this.measurements.size() > 1) {
            double total = 0.0;
            for (Measurement measurement : this.measurements) {
                total += measurement.getValueAsDouble(pos);
            }
            double average = total / this.measurements.size();
            return Math.round(average);
        }
        return 0.0;
    }

    boolean isTemperaturePlausible(double temperature) {
        if (this.measurements.size() > 1 && (temperature < -5 || temperature > 5)) {
            calculateExtrapolatedTemperature();
            return ((getExtrapolatedTemperature() * 1.20) > temperature) && (temperature < (getExtrapolatedTemperature() * 0.80));
        } else {
            return true;
        }
    }
}
