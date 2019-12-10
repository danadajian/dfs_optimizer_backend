package collect;

import api.ApiClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.DateOperations;

import java.util.*;

public class ProjectionsData {
    private ApiClient apiClient;
    private String sport;
    private Map<Integer, Map<Object, Object>> eventData;
    private Map<Integer, Map<String, String>> participantsData;
    private Map<Integer, Map<String, Number>> oddsData;

    public ProjectionsData(ApiClient apiClient, String sport) {
        this.apiClient = apiClient;
        this.sport = sport;
    }

    public Map<Integer, Map<String, Object>> getStandardFantasyProjections() {
        Map<Integer, Map<String, Object>> projectionsData = new HashMap<>();
        eventData = new EventData(apiClient, sport).getEventData();
        Set<Integer> eventIds = eventData.keySet();
        participantsData = new ParticipantsData(apiClient, sport).getParticipantsData();
        oddsData = new OddsData(apiClient, sport).getOddsData();
        for (int eventId : eventIds) {
            String apiResponse = apiClient.getProjectionsFromEvent(sport, eventId);
            if (apiResponse != null) {
                JSONObject projectionsJson = new JSONObject(apiResponse).getJSONArray("apiResults").getJSONObject(0)
                        .getJSONObject("league").getJSONObject("season").getJSONArray("eventType").getJSONObject(0)
                        .getJSONObject("fantasyProjections");
                for (Object object : projectionsJson.getJSONArray("teams")) {
                    int teamId = ((JSONObject) object).getInt("teamId");
                    JSONArray playerArray = ((JSONObject) object).getJSONArray("players");
                    collectProjectionsData(playerArray, projectionsData, eventId, teamId);
                }
            }
        }
        return projectionsData;
    }

    public Map<Integer, Map<String, Object>> getNHLFantasyProjections() {
        Map<Integer, Map<String, Object>> projectionsData = new HashMap<>();
        eventData = new EventData(apiClient, "nhl").getEventData();
        Set<Integer> eventIds = eventData.keySet();
        participantsData = new ParticipantsData(apiClient, "nhl").getParticipantsData();
        oddsData = new OddsData(apiClient, sport).getOddsData();
        for (int eventId : eventIds) {
            String apiResponse = apiClient.getProjectionsFromEvent("nhl", eventId);
            if (apiResponse != null) {
                JSONObject projectionsJson = new JSONObject(apiResponse).getJSONArray("apiResults").getJSONObject(0)
                        .getJSONObject("league").getJSONObject("season").getJSONArray("eventType").getJSONObject(0)
                        .getJSONObject("fantasyProjections");
                for (Object object : projectionsJson.getJSONArray("teams")) {
                    int teamId = ((JSONObject) object).getInt("teamId");
                    JSONArray playerArray = ((JSONObject) object).getJSONArray("skaters");
                    collectProjectionsData(playerArray, projectionsData, eventId, teamId);
                    JSONArray goalieArray = ((JSONObject) object).getJSONArray("goalies");
                    collectProjectionsData(goalieArray, projectionsData, eventId, teamId);
                }
            }
        }
        return projectionsData;
    }

    public Map<Integer, Map<String, Object>> getNFLFantasyProjections() {
        Map<Integer, Map<String, Object>> projectionsData = new HashMap<>();
        eventData = new EventData(apiClient, "nfl").getEventData();
        oddsData = new OddsData(apiClient, sport).getOddsData();
        String apiResponse = apiClient.getProjectionsFromThisWeek("nfl");
        if (apiResponse.length() > 0) {
            JSONObject projectionsJson = new JSONObject(apiResponse).getJSONArray("apiResults").getJSONObject(0)
                    .getJSONObject("league").getJSONObject("season").getJSONArray("eventType").getJSONObject(0)
                    .getJSONObject("fantasyProjections");
            JSONArray offensiveJson = projectionsJson.getJSONArray("offensiveProjections");
            JSONArray defensiveJson = projectionsJson.getJSONArray("defensiveProjections");
            projectionsData.putAll(getOffensiveProjections(offensiveJson));
            projectionsData.putAll(getDefensiveProjections(defensiveJson));
        }
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
        int eventId = playerObject.getInt("eventId");
        int teamId = playerObject.getJSONObject("team").getInt("teamId");
        statMap.put("team", playerObject.getJSONObject("team").getString("abbreviation"));
        statMap.put("opponent", eventData.get(eventId).get(teamId));
        statMap.put("gameDate",
                new DateOperations().getEasternTime(playerObject.getJSONObject("gameDate").getString("full"),
                        playerObject.getJSONObject("gameDate").getString("dateType"), "EEE h:mma z"));
        addOddsDataToMap(playerMap, playerObject, statMap, eventId, teamId, id);
    }

    private void collectProjectionsData(JSONArray playerArray,
                                        Map<Integer, Map<String, Object>> projectionsData, int eventId, int teamId) {
        for (Object thing : playerArray) {
            JSONObject playerObject = (JSONObject) thing;
            Map<String, Object> statMap = new HashMap<>();
            int playerId = playerObject.getInt("playerId");
            if (participantsData.keySet().contains(playerId)) {
                statMap.put("name", participantsData.get(playerId).get("name"));
                statMap.put("team", participantsData.get(playerId).get("team"));
                statMap.put("opponent", eventData.get(eventId).get(teamId));
                statMap.put("gameDate", eventData.get(eventId).get("gameDate"));
                addOddsDataToMap(projectionsData, playerObject, statMap, eventId, teamId, playerId);
            }
        }
    }

    private void addOddsDataToMap(Map<Integer, Map<String, Object>> projectionsData,
                                  JSONObject playerObject, Map<String, Object> statMap,
                                  int eventId, int teamId, int playerId) {
        int spreadMultiplier = (int) oddsData.get(eventId).get("favoriteTeamId") == teamId ? 1 : -1;
        String spreadSign = (int) oddsData.get(eventId).get("favoriteTeamId") == teamId ? "" : "+";
        statMap.put("spread", spreadSign + (spreadMultiplier * (double) oddsData.get(eventId).get("spread")));
        statMap.put("overUnder", oddsData.get(eventId).get("overUnder"));
        addProjectionsToMap(projectionsData, playerObject, statMap, playerId);
    }

    private void addProjectionsToMap(Map<Integer, Map<String, Object>> projectionsData, JSONObject playerObject,
                                    Map<String, Object> statMap, int playerId) {
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
