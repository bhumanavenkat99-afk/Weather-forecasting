package com.weatherforecast.controller;

import com.weatherforecast.dto.ModelPerformanceResponse;
import com.weatherforecast.service.DecisionTreeService;
import com.weatherforecast.service.RegressionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/model")
public class ModelController {
    private final DecisionTreeService decisionTreeService;
    private final RegressionService regressionService;

    public ModelController(DecisionTreeService decisionTreeService, RegressionService regressionService) {
        this.decisionTreeService = decisionTreeService;
        this.regressionService = regressionService;
    }

    @GetMapping("/performance")
    public ResponseEntity<ModelPerformanceResponse> getModelPerformance() {
        ModelPerformanceResponse response = new ModelPerformanceResponse(
                decisionTreeService.getAccuracy(),
                decisionTreeService.getPrecision(),
                decisionTreeService.getRecall(),
                decisionTreeService.getF1Score(),
                regressionService.getMae(),
                regressionService.getMse(),
                regressionService.getRmse(),
                regressionService.getR2()
        );
        return ResponseEntity.ok(response);
    }
}
