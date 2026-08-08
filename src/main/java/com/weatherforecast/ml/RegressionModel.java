package com.weatherforecast.ml;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.DenseInstance;
import weka.core.Instances;

public class RegressionModel {
    private Classifier regressor;
    private double mae;
    private double mse;
    private double rmse;
    private double r2;

    public void train(Instances dataset) throws Exception {
        if (dataset == null || dataset.numInstances() == 0) {
            throw new IllegalArgumentException("Empty dataset for regression training");
        }
        int trainSize = (int) Math.round(dataset.numInstances() * 0.75);
        int testSize = dataset.numInstances() - trainSize;
        Instances trainingData = new Instances(dataset, 0, trainSize);
        Instances testingData = new Instances(dataset, trainSize, testSize);

        LinearRegression linearRegression = new LinearRegression();
        linearRegression.buildClassifier(trainingData);
        regressor = linearRegression;

        Evaluation evaluation = new Evaluation(trainingData);
        evaluation.evaluateModel(regressor, testingData);
        mae = evaluation.meanAbsoluteError();
        rmse = evaluation.rootMeanSquaredError();
        mse = rmse * rmse;
        double correlation = evaluation.correlationCoefficient();
        r2 = correlation * correlation;
    }

    public double predict(double temperature, double humidity, double pressure,
                          double windSpeed, double cloudCover, double precipitation) throws Exception {
        if (regressor == null) {
            throw new IllegalStateException("Regression model has not been trained");
        }
        DataPreprocessor preprocessor = new DataPreprocessor();
        Instances header = preprocessor.buildRegressionDataset(java.util.Collections.emptyList());
        header.clear();
        double[] values = new double[header.numAttributes()];
        values[0] = temperature;
        values[1] = humidity;
        values[2] = pressure;
        values[3] = windSpeed;
        values[4] = cloudCover;
        values[5] = precipitation;
        values[6] = 0.0;
        header.add(new DenseInstance(1.0, values));
        header.setClassIndex(header.numAttributes() - 1);
        return regressor.classifyInstance(header.instance(0));
    }

    public double getMae() {
        return mae;
    }

    public double getMse() {
        return mse;
    }

    public double getRmse() {
        return rmse;
    }

    public double getR2() {
        return r2;
    }
}
