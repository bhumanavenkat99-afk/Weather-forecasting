package com.weatherforecast.controller;

import com.weatherforecast.model.WeatherData;
import com.weatherforecast.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping("/upload")
    public ResponseEntity<List<WeatherData>> uploadWeatherCsv(@RequestParam("file") MultipartFile file) throws Exception {
        List<WeatherData> savedRecords = weatherService.uploadWeatherCsv(file);
        return ResponseEntity.ok(savedRecords);
    }

    @GetMapping("/history")
    public ResponseEntity<List<WeatherData>> getWeatherHistory() {
        return ResponseEntity.ok(weatherService.getAllWeatherData());
    }
}
