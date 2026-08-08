package com.weatherforecast.service;

import com.weatherforecast.model.WeatherData;
import com.weatherforecast.ml.DataPreprocessor;
import com.weatherforecast.ml.DecisionTreeModel;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DecisionTreeService {
    private final DecisionTreeModel decisionTreeModel = new DecisionTreeModel();
    private final DataPreprocessor dataPreprocessor = new DataPreprocessor();

    public void train(List<WeatherData> records) throws Exception {
        if (records == null || records.isEmpty()) {
            throw new IllegalArgumentException("Weather record list cannot be empty for decision tree training");
        }
        List<WeatherData> sorted = records.stream()
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .toList();
        decisionTreeModel.train(dataPreprocessor.buildClassificationDataset(sorted));
    }

    public String predict(double temperature, double humidity, double pressure, double windSpeed,
                          double cloudCover, double precipitation) throws Exception {
        return decisionTreeModel.predict(temperature, humidity, pressure, windSpeed, cloudCover, precipitation);
    }

    public double getAccuracy() {
        return decisionTreeModel.getAccuracy();
    }

    public double getPrecision() {
        return decisionTreeModel.getPrecision();
    }

    public double getRecall() {
        return decisionTreeModel.getRecall();
    }

    public double getF1Score() {
        return decisionTreeModel.getF1Score();
    }
}
