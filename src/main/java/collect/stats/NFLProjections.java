package collect.stats;

import api.ApiClient;
import collect.misc.Odds;
import collect.misc.Weather;
import org.json.JSONArray;
import org.json.JSONObject;
import util.DateOperations;

import java.util.HashMap;
import java.util.Map;

import static collect.stats.MapAdder.addMiscDataToMap;

public class NFLProjections extends Projections {
    private ApiClient apiClient;
    private String sport;
    private Map<Integer, Map<Object, Object>> eventData;
    private Map<Integer, Map<String, Number>> oddsData;
    private Map<Integer, Map<String, String>> weatherData;

    public NFLProjections(ApiClient apiClient) {
        this.apiClient = apiClient;
        this.sport = "nfl";
    }

    @Override
    public Map<Integer, Map<String, Object>> getProjectionsData() {
        Map<Integer, Map<String, Object>> projectionsData = new HashMap<>();
        eventData = new Events(apiClient, sport).getEventData();
        oddsData = new Odds(apiClient, sport).getOddsData();
        weatherData = new Weather(apiClient, sport).getWeatherData();
        String apiResponse = apiClient.getProjectionsFromThisWeek(sport);
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
        addMiscDataToMap(playerMap, oddsData, weatherData, playerObject, statMap, eventId, teamId, id);
    }
}
