package collect.stats;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MapAdder {
    public static void collectProjectionsData(JSONArray playerArray, Map<Integer, Map<String, Object>> projectionsData,
                                              Map<Integer, Map<String, String>> participantsData,
                                              Map<Integer, Map<Object, Object>> eventData,
                                              Map<Integer, Map<String, Number>> oddsData,
                                              Map<Integer, Map<String, String>> weatherData, int eventId, int teamId) {
        for (Object thing : playerArray) {
            JSONObject playerObject = (JSONObject) thing;
            Map<String, Object> statMap = new HashMap<>();
            int playerId = playerObject.getInt("playerId");
            if (participantsData.containsKey(playerId)) {
                statMap.put("name", participantsData.get(playerId).get("name"));
                statMap.put("team", participantsData.get(playerId).get("team"));
                statMap.put("opponent", eventData.get(eventId).get(teamId));
                statMap.put("gameDate", eventData.get(eventId).get("gameDate"));
                addMiscDataToMap(projectionsData, oddsData, weatherData, playerObject, statMap, eventId, teamId, playerId);
            }
        }
    }

    public static void addMiscDataToMap(Map<Integer, Map<String, Object>> projectionsData,
                                        Map<Integer, Map<String, Number>> oddsData,
                                        Map<Integer, Map<String, String>> weatherData, JSONObject playerObject,
                                        Map<String, Object> statMap, int eventId, int teamId, int playerId) {
        Map<String, Number> defaultOddsMap = new HashMap<>();
        Map<String, Number> eventOddsData = oddsData.getOrDefault(eventId, defaultOddsMap);
        int spreadMultiplier = (int) eventOddsData.getOrDefault("favoriteTeamId", 0) == teamId ? 1 : -1;
        String spreadSign = (int) eventOddsData.getOrDefault("favoriteTeamId", 0) == teamId ? "" : "+";
        statMap.put("spread", spreadSign + (spreadMultiplier * (double) eventOddsData.getOrDefault("spread", 0.0)));
        statMap.put("overUnder", eventOddsData.getOrDefault("overUnder", 0));
        Map<String, String> defaultWeatherMap = new HashMap<>();
        if (!weatherData.isEmpty()) {
            Map<String, String> eventWeatherData = weatherData.getOrDefault(eventId, defaultWeatherMap);
            statMap.put("weather", eventWeatherData);
        }
        addProjectionsToMap(projectionsData, playerObject, statMap, playerId);
    }

    public static void addProjectionsToMap(Map<Integer, Map<String, Object>> projectionsData, JSONObject playerObject,
                                     Map<String, Object> statMap, int playerId) {
        try {
            statMap.put("DraftKingsProjection", 0);
            statMap.put("FanduelProjection", 0);
            JSONArray fantasyProjections = playerObject.getJSONArray("fantasyProjections");
            for (Object object : fantasyProjections) {
                JSONObject projectionObject = (JSONObject) object;
                String site = projectionObject.getString("name").contains("FanDuel") ? "Fanduel" :
                        projectionObject.getString("name").contains("DraftKings") ? "DraftKings": "";
                if (site.length() > 0)
                    statMap.put(site + "Projection", Double.parseDouble(projectionObject.getString("points")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        projectionsData.put(playerId, statMap);
    }
}
