package com.weatherforecast.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prediction_history")
public class PredictionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "prediction_date", nullable = false)
    private LocalDateTime predictionDate;

    @Column(nullable = false)
    private Double temperature;

    @Column(nullable = false)
    private Double humidity;

    @Column(nullable = false)
    private Double pressure;

    @Column(name = "wind_speed", nullable = false)
    private Double windSpeed;

    @Column(name = "cloud_cover", nullable = false)
    private Double cloudCover;

    @Column(nullable = false)
    private Double precipitation;

    @Column(name = "decision_tree_prediction", nullable = false)
    private String decisionTreePrediction;

    @Column(name = "regression_prediction", nullable = false)
    private Double regressionPrediction;

    public PredictionHistory() {
    }

    public PredictionHistory(User user, LocalDateTime predictionDate, Double temperature, Double humidity,
                             Double pressure, Double windSpeed, Double cloudCover, Double precipitation,
                             String decisionTreePrediction, Double regressionPrediction) {
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
