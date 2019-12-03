package collect;

import api.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;
import util.DateOperations;

import java.util.HashMap;
import java.util.Map;

import static collect.StandardProjectionsData.addProjectionsToMap;

public class NFLProjectionsData {
    private ApiClient apiClient;
    private Map<Integer, Map<Object, Object>> eventData;

    public NFLProjectionsData(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public Map<Integer, Map<String, Object>> getFantasyProjections() {
        Map<Integer, Map<String, Object>> projectionsData = new HashMap<>();
        eventData = new EventData(apiClient, "nfl").getEventData();
        String apiResponse = apiClient.getProjectionsFromThisWeek("nfl");
        JSONObject projectionsJson = new JSONObject(apiResponse).getJSONArray("apiResults").getJSONObject(0)
                .getJSONObject("league").getJSONObject("season").getJSONArray("eventType").getJSONObject(0)
                .getJSONObject("fantasyProjections");
        JSONArray offensiveJson = projectionsJson.getJSONArray("offensiveProjections");
        JSONArray defensiveJson = projectionsJson.getJSONArray("defensiveProjections");
        projectionsData.putAll(getOffensiveProjections(offensiveJson));
        projectionsData.putAll(getDefensiveProjections(defensiveJson));
        return projectionsData;
    }

    private Map<Integer, Map<String, Object>> getOffensiveProjections(JSONArray offensiveJson) {
        Map<Integer, Map<String, Object>> offensiveMap = new HashMap<>();
        for (Object object : offensiveJson) {
            JSONObject playerObject = (JSONObject) object;
            int playerId = playerObject.getJSONObject("player").getInt("playerId");
            Map<String, Object> statMap = new HashMap<>();
            statMap.put("name", playerObject.getJSONObject("player").getString("firstName") + " " +
                    playerObject.getJSONObject("player").getString("lastName"));
            addInfoToPlayerMap(offensiveMap, playerObject, playerId, statMap);
        }
        return offensiveMap;
    }

    private Map<Integer, Map<String, Object>> getDefensiveProjections(JSONArray defensiveJson) {
        Map<Integer, Map<String, Object>> defensiveMap = new HashMap<>();
        for (Object object : defensiveJson) {
            JSONObject playerObject = (JSONObject) object;
            int teamId = playerObject.getJSONObject("team").getInt("teamId");
            Map<String, Object> statMap = new HashMap<>();
            statMap.put("name", playerObject.getJSONObject("team").getString("nickname") + " D/ST");
            addInfoToPlayerMap(defensiveMap, playerObject, teamId, statMap);
        }
        return defensiveMap;
    }

    private void addInfoToPlayerMap(Map<Integer, Map<String, Object>> playerMap, JSONObject playerObject,
                                    int id, Map<String, Object> statMap) {
        statMap.put("team", playerObject.getJSONObject("team").getString("abbreviation"));
        statMap.put("opponent", eventData.get(playerObject.getInt("eventId"))
                .get(playerObject.getJSONObject("team").getInt("teamId")));
        statMap.put("gameDate",
                new DateOperations().getEasternTime(playerObject.getJSONObject("gameDate").getString("full"),
                        playerObject.getJSONObject("gameDate").getString("dateType"), "EEE h:mma z"));
        addProjectionsToMap(playerMap, playerObject, statMap, id);
    }
}
