package com.weatherforecast.service;

import com.weatherforecast.model.WeatherData;
import com.weatherforecast.repository.WeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WeatherServiceTest {
    private WeatherRepository weatherRepository;
    private DecisionTreeService decisionTreeService;
    private RegressionService regressionService;
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        weatherRepository = Mockito.mock(WeatherRepository.class);
        decisionTreeService = Mockito.mock(DecisionTreeService.class);
        regressionService = Mockito.mock(RegressionService.class);
        weatherService = new WeatherService(weatherRepository, decisionTreeService, regressionService);
    }

    @Test
    void uploadWeatherCsvStoresRecords() throws Exception {
        String csv = "date,temperature,humidity,pressure,windSpeed,cloudCover,precipitation,weatherCondition\n"
                + "2024-01-01,15.0,70.0,1012.0,10.0,50.0,0.0,Clear\n";
        MultipartFile file = new MockMultipartFile("file", "weather.csv", "text/csv", csv.getBytes());
        when(weatherRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(weatherRepository.findAll()).thenReturn(new ArrayList<>());

        List<WeatherData> result = weatherService.uploadWeatherCsv(file);

        assertEquals(1, result.size());
        assertEquals(LocalDate.of(2024, 1, 1), result.get(0).getDate());
        verify(weatherRepository).saveAll(any());
    }

    @Test
    void getAllWeatherDataReturnsRepositoryData() {
        List<WeatherData> records = new ArrayList<>();
        records.add(new WeatherData(LocalDate.now(), 20.0, 65.0, 1010.0, 5.0, 20.0, 0.0, "Clear"));
        when(weatherRepository.findAll()).thenReturn(records);

        assertEquals(1, weatherService.getAllWeatherData().size());
    }
}
