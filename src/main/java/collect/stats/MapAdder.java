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
                                              Map<Integer, Map<String, Number>> oddsData, int eventId, int teamId) {
        for (Object thing : playerArray) {
            JSONObject playerObject = (JSONObject) thing;
            Map<String, Object> statMap = new HashMap<>();
            int playerId = playerObject.getInt("playerId");
            if (participantsData.containsKey(playerId)) {
                statMap.put("name", participantsData.get(playerId).get("name"));
                statMap.put("team", participantsData.get(playerId).get("team"));
                statMap.put("opponent", eventData.get(eventId).get(teamId));
                statMap.put("gameDate", eventData.get(eventId).get("gameDate"));
                addOddsDataToMap(projectionsData, oddsData, playerObject, statMap, eventId, teamId, playerId);
            }
        }
    }

    public static void addOddsDataToMap(Map<Integer, Map<String, Object>> projectionsData,
                                        Map<Integer, Map<String, Number>> oddsData, JSONObject playerObject,
                                        Map<String, Object> statMap, int eventId, int teamId, int playerId) {
        int spreadMultiplier = (int) oddsData.get(eventId).get("favoriteTeamId") == teamId ? 1 : -1;
        String spreadSign = (int) oddsData.get(eventId).get("favoriteTeamId") == teamId ? "" : "+";
        statMap.put("spread", spreadSign + (spreadMultiplier * (double) oddsData.get(eventId).get("spread")));
        statMap.put("overUnder", oddsData.get(eventId).get("overUnder"));
        addProjectionsToMap(projectionsData, playerObject, statMap, playerId);
    }

    public static void addProjectionsToMap(Map<Integer, Map<String, Object>> projectionsData, JSONObject playerObject,
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
