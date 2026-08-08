package com.weatherforecast.dto;

import java.util.List;

public class AgentChatResponse {
    private String responseMessage;
    private String intent;
    private String decisionTreePrediction;
    private Double regressionPrediction;
    private Double confidenceScore;
    private List<String> recommendations;
    private List<String> quickActions;

    public AgentChatResponse() {
    }

    public AgentChatResponse(String responseMessage, String intent, String decisionTreePrediction,
                             Double regressionPrediction, Double confidenceScore,
                             List<String> recommendations, List<String> quickActions) {
        this.responseMessage = responseMessage;
        this.intent = intent;
        this.decisionTreePrediction = decisionTreePrediction;
        this.regressionPrediction = regressionPrediction;
        this.confidenceScore = confidenceScore;
        this.recommendations = recommendations;
        this.quickActions = quickActions;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
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

    public Double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(Double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    public List<String> getQuickActions() {
        return quickActions;
    }

    public void setQuickActions(List<String> quickActions) {
        this.quickActions = quickActions;
    }
}
