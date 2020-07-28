package collect.misc;

import api.ApiClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Weather {
    private ApiClient apiClient;
    private String sport;

    public Weather(ApiClient apiClient, String sport) {
        this.apiClient = apiClient;
        this.sport = sport;
    }

    public Map<Integer, Map<String, String>> getWeatherData() {
        Map<Integer, Map<String, String>> weatherData = new HashMap<>();
        String apiResponse = apiClient.getWeatherData(sport);
        if (apiResponse.length() > 0) {
            JSONArray forecastArray = new JSONObject(apiResponse).getJSONArray("apiResults")
                    .getJSONObject(0).getJSONObject("league").getJSONObject("season")
                    .getJSONArray("eventType").getJSONObject(0).getJSONArray("weatherForecasts");
            for (Object object : forecastArray) {
                JSONObject weatherObject = (JSONObject) object;
                int eventId = weatherObject.getInt("eventId");
                Map<String, String> gameWeatherData = new HashMap<>();
                try {
                    JSONObject forecast = weatherObject.getJSONArray("forecasts").getJSONObject(0);
                    JSONObject temperature = forecast.getJSONArray("temperature").getJSONObject(0);
                    gameWeatherData.put("forecast", forecast.getString("condition"));
                    gameWeatherData.put("details",
                            temperature.getInt("degrees") + "°, " +
                                    Math.round(Float.parseFloat(forecast.getString("precipitation"))) + "% precip");
                    weatherData.put(eventId, gameWeatherData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return weatherData;
    }
}
