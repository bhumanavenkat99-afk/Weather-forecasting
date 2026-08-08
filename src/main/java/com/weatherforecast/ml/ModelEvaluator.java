package com.weatherforecast.ml;

public class ModelEvaluator {
    public static double calculateR2(double mse, double variance) {
        if (variance == 0.0) {
            return 0.0;
        }
        return 1.0 - mse / variance;
    }
}
