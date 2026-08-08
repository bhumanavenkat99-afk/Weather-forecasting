package com.weatherforecast.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/weather")
    public String weatherPage() {
        return "weather";
    }

    @GetMapping("/prediction")
    public String predictionPage() {
        return "prediction";
    }

    @GetMapping("/history")
    public String historyPage() {
        return "history";
    }

    @GetMapping("/performance")
    public String performancePage() {
        return "model-performance";
    }

    @GetMapping("/agent")
    public String agentPage() {
        return "agent";
    }
}
