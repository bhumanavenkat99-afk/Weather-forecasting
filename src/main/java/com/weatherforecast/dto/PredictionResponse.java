package com.weatherforecast.dto;

public class PredictionResponse {
    private String decisionTreeResult;
    private Double regressionResult;
    private String explanation;

    public PredictionResponse() {
    }

    public PredictionResponse(String decisionTreeResult, Double regressionResult, String explanation) {
        this.decisionTreeResult = decisionTreeResult;
        this.regressionResult = regressionResult;
        this.explanation = explanation;
    }

    public String getDecisionTreeResult() {
        return decisionTreeResult;
    }

    public void setDecisionTreeResult(String decisionTreeResult) {
        this.decisionTreeResult = decisionTreeResult;
    }

    public Double getRegressionResult() {
        return regressionResult;
    }

    public void setRegressionResult(Double regressionResult) {
        this.regressionResult = regressionResult;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
