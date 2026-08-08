package com.weatherforecast.service;

import com.weatherforecast.dto.PredictionRequest;
import com.weatherforecast.model.PredictionHistory;
import com.weatherforecast.model.User;
import com.weatherforecast.repository.PredictionRepository;
import com.weatherforecast.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PredictionServiceTest {
    private DecisionTreeService decisionTreeService;
    private RegressionService regressionService;
    private UserRepository userRepository;
    private PredictionRepository predictionRepository;
    private PredictionService predictionService;

    @BeforeEach
    void setUp() {
        decisionTreeService = Mockito.mock(DecisionTreeService.class);
        regressionService = Mockito.mock(RegressionService.class);
        userRepository = Mockito.mock(UserRepository.class);
        predictionRepository = Mockito.mock(PredictionRepository.class);
        predictionService = new PredictionService(decisionTreeService, regressionService, userRepository, predictionRepository);
    }

    @Test
    void predictReturnsResponseAndSavesHistory() throws Exception {
        PredictionRequest request = new PredictionRequest();
        request.setTemperature(18.0);
        request.setHumidity(70.0);
        request.setPressure(1012.0);
        request.setWindSpeed(12.0);
        request.setCloudCover(40.0);
        request.setPrecipitation(0.0);

        when(decisionTreeService.predict(anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn("No Rain");
        when(regressionService.predict(anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(19.5);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(new User()));
        when(predictionRepository.save(any(PredictionHistory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = predictionService.predict(request, "user@example.com");

        assertEquals("No Rain", response.getDecisionTreeResult());
        assertEquals(19.5, response.getRegressionResult());
        verify(predictionRepository).save(any(PredictionHistory.class));
    }
}
