package com.example.leertaak_three;

import java.util.ArrayList;

/**
 * Created by Robin on 21-12-2016.
 */
class Station {
    private ArrayList<Measurement> measurements = new ArrayList<>();

    Station() {

    }

    void addMeasurement(Measurement measurement) {
        if (measurements.size() >= 30) {
            measurements.remove(0);
        }
        measurements.add(measurement);
    }

    double getExtrapolatedTemperature() {
        if (measurements.size() > 1) {
            double slope = (measurements.get(0).getTemperature() - measurements.get(measurements.size() - 1).getTemperature()) / (0 - (measurements.size() - 1));
            return Math.round(measurements.get(measurements.size() - 1).getTemperature() + slope * 10) / 10;
        }
        return 0.0;
    }

    double getExtrapolatedValue(int pos) {
        if (measurements.size() > 1) {
            double slope = (measurements.get(0).getValueAsDouble(pos) - measurements.get(measurements.size() - 1).getValueAsDouble(pos)) / (0 - (measurements.size() - 1));
            if (slope > 0) {
                return Math.round(measurements.get(measurements.size() - 1).getValueAsDouble(pos) + slope * 100) / 100;
            }
        }
        return 0.0;
    }

    boolean isTemperaturePlausible(double temperature) {
        return (getExtrapolatedTemperature() * 1.20) < temperature && temperature > (getExtrapolatedTemperature() * 0.80);
    }
}
