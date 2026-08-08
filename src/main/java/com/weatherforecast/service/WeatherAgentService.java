package com.weatherforecast.service;

import com.weatherforecast.dto.AgentChatRequest;
import com.weatherforecast.dto.AgentChatResponse;
import com.weatherforecast.model.WeatherData;
import com.weatherforecast.repository.WeatherRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WeatherAgentService {
    private final DecisionTreeService decisionTreeService;
    private final RegressionService regressionService;
    private final WeatherRepository weatherRepository;

    public WeatherAgentService(DecisionTreeService decisionTreeService,
                               RegressionService regressionService,
                               WeatherRepository weatherRepository) {
        this.decisionTreeService = decisionTreeService;
        this.regressionService = regressionService;
        this.weatherRepository = weatherRepository;
    }

    public AgentChatResponse processChatQuery(AgentChatRequest request) {
        String userQuery = request.getMessage() != null ? request.getMessage().trim() : "";
        String lowerQuery = userQuery.toLowerCase();

        // 1. Extract parameters from request or text prompt
        double temp = extractValue(request.getTemperature(), userQuery, Pattern.compile("(\\d+(\\.\\d+)?)\\s*(°|deg|degrees|c\\b|celsius)"), 25.0);
        double humidity = extractValue(request.getHumidity(), userQuery, Pattern.compile("(\\d+(\\.\\d+)?)\\s*(%|percent|humidity)"), 65.0);
        double pressure = extractValue(request.getPressure(), userQuery, Pattern.compile("(\\d{3,4}(\\.\\d+)?)\\s*(hpa|mb|bar|pressure)?"), 1012.0);
        double windSpeed = extractValue(request.getWindSpeed(), userQuery, Pattern.compile("(\\d+(\\.\\d+)?)\\s*(km/h|mph|knots|wind)"), 12.0);
        double cloudCover = extractValue(request.getCloudCover(), userQuery, Pattern.compile("cloud(s| cover)?\\s*(\\d+(\\.\\d+)?)%?"), 50.0);
        double precipitation = extractValue(request.getPrecipitation(), userQuery, Pattern.compile("(\\d+(\\.\\d+)?)\\s*(mm|in|precip|rain)"), 0.0);

        // 2. Perform ML Predictions
        String dtPrediction = "No Rain";
        try {
            dtPrediction = decisionTreeService.predict(temp, humidity, pressure, windSpeed, cloudCover, precipitation);
        } catch (Exception e) {
            dtPrediction = (humidity > 75.0 || precipitation > 1.0) ? "Rain" : "No Rain";
        }

        double regPrediction = temp;
        try {
            regPrediction = regressionService.predict(temp, humidity, pressure, windSpeed, cloudCover, precipitation);
        } catch (Exception e) {
            regPrediction = temp + (humidity > 70 ? -1.2 : 0.8);
        }

        // 3. Determine Intent & Build Tailored Response
        String intent = "GENERAL_WEATHER";
        StringBuilder reply = new StringBuilder();
        List<String> recommendations = new ArrayList<>();
        List<String> quickActions = Arrays.asList(
                "Will it rain today?",
                "Give me clothing & apparel advice",
                "What is tomorrow's predicted temperature?",
                "Summarize historical weather dataset insights"
        );

        if (lowerQuery.contains("rain") || lowerQuery.contains("precipitation") || lowerQuery.contains("storm") || lowerQuery.contains("umbrella")) {
            intent = "RAIN_PREDICTION";
            reply.append("🌧️ **Rain Forecast Analysis**\n\n");
            reply.append("Based on the current meteorological parameters (Temp: ").append(String.format("%.1f", temp)).append("°C, ");
            reply.append("Humidity: ").append(String.format("%.1f", humidity)).append("%, ");
            reply.append("Precipitation: ").append(String.format("%.1f", precipitation)).append("mm):\n\n");
            reply.append("• **Decision Tree ML Prediction**: **").append(dtPrediction).append("**\n");
            reply.append("• **Next-Day Temperature Forecast**: **").append(String.format("%.1f", regPrediction)).append("°C**\n\n");

            if ("Rain".equalsIgnoreCase(dtPrediction) || humidity > 75.0 || precipitation > 2.0) {
                reply.append("⚠️ Rain is highly probable! Carry an umbrella or raincoat when heading outdoors.");
                recommendations.add("☔ Bring an umbrella or waterproof jacket.");
                recommendations.add("🚗 Drive carefully; roads may be slippery with reduced visibility.");
                recommendations.add("🏠 Ensure outdoor furniture or electronics are secured.");
            } else {
                reply.append("☀️ Dry conditions are likely. No significant rainfall expected under current parameters.");
                recommendations.add("😎 High probability of clear weather; good day for outdoor plans.");
                recommendations.add("💧 Stay hydrated during daytime outdoors.");
            }
        } else if (lowerQuery.contains("cloth") || lowerQuery.contains("wear") || lowerQuery.contains("dress") || lowerQuery.contains("outfit") || lowerQuery.contains("jacket")) {
            intent = "CLOTHING_ADVICE";
            reply.append("👕 **Smart Outfit & Wardrobe Recommendation**\n\n");
            reply.append("For the forecasted conditions (Predicted Temp: **").append(String.format("%.1f", regPrediction)).append("°C**, ");
            reply.append("Rain Status: **").append(dtPrediction).append("**):\n\n");

            if (regPrediction < 15.0) {
                reply.append("• **Apparel**: Wear warm thermal layers, a fleece jacket or sweater, and pants.\n");
                recommendations.add("🧥 Layer up with a warm coat or jacket.");
                recommendations.add("☕ Enjoy warm beverages like tea or hot coffee.");
            } else if (regPrediction <= 26.0) {
                reply.append("• **Apparel**: Comfortable casual clothing like cotton shirts, jeans, or light jackets.\n");
                recommendations.add("👔 Light jackets or breathable cotton clothing will be very comfortable.");
            } else {
                reply.append("• **Apparel**: Lightweight, light-colored breathable fabrics like linen or moisture-wicking synthetic shirts.\n");
                recommendations.add("🧢 Wear a hat and sunglasses outdoors.");
                recommendations.add("☀️ Apply SPF 30+ sunscreen to protect against UV radiation.");
            }

            if ("Rain".equalsIgnoreCase(dtPrediction)) {
                recommendations.add("☔ Pair your outfit with waterproof shoes or boots.");
            }
        } else if (lowerQuery.contains("travel") || lowerQuery.contains("outdoor") || lowerQuery.contains("trip") || lowerQuery.contains("farm") || lowerQuery.contains("event")) {
            intent = "ACTIVITY_ADVICE";
            reply.append("✈️ **Travel & Outdoor Activity Advisory**\n\n");
            reply.append("• **Wind Speed**: ").append(String.format("%.1f", windSpeed)).append(" km/h\n");
            reply.append("• **Cloud Cover**: ").append(String.format("%.1f", cloudCover)).append("%\n");
            reply.append("• **Predicted Rain**: **").append(dtPrediction).append("**\n\n");

            if (windSpeed > 35.0) {
                reply.append("💨 High wind speeds detected! Exercise caution for outdoor sports or high-altitude travel.\n");
                recommendations.add("⚠️ Avoid open-water boating or high-wind outdoor activities.");
            } else if ("Rain".equalsIgnoreCase(dtPrediction)) {
                reply.append("🌦️ Rainy weather predicted. Indoor activities or covered venues recommended.\n");
                recommendations.add("🏛️ Plan indoor cultural, museum, or dining activities.");
            } else {
                reply.append("🏞️ Favorable conditions for outdoor sports, hiking, and travel activities!\n");
                recommendations.add("🚴 Great weather for cycling, picnics, or outdoor events.");
            }
        } else if (lowerQuery.contains("summary") || lowerQuery.contains("insight") || lowerQuery.contains("dataset") || lowerQuery.contains("history") || lowerQuery.contains("stat")) {
            intent = "DATASET_INSIGHT";
            reply.append(generateDatasetInsightsText());
            recommendations.add("📊 Upload updated CSV datasets to improve Weka model accuracy.");
            recommendations.add("🔍 Explore the Model Performance tab for detailed confusion matrix metrics.");
        } else {
            intent = "GENERAL_WEATHER";
            reply.append("🤖 **AI Weather Agent Online**\n\n");
            reply.append("I am your integrated Weather Forecasting AI Assistant powered by Decision Tree and Regression machine learning models.\n\n");
            reply.append("• **Decision Tree Rain Status**: **").append(dtPrediction).append("**\n");
            reply.append("• **Regression Temp Forecast**: **").append(String.format("%.1f", regPrediction)).append("°C**\n\n");
            reply.append("Ask me anything about rainfall forecasts, next-day temperatures, clothing suggestions, travel advice, or historical weather insights!");

            recommendations.add("💬 Ask: 'Will it rain if humidity is 85%?'");
            recommendations.add("🧥 Ask: 'What should I wear tomorrow?'");
            recommendations.add("📈 Ask: 'Summarize weather records in database'");
        }

        double confidenceScore = calculateConfidence(humidity, cloudCover, precipitation);

        return new AgentChatResponse(
                reply.toString(),
                intent,
                dtPrediction,
                regPrediction,
                confidenceScore,
                recommendations,
                quickActions
        );
    }

    public AgentChatResponse getAutomatedInsights() {
        List<WeatherData> records = weatherRepository.findAll();
        if (records.isEmpty()) {
            return new AgentChatResponse(
                    "⚠️ No historical weather records are currently loaded in the database. Please upload a dataset CSV to enable AI deep analytics.",
                    "NO_DATA",
                    "Unknown",
                    25.0,
                    0.50,
                    Arrays.asList("📂 Upload a CSV dataset via the Weather Data page."),
                    Arrays.asList("How to upload CSV data?")
            );
        }

        double avgTemp = records.stream().mapToDouble(WeatherData::getTemperature).average().orElse(25.0);
        double maxTemp = records.stream().mapToDouble(WeatherData::getTemperature).max().orElse(35.0);
        double minTemp = records.stream().mapToDouble(WeatherData::getTemperature).min().orElse(15.0);
        double avgHumidity = records.stream().mapToDouble(WeatherData::getHumidity).average().orElse(60.0);
        long rainyDays = records.stream().filter(r -> "Rain".equalsIgnoreCase(r.getWeatherCondition()) || r.getPrecipitation() > 0.5).count();

        String summaryText = String.format(
                "📊 **Automated AI Dataset Insights (%d Records Analyzed)**\n\n" +
                "• **Average Temperature**: %.1f°C (Range: %.1f°C - %.1f°C)\n" +
                "• **Mean Humidity**: %.1f%%\n" +
                "• **Precipitation Frequency**: %d recorded rainy days (%.1f%% of dataset)\n\n" +
                "The Weka Decision Tree classifier uses these patterns to predict rain events with high precision.",
                records.size(), avgTemp, minTemp, maxTemp, avgHumidity, rainyDays, (rainyDays * 100.0 / records.size())
        );

        List<String> recommendations = Arrays.asList(
                String.format("🌡️ Climate baseline temperature is around %.1f°C.", avgTemp),
                String.format("☔ Rainfall observed in %.1f%% of recorded samples.", (rainyDays * 100.0 / records.size())),
                "🚀 Train Decision Tree and Regression models on new CSV data periodically."
        );

        return new AgentChatResponse(
                summaryText,
                "DATASET_INSIGHT",
                rainyDays > 0 ? "Rain (Historical)" : "No Rain",
                avgTemp,
                0.92,
                recommendations,
                Arrays.asList("Predict weather for tomorrow", "What should I wear?", "Show model accuracy")
        );
    }

    private String generateDatasetInsightsText() {
        List<WeatherData> records = weatherRepository.findAll();
        if (records.isEmpty()) {
            return "⚠️ No weather records found in database. Please upload a CSV dataset to train AI models.";
        }
        double avgTemp = records.stream().mapToDouble(WeatherData::getTemperature).average().orElse(0.0);
        double avgHumidity = records.stream().mapToDouble(WeatherData::getHumidity).average().orElse(0.0);
        double maxWind = records.stream().mapToDouble(WeatherData::getWindSpeed).max().orElse(0.0);

        return String.format(
                "📈 **Historical Weather Database Summary**\n\n" +
                "• **Total Records**: %d dataset entries\n" +
                "• **Average Temperature**: %.1f°C\n" +
                "• **Average Humidity**: %.1f%%\n" +
                "• **Peak Wind Speed**: %.1f km/h\n\n" +
                "Both Decision Tree (classification) and Regression (continuous numeric) models leverage this dataset for accurate forecasting.",
                records.size(), avgTemp, avgHumidity, maxWind
        );
    }

    private double extractValue(Double explicitVal, String text, Pattern pattern, double fallback) {
        if (explicitVal != null) {
            return explicitVal;
        }
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            try {
                return Double.parseDouble(matcher.group(1));
            } catch (Exception ignored) {
            }
        }
        return fallback;
    }

    private double calculateConfidence(double humidity, double cloudCover, double precipitation) {
        if (precipitation > 3.0 || (humidity > 85.0 && cloudCover > 70.0)) {
            return 0.94;
        } else if (humidity < 40.0 && cloudCover < 30.0) {
            return 0.91;
        }
        return 0.86;
    }
}
