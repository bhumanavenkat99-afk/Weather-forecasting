package com.weatherforecast.service;

import com.weatherforecast.dto.PredictionRequest;
import com.weatherforecast.dto.PredictionResponse;
import com.weatherforecast.model.PredictionHistory;
import com.weatherforecast.model.User;
import com.weatherforecast.repository.PredictionRepository;
import com.weatherforecast.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PredictionService {
    private final DecisionTreeService decisionTreeService;
    private final RegressionService regressionService;
    private final UserRepository userRepository;
    private final PredictionRepository predictionRepository;

    public PredictionService(DecisionTreeService decisionTreeService,
                             RegressionService regressionService,
                             UserRepository userRepository,
                             PredictionRepository predictionRepository) {
        this.decisionTreeService = decisionTreeService;
        this.regressionService = regressionService;
        this.userRepository = userRepository;
        this.predictionRepository = predictionRepository;
    }

    public PredictionResponse predict(PredictionRequest request, String userEmail) throws Exception {
        String treeResult = decisionTreeService.predict(
                request.getTemperature(),
                request.getHumidity(),
                request.getPressure(),
                request.getWindSpeed(),
                request.getCloudCover(),
                request.getPrecipitation()
        );

        double regressionResult = regressionService.predict(
                request.getTemperature(),
                request.getHumidity(),
                request.getPressure(),
                request.getWindSpeed(),
                request.getCloudCover(),
                request.getPrecipitation()
        );

        savePredictionHistory(request, userEmail, treeResult, regressionResult);

        String explanation = "The decision tree predicts whether rain is likely based on humidity and precipitation patterns. " +
                "The regression model estimates the next-day temperature from current weather attributes.";

        return new PredictionResponse(treeResult, regressionResult, explanation);
    }

    public String predictDecisionTree(PredictionRequest request) throws Exception {
        return decisionTreeService.predict(
                request.getTemperature(),
                request.getHumidity(),
                request.getPressure(),
                request.getWindSpeed(),
                request.getCloudCover(),
                request.getPrecipitation()
        );
    }

    public double predictRegression(PredictionRequest request) throws Exception {
        return regressionService.predict(
                request.getTemperature(),
                request.getHumidity(),
                request.getPressure(),
                request.getWindSpeed(),
                request.getCloudCover(),
                request.getPrecipitation()
        );
    }

    private void savePredictionHistory(PredictionRequest request, String userEmail,
                                       String decisionTreeResult, double regressionResult) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + userEmail));
        PredictionHistory history = new PredictionHistory();
        history.setUser(user);
        history.setPredictionDate(LocalDateTime.now());
        history.setTemperature(request.getTemperature());
        history.setHumidity(request.getHumidity());
        history.setPressure(request.getPressure());
        history.setWindSpeed(request.getWindSpeed());
        history.setCloudCover(request.getCloudCover());
        history.setPrecipitation(request.getPrecipitation());
        history.setDecisionTreePrediction(decisionTreeResult);
        history.setRegressionPrediction(regressionResult);
        predictionRepository.save(history);
    }

    public java.util.List<PredictionHistory> getHistoryForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));
        return predictionRepository.findByUserOrderByPredictionDateDesc(user);
    }
}
