package collect;

import api.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WrapStatsData {
    private ApiClient apiClient;
    private int thisWeek;

    WrapStatsData(ApiClient apiClient) {
        this.apiClient = apiClient;
        this.thisWeek = 0;
    }

    List<Integer> getEventIdsFromThisWeek() {
        String apiResponse = apiClient.getEventsFromThisWeek();
        JSONArray eventsArray = new JSONObject(apiResponse).getJSONArray("apiResults").getJSONObject(0)
                .getJSONObject("league").getJSONObject("season").getJSONArray("eventType").getJSONObject(0)
                .getJSONArray("events");
        List<Integer> allEvents = new ArrayList<>();
        for (Object eventObject : eventsArray) {
            int eventId = ((JSONObject) eventObject).getInt("eventId");
            allEvents.add(eventId);
        }
        return allEvents;
    }

    Map<Integer, Map<String, Object>> getFantasyProjections() {
        String apiResponse = apiClient.getProjectionsFromThisWeek();
        JSONObject projectionsJson = new JSONObject(apiResponse).getJSONArray("apiResults").getJSONObject(0)
                .getJSONObject("league").getJSONObject("season").getJSONArray("eventType").getJSONObject(0)
                .getJSONObject("fantasyProjections");
        thisWeek = projectionsJson.getInt("week");
        JSONArray offensiveJson = projectionsJson.getJSONArray("offensiveProjections");
        JSONArray defensiveJson = projectionsJson.getJSONArray("defensiveProjections");
        Map<Integer, Map<String, Object>> resultMap = new HashMap<>();
        resultMap.putAll(getOffensiveProjections(offensiveJson));
        resultMap.putAll(getDefensiveProjections(defensiveJson));
        return resultMap;
    }

    private Map<Integer, Map<String, Object>> getOffensiveProjections(JSONArray offensiveJson) {
        Map<Integer, Map<String, Object>> offensiveMap = new HashMap<>();
        for (Object object : offensiveJson) {
            JSONObject playerObject = (JSONObject) object;
            int playerId = playerObject.getJSONObject("player").getInt("playerId");
            Map<String, Object> statMap = new HashMap<>();
            collectProjections(offensiveMap, playerObject, playerId, statMap);
        }
        return offensiveMap;
    }

    private Map<Integer, Map<String, Object>> getDefensiveProjections(JSONArray defensiveJson) {
        Map<Integer, Map<String, Object>> defensiveMap = new HashMap<>();
        for (Object object : defensiveJson) {
            JSONObject playerObject = (JSONObject) object;
            int teamId = playerObject.getJSONObject("team").getInt("teamId");
            Map<String, Object> statMap = new HashMap<>();
            collectProjections(defensiveMap, playerObject, teamId, statMap);
        }
        return defensiveMap;
    }

    private void collectProjections(Map<Integer, Map<String, Object>> defensiveMap, JSONObject playerObject,
                                    int teamId, Map<String, Object> statMap) {
        double dkProjection = Double.parseDouble(playerObject.getJSONArray("fantasyProjections")
                .getJSONObject(0).getString("points"));
        double fdProjection = Double.parseDouble(playerObject.getJSONArray("fantasyProjections")
                .getJSONObject(1).getString("points"));
        statMap.put("dkProjection", dkProjection);
        statMap.put("fdProjection", fdProjection);
        defensiveMap.put(teamId, statMap);
    }


}
