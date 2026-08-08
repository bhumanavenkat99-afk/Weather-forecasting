package com.weatherforecast.service;

import com.weatherforecast.model.WeatherData;
import com.weatherforecast.ml.DataPreprocessor;
import com.weatherforecast.ml.RegressionModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegressionService {
    private final RegressionModel regressionModel = new RegressionModel();
    private final DataPreprocessor dataPreprocessor = new DataPreprocessor();

    public void train(List<WeatherData> records) throws Exception {
        if (records == null || records.size() < 2) {
            throw new IllegalArgumentException("At least two weather records are required for regression training");
        }
        List<WeatherData> sorted = records.stream()
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .toList();
        regressionModel.train(dataPreprocessor.buildRegressionDataset(sorted));
    }

    public double predict(double temperature, double humidity, double pressure, double windSpeed,
                          double cloudCover, double precipitation) throws Exception {
        return regressionModel.predict(temperature, humidity, pressure, windSpeed, cloudCover, precipitation);
    }

    public double getMae() {
        return regressionModel.getMae();
    }

    public double getMse() {
        return regressionModel.getMse();
    }

    public double getRmse() {
        return regressionModel.getRmse();
    }

    public double getR2() {
        return regressionModel.getR2();
    }
}
