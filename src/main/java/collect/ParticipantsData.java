package collect;

import api.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ParticipantsData {
    private ApiClient apiClient;
    private String sport;

    public ParticipantsData(ApiClient apiClient, String sport) {
        this.apiClient = apiClient;
        this.sport = sport;
    }

    public Map<Integer, Map<String, String>> getParticipantsData() {
        String apiResponse = apiClient.getParticipants(sport);
        JSONArray playersArray = new JSONObject(apiResponse).getJSONArray("apiResults").getJSONObject(0)
                .getJSONObject("league").getJSONArray("players");
        Map<Integer, Map<String, String>> allPlayerData = new HashMap<>();
        for (Object object : playersArray) {
            JSONObject playerObject = (JSONObject) object;
            Map<String, String> statMap = new HashMap<>();
            statMap.put("name", playerObject.getString("firstName") + " " + playerObject.getString("lastName"));
            statMap.put("team", playerObject.getJSONObject("team").getString("abbreviation"));
            allPlayerData.put(playerObject.getInt("playerId"), statMap);
        }
        return allPlayerData;
    }
}
