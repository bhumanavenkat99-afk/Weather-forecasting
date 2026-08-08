package com.weatherforecast.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class WeatherSchedulerService {
    private static final Logger logger = Logger.getLogger(WeatherSchedulerService.class.getName());

    private final WeatherService weatherService;

    private LocalDateTime lastExecutionTime;
    private int totalRuns = 0;
    private String lastStatus = "IDLE";
    private String lastMessage = "Scheduler initialized. Waiting for scheduled cycle or manual trigger.";

    public WeatherSchedulerService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Scheduled(fixedRateString = "${weather.scheduler.rate-ms:3600000}")
    public void scheduledRetrainingTask() {
        logger.info("Starting scheduled background weather model retraining task...");
        runModelRetraining();
    }

    public synchronized Map<String, Object> triggerManualRetraining() {
        logger.info("Manual background retraining task triggered via API...");
        runModelRetraining();
        return getSchedulerStatus();
    }

    private void runModelRetraining() {
        this.lastStatus = "RUNNING";
        try {
            int recordCount = weatherService.getAllWeatherData().size();
            weatherService.trainModels();
            this.lastExecutionTime = LocalDateTime.now();
            this.totalRuns++;
            this.lastStatus = "SUCCESS";
            this.lastMessage = String.format("Retrained Decision Tree & Regression models successfully on %d dataset records.", recordCount);
            logger.info(this.lastMessage);
        } catch (Exception e) {
            this.lastExecutionTime = LocalDateTime.now();
            this.lastStatus = "FAILED";
            this.lastMessage = "Background retraining encountered an error: " + e.getMessage();
            logger.log(Level.SEVERE, this.lastMessage, e);
        }
    }

    public Map<String, Object> getSchedulerStatus() {
        Map<String, Object> statusMap = new HashMap<>();
        statusMap.put("lastExecutionTime", lastExecutionTime != null ? lastExecutionTime.toString() : "N/A");
        statusMap.put("totalRuns", totalRuns);
        statusMap.put("lastStatus", lastStatus);
        statusMap.put("lastMessage", lastMessage);
        statusMap.put("schedulerActive", true);
        return statusMap;
    }
}
