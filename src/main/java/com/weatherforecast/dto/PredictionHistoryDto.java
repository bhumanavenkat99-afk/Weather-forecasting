package com.weatherforecast.dto;

import java.time.LocalDateTime;

public class PredictionHistoryDto {
    private Long id;
    private String userEmail;
    private LocalDateTime predictionDate;
    private Double temperature;
    private Double humidity;
    private Double pressure;
    private Double windSpeed;
    private Double cloudCover;
    private Double precipitation;
    private String decisionTreePrediction;
    private Double regressionPrediction;

    public PredictionHistoryDto() {
    }

    public PredictionHistoryDto(Long id, String userEmail, LocalDateTime predictionDate, Double temperature,
                                Double humidity, Double pressure, Double windSpeed, Double cloudCover,
                                Double precipitation, String decisionTreePrediction, Double regressionPrediction) {
        this.id = id;
        this.userEmail = userEmail;
        this.predictionDate = predictionDate;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.cloudCover = cloudCover;
        this.precipitation = precipitation;
        this.decisionTreePrediction = decisionTreePrediction;
        this.regressionPrediction = regressionPrediction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDateTime getPredictionDate() {
        return predictionDate;
    }

    public void setPredictionDate(LocalDateTime predictionDate) {
        this.predictionDate = predictionDate;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Double getCloudCover() {
        return cloudCover;
    }

    public void setCloudCover(Double cloudCover) {
        this.cloudCover = cloudCover;
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Double precipitation) {
        this.precipitation = precipitation;
    }

    public String getDecisionTreePrediction() {
        return decisionTreePrediction;
    }

    public void setDecisionTreePrediction(String decisionTreePrediction) {
        this.decisionTreePrediction = decisionTreePrediction;
    }

    public Double getRegressionPrediction() {
        return regressionPrediction;
    }

    public void setRegressionPrediction(Double regressionPrediction) {
        this.regressionPrediction = regressionPrediction;
    }
}
