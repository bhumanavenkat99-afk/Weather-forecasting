package com.weatherforecast.controller;

import com.weatherforecast.service.WeatherSchedulerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/scheduler")
public class SchedulerController {
    private final WeatherSchedulerService weatherSchedulerService;

    public SchedulerController(WeatherSchedulerService weatherSchedulerService) {
        this.weatherSchedulerService = weatherSchedulerService;
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSchedulerStatus(Authentication authentication) {
        return ResponseEntity.ok(weatherSchedulerService.getSchedulerStatus());
    }

    @PostMapping("/trigger")
    public ResponseEntity<Map<String, Object>> triggerRetraining(Authentication authentication) {
        Map<String, Object> result = weatherSchedulerService.triggerManualRetraining();
        return ResponseEntity.ok(result);
    }
}
