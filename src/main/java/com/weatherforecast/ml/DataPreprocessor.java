package com.weatherforecast.ml;

import com.weatherforecast.model.WeatherData;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;

public class DataPreprocessor {
    public Instances buildClassificationDataset(List<WeatherData> records) {
        ArrayList<Attribute> attributes = getFeatureAttributes();
        Attribute classAttribute = new Attribute("rainLabel", List.of("No Rain", "Rain"));
        attributes.add(classAttribute);
        Instances dataset = new Instances("WeatherClassification", attributes, records.size());
        dataset.setClassIndex(attributes.size() - 1);

        for (WeatherData record : records) {
            double[] values = new double[attributes.size()];
            values[0] = record.getTemperature();
            values[1] = record.getHumidity();
            values[2] = record.getPressure();
            values[3] = record.getWindSpeed();
            values[4] = record.getCloudCover();
            values[5] = record.getPrecipitation();
            values[6] = classAttribute.indexOfValue(mapConditionToLabel(record.getWeatherCondition()));
            dataset.add(new DenseInstance(1.0, values));
        }
        return dataset;
    }

    public Instances buildRegressionDataset(List<WeatherData> records) {
        ArrayList<Attribute> attributes = getFeatureAttributes();
        Attribute targetAttribute = new Attribute("nextTemperature");
        attributes.add(targetAttribute);
        Instances dataset = new Instances("WeatherRegression", attributes, records.size() - 1);
        dataset.setClassIndex(attributes.size() - 1);

        for (int i = 0; i < records.size() - 1; i++) {
            WeatherData current = records.get(i);
            WeatherData nextDay = records.get(i + 1);
            if (nextDay == null) {
                continue;
            }
            double[] values = new double[attributes.size()];
            values[0] = current.getTemperature();
            values[1] = current.getHumidity();
            values[2] = current.getPressure();
            values[3] = current.getWindSpeed();
            values[4] = current.getCloudCover();
            values[5] = current.getPrecipitation();
            values[6] = nextDay.getTemperature();
            dataset.add(new DenseInstance(1.0, values));
        }
        return dataset;
    }

    public String mapConditionToLabel(String condition) {
        if (condition == null) {
            return "No Rain";
        }
        String normalized = condition.toLowerCase();
        if (normalized.contains("rain") || normalized.contains("storm") || normalized.contains("shower") || normalized.contains("drizzle")) {
            return "Rain";
        }
        return "No Rain";
    }

    private ArrayList<Attribute> getFeatureAttributes() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("temperature"));
        attributes.add(new Attribute("humidity"));
        attributes.add(new Attribute("pressure"));
        attributes.add(new Attribute("windSpeed"));
        attributes.add(new Attribute("cloudCover"));
        attributes.add(new Attribute("precipitation"));
        return attributes;
    }
}
