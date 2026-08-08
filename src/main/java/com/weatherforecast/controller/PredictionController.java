package com.weatherforecast.controller;

import com.weatherforecast.dto.PredictionRequest;
import com.weatherforecast.dto.PredictionResponse;
import com.weatherforecast.dto.PredictionHistoryDto;
import com.weatherforecast.model.PredictionHistory;
import com.weatherforecast.service.PredictionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prediction")
public class PredictionController {
    private final PredictionService predictionService;

    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @PostMapping("/decision-tree")
    public ResponseEntity<String> decisionTreePrediction(@Valid @RequestBody PredictionRequest request,
                                                         Authentication authentication) throws Exception {
        String result = predictionService.predictDecisionTree(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/regression")
    public ResponseEntity<Double> regressionPrediction(@Valid @RequestBody PredictionRequest request,
                                                       Authentication authentication) throws Exception {
        Double result = predictionService.predictRegression(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/predict")
    public ResponseEntity<PredictionResponse> combinedPrediction(@Valid @RequestBody PredictionRequest request,
                                                                  Authentication authentication) throws Exception {
        PredictionResponse response = predictionService.predict(request, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<PredictionHistoryDto>> getPredictionHistory(Authentication authentication) {
        List<PredictionHistory> history = predictionService.getHistoryForUser(authentication.getName());
        List<PredictionHistoryDto> dtoList = history.stream()
                .map(item -> new PredictionHistoryDto(
                        item.getId(),
                        item.getUser().getEmail(),
                        item.getPredictionDate(),
                        item.getTemperature(),
                        item.getHumidity(),
                        item.getPressure(),
                        item.getWindSpeed(),
                        item.getCloudCover(),
                        item.getPrecipitation(),
                        item.getDecisionTreePrediction(),
                        item.getRegressionPrediction()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }
}
