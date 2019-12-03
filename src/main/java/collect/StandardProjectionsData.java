package collect;

import api.ApiClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class StandardProjectionsData {
    private ApiClient apiClient;
    private String sport;

    public StandardProjectionsData(ApiClient apiClient, String sport) {
        this.apiClient = apiClient;
        this.sport = sport;
    }

    public Map<Integer, Map<String, Object>> getFantasyProjections() {
        Map<Integer, Map<String, Object>> projectionsData = new HashMap<>();
        Map<Integer, Map<Object, Object>> eventData = new EventData(apiClient, sport).getEventData();
        Set<Integer> eventIds = eventData.keySet();
        Map<Integer, Map<String, String>> participantsData = getParticipantsData();
        for (int eventId : eventIds) {
            String apiResponse = apiClient.getProjectionsFromEvent(sport, eventId);
            JSONObject projectionsJson = new JSONObject(apiResponse).getJSONArray("apiResults").getJSONObject(0)
                    .getJSONObject("league").getJSONObject("season").getJSONArray("eventType").getJSONObject(0)
                    .getJSONObject("fantasyProjections");
            for (Object object : projectionsJson.getJSONArray("teams")) {
                int teamId = ((JSONObject) object).getInt("teamId");
                JSONArray playerArray = ((JSONObject) object).getJSONArray(sport.equals("nhl") ? "skaters" : "players");
                collectProjectionsData(playerArray, projectionsData, eventData, participantsData, eventId, teamId);
                if (sport.equals("nhl")) {
                    JSONArray goalieArray = ((JSONObject) object).getJSONArray("goalies");
                    collectProjectionsData(goalieArray, projectionsData, eventData, participantsData, eventId, teamId);
                }
            }
        }
        return projectionsData;
    }

    private void collectProjectionsData(JSONArray playerArray,
                                        Map<Integer, Map<String, Object>> projectionsData,
                                        Map<Integer, Map<Object, Object>> eventData,
                                        Map<Integer, Map<String, String>> participantsData,
                                        int eventId, int teamId) {
        for (Object thing : playerArray) {
            JSONObject playerObject = (JSONObject) thing;
            Map<String, Object> statMap = new HashMap<>();
            int playerId = playerObject.getInt("playerId");
            if (participantsData.keySet().contains(playerId)) {
                statMap.put("name", participantsData.get(playerId).get("name"));
                statMap.put("team", participantsData.get(playerId).get("team"));
                statMap.put("opponent", eventData.get(eventId).get(teamId));
                statMap.put("gameDate", eventData.get(eventId).get("gameDate"));
                addProjectionsToMap(projectionsData, playerObject, statMap, playerId);
            }
        }
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

    static void addProjectionsToMap(Map<Integer, Map<String, Object>> projectionsData, JSONObject playerObject, Map<String, Object> statMap, int playerId) {
        try {
            statMap.put("dkProjection", Double.parseDouble(playerObject.getJSONArray("fantasyProjections")
                    .getJSONObject(0).getString("points")));
            statMap.put("fdProjection", Double.parseDouble(playerObject.getJSONArray("fantasyProjections")
                    .getJSONObject(1).getString("points")));
        } catch (JSONException e) {
            statMap.put("dkProjection", 0);
            statMap.put("fdProjection", 0);
        }
        projectionsData.put(playerId, statMap);
    }
}
