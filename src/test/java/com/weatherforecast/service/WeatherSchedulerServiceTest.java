package com.weatherforecast.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WeatherSchedulerServiceTest {
    private WeatherService weatherService;
    private WeatherSchedulerService weatherSchedulerService;

    @BeforeEach
    void setUp() {
        weatherService = Mockito.mock(WeatherService.class);
        weatherSchedulerService = new WeatherSchedulerService(weatherService);
    }

    @Test
    void scheduledRetrainingTaskRunsSuccessfully() throws Exception {
        when(weatherService.getAllWeatherData()).thenReturn(Collections.emptyList());

        weatherSchedulerService.scheduledRetrainingTask();

        verify(weatherService).trainModels();
        Map<String, Object> status = weatherSchedulerService.getSchedulerStatus();
        assertEquals("SUCCESS", status.get("lastStatus"));
        assertEquals(1, status.get("totalRuns"));
    }

    @Test
    void triggerManualRetrainingRunsSuccessfully() throws Exception {
        when(weatherService.getAllWeatherData()).thenReturn(Collections.emptyList());

        Map<String, Object> status = weatherSchedulerService.triggerManualRetraining();

        verify(weatherService).trainModels();
        assertEquals("SUCCESS", status.get("lastStatus"));
        assertEquals(1, status.get("totalRuns"));
    }
}
