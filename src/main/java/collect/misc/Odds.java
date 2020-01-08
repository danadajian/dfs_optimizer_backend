package collect.misc;

import api.ApiClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Odds {
    private ApiClient apiClient;
    private String sport;

    public Odds(ApiClient apiClient, String sport) {
        this.apiClient = apiClient;
        this.sport = sport;
    }

    public Map<Integer, Map<String, Number>> getOddsData() {
        Map<Integer, Map<String, Number>> oddsData = new HashMap<>();
        String apiResponse = apiClient.getOddsData(sport);
        if (apiResponse.length() > 0) {
            JSONArray oddsArray = new JSONObject(apiResponse).getJSONArray("apiResults")
                    .getJSONObject(0).getJSONObject("league").getJSONObject("season")
                    .getJSONArray("eventType").getJSONObject(0).getJSONArray("lineEvents");
            for (Object object : oddsArray) {
                JSONObject oddsObject = (JSONObject) object;
                int eventId = oddsObject.getInt("eventId");
                Map<String, Number> gameOddsData = new HashMap<>();
                try {
                    JSONObject currentLine = oddsObject.getJSONArray("lines").getJSONObject(0)
                            .getJSONArray("line").getJSONObject(1);
                    gameOddsData.put("favoriteTeamId", currentLine.getInt("favoriteTeamId"));
                    gameOddsData.put("spread", currentLine.getNumber("favoritePoints"));
                    gameOddsData.put("overUnder", currentLine.getNumber("total"));
                    oddsData.put(eventId, gameOddsData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return oddsData;
    }
}
