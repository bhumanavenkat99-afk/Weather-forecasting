package com.weatherforecast.controller;

import com.weatherforecast.dto.AgentChatRequest;
import com.weatherforecast.dto.AgentChatResponse;
import com.weatherforecast.service.WeatherAgentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agent")
public class AgentController {
    private final WeatherAgentService weatherAgentService;

    public AgentController(WeatherAgentService weatherAgentService) {
        this.weatherAgentService = weatherAgentService;
    }

    @PostMapping("/chat")
    public ResponseEntity<AgentChatResponse> chat(@Valid @RequestBody AgentChatRequest request,
                                                  Authentication authentication) {
        AgentChatResponse response = weatherAgentService.processChatQuery(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/insights")
    public ResponseEntity<AgentChatResponse> getInsights(Authentication authentication) {
        AgentChatResponse response = weatherAgentService.getAutomatedInsights();
        return ResponseEntity.ok(response);
    }
}
