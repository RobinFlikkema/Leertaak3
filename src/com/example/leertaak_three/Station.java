package com.example.leertaak_three;

import java.util.ArrayList;

/**
 * For every weatherstation, a Station object is created. This object stores the last 30 measurements of that station.
 *
 * @author Robin Flikkema
 *         TODO: We could probably make some function static as they're the same for every instance (might result in less memory usage)
 */
class Station {
    // Used to store the latest Measurements
    private ArrayList<Measurement> measurements = new ArrayList<>();
    // Used to store the Extrapolated Temperature
    private double extrapolatedTemperature;

    Station() {

    }

    /**
     * This function checks if the List has more than 30 values, if not it will add the new Measurement. If it has more
     * than 30 values it will remove the latest and add the new value.
     *
     * @param measurement, the Measurement which should be added.
     */
    void addMeasurement(Measurement measurement) {
        if (this.measurements.size() >= 30) {
            this.measurements.remove(0);
        }
        this.measurements.add(measurement);
    }

    /**
     * This function calculates the average Temperature by taking the average of the latest 30 measurements.
     * If there are less then 2 Measurements it will insert 0.0
     */
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

    /**
     * @return the Extrapolated temperature, set by the calculateExtrapolatedTemperature() function
     */
    double getExtrapolatedTemperature() {
        return this.extrapolatedTemperature;
    }

    /**
     * This function does exactly the same as the calculateExtrapolatedTemperate, except it does return the extrapolated
     * value, and it can extrapolate almost all values of the measurement.
     *
     * @param pos, the position of the value which needs to be extrapolated
     *
     * @return the extrapolated value
     */
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

    /**
     * This function determines if the temperature is plausible. If there temperature is close to zero it wont calculate
     * anything since 20% of 0 is nothing.
     *
     * @param temperature, the temperature which needs to be checked
     *
     * @return True when the value is plausible, False when it's not.
     */
    boolean isTemperaturePlausible(double temperature) {
        if (this.measurements.size() > 1 && (temperature < -5 || temperature > 5)) {
            calculateExtrapolatedTemperature();
            return ((getExtrapolatedTemperature() * 1.20) > temperature) && (temperature < (getExtrapolatedTemperature() * 0.80));
        } else {
            return true;
        }
    }
}
