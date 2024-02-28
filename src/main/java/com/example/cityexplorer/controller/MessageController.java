package com.example.cityexplorer.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MessageController {

    private static final String API_KEY = "NhoYnVN8Gc1jw/DpAhmrHA==ERZoPyHAtZgn1i3c";

    @GetMapping("/{city}")
    public ResponseEntity<SkatingConditions> getWeatherData(@PathVariable String city) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Api-Key", "NhoYnVN8Gc1jw/DpAhmrHA==ERZoPyHAtZgn1i3c");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = UriComponentsBuilder.fromHttpUrl("https://api.api-ninjas.com/v1/weather")
                    .queryParam("city", city)
                    .toUriString();

            ResponseEntity<WeatherData> response = restTemplate.exchange(url, HttpMethod.GET, entity, WeatherData.class);
            WeatherData weatherData = response.getBody();

            SkatingConditions skatingConditions = calculateSkatingConditions(weatherData);
            return new ResponseEntity<>(skatingConditions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new SkatingConditions("Error calculating conditions", 0, new HashMap<>()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private SkatingConditions calculateSkatingConditions(WeatherData weatherData) {
        SkatingConditions conditions = new SkatingConditions();

        // Temperature suitability and explanation
        boolean temperatureSuitable = weatherData.getTemp() >= 10 && weatherData.getTemp() <= 25;
        String temperatureExplanation = temperatureSuitable ? "Good temperature for skating" : "Too hot/cold for skating";
        conditions.addDetail("temperature", Map.of("value", weatherData.getTemp() + "°C", "comment", temperatureExplanation));

        // Humidity suitability and explanation
        boolean humiditySuitable = weatherData.getHumidity() <= 65;
        String humidityExplanation = humiditySuitable ? "Low humidity, good conditions for skating" : "High humidity, poor conditions for skating";
        conditions.addDetail("humidity", Map.of("value", weatherData.getHumidity() + "%", "comment", humidityExplanation));

        // Wind speed suitability and explanation
        boolean windSpeedSuitable = weatherData.getWind_speed() <= 16.09;
        String windSpeedExplanation = windSpeedSuitable ? "Low wind speed, good conditions for skating" : "High wind speed, poor conditions for skating";
        conditions.addDetail("windSpeed", Map.of("value", weatherData.getWind_speed() + " km/h", "comment", windSpeedExplanation));

        // Cloud cover suitability and explanation
        boolean cloudCoverSuitable = weatherData.getCloudy() <= 50;
        String cloudCoverExplanation = cloudCoverSuitable ? "Clear skies, good conditions for skating" : "Cloudy skies, skating may be less enjoyable";
        conditions.addDetail("chanceOfClouds", Map.of("value", weatherData.getCloudy() + "%", "comment", cloudCoverExplanation));

        // Min temperature suitability and explanation
        boolean minTempSuitable = weatherData.getMin_temp() >= 15.56;
        String minTempExplanation = minTempSuitable ? "Min temperature suitable for skating" : "Min temperature too low for skating";
        conditions.addDetail("minTemp", Map.of("value", weatherData.getMin_temp() + "°C", "comment", minTempExplanation));

        // Max temperature suitability and explanation
        boolean maxTempSuitable = weatherData.getMax_temp() <= 26.53;
        String maxTempExplanation = maxTempSuitable ? "Max temperature suitable for skating" : "Max temperature too high for skating";
        conditions.addDetail("maxTemp", Map.of("value", weatherData.getMax_temp() + "°C", "comment", maxTempExplanation));

        // Rain suitability (assuming a basic logic based on humidity and cloud cover)
        boolean rainSuitable = weatherData.getHumidity() > 70 && weatherData.getCloudy() > 50;
        String rainExplanation = rainSuitable ? "Rain likely, poor conditions for skating" : "No rain expected, good conditions for skating";
        conditions.addDetail("rain", Map.of("comment", rainExplanation));

        // Snow suitability (assuming basic logic based on min and max temperatures)
        boolean snowSuitable = weatherData.getMin_temp() <= 2 && weatherData.getMax_temp() <= 4;
        String snowExplanation = snowSuitable ? "Snow likely, poor conditions for skating" : "No snow expected, good conditions for skating";
        conditions.addDetail("snow", Map.of("comment", snowExplanation));

        // Calculate overall score
        int overallScore = calculateOverallScore(new boolean[]{temperatureSuitable, humiditySuitable, windSpeedSuitable, cloudCoverSuitable, minTempSuitable, maxTempSuitable, !rainSuitable, !snowSuitable});
        conditions.setOverallScore(overallScore);

        // Determine overall condition based on overall score
        String overallCondition;
        if (overallScore < 50) {
            overallCondition = "Poor conditions for skating";
        } else if (overallScore >= 50 && overallScore < 75) {
            overallCondition = "Moderate conditions for skating";
        } else {
            overallCondition = "Good conditions for skating";
        }
        conditions.setConditions(overallCondition);

        return conditions;
    }

    private int calculateOverallScore(boolean[] conditions) {
        int[] weights = {20, 15, 15, 10, 20, 20, 5, 5}; // Weights for each condition
        int score = 0;
        for (int i = 0; i < conditions.length; i++) {
            if (conditions[i]) {
                score += weights[i];
            }
        }
        return score;
    }

    // Nested class to represent weather data
    private static class WeatherData {
        private double temp;
        private double humidity;
        private double wind_speed;
        private double cloudy; // Assuming this represents cloud percentage
        private double min_temp;
        private double max_temp;
        // Additional fields from your JSON response
        private double wind_degrees;
        private long sunrise;
        private long sunset;

        // Getters and setters for all fields
        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getHumidity() {
            return humidity;
        }

        public void setHumidity(double humidity) {
            this.humidity = humidity;
        }

        public double getWind_speed() {
            return wind_speed;
        }

        public void setWind_speed(double wind_speed) {
            this.wind_speed = wind_speed;
        }

        public double getCloudy() {
            return cloudy;
        }

        public void setCloudy(double cloudy) {
            this.cloudy = cloudy;
        }

        public double getMin_temp() {
            return min_temp;
        }

        public void setMin_temp(double min_temp) {
            this.min_temp = min_temp;
        }

        public double getMax_temp() {
            return max_temp;
        }

        public void setMax_temp(double max_temp) {
            this.max_temp = max_temp;
        }

        public double getWind_degrees() {
            return wind_degrees;
        }

        public void setWind_degrees(double wind_degrees) {
            this.wind_degrees = wind_degrees;
        }

        public long getSunrise() {
            return sunrise;
        }

        public void setSunrise(long sunrise) {
            this.sunrise = sunrise;
        }

        public long getSunset() {
            return sunset;
        }

        public void setSunset(long sunset) {
            this.sunset = sunset;
        }
    }


    // Nested class to represent skating conditions
    private static class SkatingConditions {
        private String conditions;
        private int overallScore;
        private Map<String, Map<String, String>> details;

        public SkatingConditions() {
            this.details = new HashMap<>();
        }

        public SkatingConditions(String conditions, int overallScore, Map<String, Map<String, String>> details) {
            this.conditions = conditions;
            this.overallScore = overallScore;
            this.details = details;
        }

        // Getters and Setters
        public String getConditions() {
            return conditions;
        }

        public void setConditions(String conditions) {
            this.conditions = conditions;
        }

        public int getOverallScore() {
            return overallScore;
        }

        public void setOverallScore(int overallScore) {
            this.overallScore = overallScore;
        }

        public Map<String, Map<String, String>> getDetails() {
            return details;
        }

        public void setDetails(Map<String, Map<String, String>> details) {
            this.details = details;
        }

        public void addDetail(String key, Map<String, String> value) {
            this.details.put(key, value);
        }
    }

}
