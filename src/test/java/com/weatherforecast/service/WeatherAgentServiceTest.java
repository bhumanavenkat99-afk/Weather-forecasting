package com.weatherforecast.service;

import com.weatherforecast.dto.AgentChatRequest;
import com.weatherforecast.dto.AgentChatResponse;
import com.weatherforecast.repository.WeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

class WeatherAgentServiceTest {
    private DecisionTreeService decisionTreeService;
    private RegressionService regressionService;
    private WeatherRepository weatherRepository;
    private WeatherAgentService weatherAgentService;

    @BeforeEach
    void setUp() {
        decisionTreeService = Mockito.mock(DecisionTreeService.class);
        regressionService = Mockito.mock(RegressionService.class);
        weatherRepository = Mockito.mock(WeatherRepository.class);
        weatherAgentService = new WeatherAgentService(decisionTreeService, regressionService, weatherRepository);
    }

    @Test
    void processChatQueryRainForecast() throws Exception {
        when(decisionTreeService.predict(anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn("Rain");
        when(regressionService.predict(anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(22.0);

        AgentChatRequest request = new AgentChatRequest("Will it rain today if humidity is 90%?");
        AgentChatResponse response = weatherAgentService.processChatQuery(request);

        assertNotNull(response);
        assertEquals("RAIN_PREDICTION", response.getIntent());
        assertEquals("Rain", response.getDecisionTreePrediction());
        assertEquals(22.0, response.getRegressionPrediction());
        assertFalse(response.getRecommendations().isEmpty());
    }

    @Test
    void processChatQueryClothingAdvice() throws Exception {
        when(decisionTreeService.predict(anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn("No Rain");
        when(regressionService.predict(anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(28.5);

        AgentChatRequest request = new AgentChatRequest("What should I wear tomorrow?");
        AgentChatResponse response = weatherAgentService.processChatQuery(request);

        assertNotNull(response);
        assertEquals("CLOTHING_ADVICE", response.getIntent());
        assertFalse(response.getRecommendations().isEmpty());
    }

    @Test
    void getAutomatedInsightsEmptyData() {
        when(weatherRepository.findAll()).thenReturn(Collections.emptyList());
        AgentChatResponse response = weatherAgentService.getAutomatedInsights();

        assertNotNull(response);
        assertEquals("NO_DATA", response.getIntent());
    }
}
