package com.weatherforecast.ml;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;

import java.util.Random;

public class DecisionTreeModel {
    private Classifier classifier;
    private double accuracy;
    private double precision;
    private double recall;
    private double f1Score;

    public void train(Instances dataset) throws Exception {
        if (dataset == null || dataset.numInstances() == 0) {
            throw new IllegalArgumentException("Empty dataset for decision tree training");
        }
        int trainSize = (int) Math.round(dataset.numInstances() * 0.75);
        int testSize = dataset.numInstances() - trainSize;
        Instances trainingData = new Instances(dataset, 0, trainSize);
        Instances testingData = new Instances(dataset, trainSize, testSize);

        if (trainingData.numInstances() < 2) {
            throw new IllegalArgumentException("Not enough training instances for decision tree learning");
        }

        J48 tree = new J48();
        tree.setUnpruned(false);
        tree.buildClassifier(trainingData);
        classifier = tree;

        Evaluation evaluation = new Evaluation(trainingData);
        if (testingData.numInstances() > 0) {
            evaluation.evaluateModel(classifier, testingData);
            accuracy = evaluation.pctCorrect() / 100.0;
            precision = evaluation.weightedPrecision();
            recall = evaluation.weightedRecall();
            f1Score = evaluation.weightedFMeasure();
        } else {
            evaluation.crossValidateModel(classifier, trainingData, 3, new Random(42));
            accuracy = evaluation.pctCorrect() / 100.0;
            precision = evaluation.weightedPrecision();
            recall = evaluation.weightedRecall();
            f1Score = evaluation.weightedFMeasure();
        }
    }

    public String predict(double temperature, double humidity, double pressure, double windSpeed,
                          double cloudCover, double precipitation) throws Exception {
        if (classifier == null) {
            throw new IllegalStateException("Decision tree model has not been trained");
        }
        Instances header = createHeader();
        double[] values = new double[header.numAttributes()];
        values[0] = temperature;
        values[1] = humidity;
        values[2] = pressure;
        values[3] = windSpeed;
        values[4] = cloudCover;
        values[5] = precipitation;
        header.add(new weka.core.DenseInstance(1.0, values));
        header.setClassIndex(header.numAttributes() - 1);
        double resultIndex = classifier.classifyInstance(header.instance(0));
        return header.classAttribute().value((int) resultIndex);
    }

    private Instances createHeader() {
        DataPreprocessor preprocessor = new DataPreprocessor();
        Instances header = preprocessor.buildClassificationDataset(java.util.Collections.emptyList());
        header.clear();
        return header;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getPrecision() {
        return precision;
    }

    public double getRecall() {
        return recall;
    }

    public double getF1Score() {
        return f1Score;
    }
}
