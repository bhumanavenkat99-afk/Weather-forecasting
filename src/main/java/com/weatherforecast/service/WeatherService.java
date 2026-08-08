package com.weatherforecast.service;

import com.weatherforecast.model.WeatherData;
import com.weatherforecast.repository.WeatherRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {
    private final WeatherRepository weatherRepository;
    private final DecisionTreeService decisionTreeService;
    private final RegressionService regressionService;

    public WeatherService(WeatherRepository weatherRepository,
                          DecisionTreeService decisionTreeService,
                          RegressionService regressionService) {
        this.weatherRepository = weatherRepository;
        this.decisionTreeService = decisionTreeService;
        this.regressionService = regressionService;
    }

    public List<WeatherData> uploadWeatherCsv(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("CSV file cannot be empty");
        }

        List<WeatherData> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String header = reader.readLine();
            if (header == null || !header.toLowerCase().contains("date")) {
                throw new IllegalArgumentException("CSV header is invalid or missing the date column");
            }
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] tokens = line.split(",");
                if (tokens.length < 8) {
                    continue;
                }
                try {
                    LocalDate date = LocalDate.parse(tokens[0].trim());
                    Double temperature = Double.parseDouble(tokens[1].trim());
                    Double humidity = Double.parseDouble(tokens[2].trim());
                    Double pressure = Double.parseDouble(tokens[3].trim());
                    Double windSpeed = Double.parseDouble(tokens[4].trim());
                    Double cloudCover = Double.parseDouble(tokens[5].trim());
                    Double precipitation = Double.parseDouble(tokens[6].trim());
                    String weatherCondition = tokens[7].trim();
                    WeatherData record = new WeatherData(date, temperature, humidity, pressure,
                            windSpeed, cloudCover, precipitation, weatherCondition);
                    records.add(record);
                } catch (Exception ignored) {
                    // Skip invalid rows and continue processing
                }
            }
        }

        if (records.isEmpty()) {
            throw new IllegalArgumentException("No valid weather records were found in the uploaded CSV file");
        }

        weatherRepository.saveAll(records);
        trainModels();
        return records;
    }

    public List<WeatherData> getAllWeatherData() {
        return weatherRepository.findAll();
    }

    public void trainModels() throws Exception {
        List<WeatherData> allRecords = weatherRepository.findAll();
        if (!allRecords.isEmpty()) {
            decisionTreeService.train(allRecords);
            regressionService.train(allRecords);
        }
    }
}
