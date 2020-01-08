package collect.stats;

import api.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Participants {
    private ApiClient apiClient;
    private String sport;

    public Participants(ApiClient apiClient, String sport) {
        this.apiClient = apiClient;
        this.sport = sport;
    }

    public Map<Integer, Map<String, String>> getParticipantsData() {
        Map<Integer, Map<String, String>> allPlayerData = new HashMap<>();
        String apiResponse = apiClient.getParticipants(sport);
        if (apiResponse.length() > 0) {
            JSONArray playersArray = new JSONObject(apiResponse).getJSONArray("apiResults").getJSONObject(0)
                    .getJSONObject("league").getJSONArray("players");
            for (Object object : playersArray) {
                JSONObject playerObject = (JSONObject) object;
                Map<String, String> statMap = new HashMap<>();
                statMap.put("name", playerObject.getString("firstName") + " " + playerObject.getString("lastName"));
                statMap.put("team", playerObject.getJSONObject("team").getString("abbreviation"));
                allPlayerData.put(playerObject.getInt("playerId"), statMap);
            }
        }
        return allPlayerData;
    }
}
