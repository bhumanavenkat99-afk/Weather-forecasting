package com.weatherforecast.dto;

public class ModelPerformanceResponse {
    private double decisionTreeAccuracy;
    private double precision;
    private double recall;
    private double f1Score;
    private double mae;
    private double mse;
    private double rmse;
    private double r2;

    public ModelPerformanceResponse() {
    }

    public ModelPerformanceResponse(double decisionTreeAccuracy, double precision, double recall,
                                    double f1Score, double mae, double mse, double rmse, double r2) {
        this.decisionTreeAccuracy = decisionTreeAccuracy;
        this.precision = precision;
        this.recall = recall;
        this.f1Score = f1Score;
        this.mae = mae;
        this.mse = mse;
        this.rmse = rmse;
        this.r2 = r2;
    }

    public double getDecisionTreeAccuracy() {
        return decisionTreeAccuracy;
    }

    public void setDecisionTreeAccuracy(double decisionTreeAccuracy) {
        this.decisionTreeAccuracy = decisionTreeAccuracy;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getRecall() {
        return recall;
    }

    public void setRecall(double recall) {
        this.recall = recall;
    }

    public double getF1Score() {
        return f1Score;
    }

    public void setF1Score(double f1Score) {
        this.f1Score = f1Score;
    }

    public double getMae() {
        return mae;
    }

    public void setMae(double mae) {
        this.mae = mae;
    }

    public double getMse() {
        return mse;
    }

    public void setMse(double mse) {
        this.mse = mse;
    }

    public double getRmse() {
        return rmse;
    }

    public void setRmse(double rmse) {
        this.rmse = rmse;
    }

    public double getR2() {
        return r2;
    }

    public void setR2(double r2) {
        this.r2 = r2;
    }
}
