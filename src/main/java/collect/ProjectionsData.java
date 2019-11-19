package collect;

import api.ApiClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProjectionsData {
    private ApiClient apiClient;
    private String sport;
    private Map<Integer, Map<Object, Object>> eventData;

    public ProjectionsData(ApiClient apiClient, String sport) {
        this.apiClient = apiClient;
        this.sport = sport;
    }

    public Map<Integer, Map<String, Object>> getFantasyProjections() {
        Map<Integer, Map<String, Object>> projectionsData = new HashMap<>();
        eventData = getEventData();
        Set<Integer> eventIds = eventData.keySet();
        if (sport.equals("nfl")) {
            String apiResponse = apiClient.getProjectionsFromThisWeek(sport);
            JSONObject projectionsJson = new JSONObject(apiResponse).getJSONArray("apiResults").getJSONObject(0)
                    .getJSONObject("league").getJSONObject("season").getJSONArray("eventType").getJSONObject(0)
                    .getJSONObject("fantasyProjections");
            JSONArray offensiveJson = projectionsJson.getJSONArray("offensiveProjections");
            JSONArray defensiveJson = projectionsJson.getJSONArray("defensiveProjections");
            projectionsData.putAll(getOffensiveProjections(offensiveJson));
            projectionsData.putAll(getDefensiveProjections(defensiveJson));
        } else {
            Map<Integer, Map<String, String>> participantsData = getParticipantsData();
            for (int eventId : eventIds) {
                String apiResponse = apiClient.getProjectionsFromEvent(sport, eventId);
                JSONObject projectionsJson = new JSONObject(apiResponse).getJSONArray("apiResults").getJSONObject(0)
                        .getJSONObject("league").getJSONObject("season").getJSONArray("eventType").getJSONObject(0)
                        .getJSONObject("fantasyProjections");
                for (Object object : projectionsJson.getJSONArray("teams")) {
                    int teamId = ((JSONObject) object).getInt("teamId");
                    JSONArray playersArray = ((JSONObject) object).getJSONArray("players");
                    for (Object thing : playersArray) {
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
            }
        }
        return projectionsData;
    }

    public Map<Integer, Map<Object, Object>> getEventData() {
        String apiResponse = apiClient.getCurrentEvents(sport);
        JSONArray eventsArray = new JSONObject(apiResponse).getJSONArray("apiResults").getJSONObject(0)
                .getJSONObject("league").getJSONObject("season").getJSONArray("eventType").getJSONObject(0)
                .getJSONArray("events");
        Map<Integer, Map<Object, Object>> allEventData = new HashMap<>();
        for (Object object : eventsArray) {
            JSONObject eventObject = (JSONObject) object;
            int eventId = eventObject.getInt("eventId");
            String gameDate = getEasternTime(eventObject.getJSONArray("startDate").getJSONObject(1));
            JSONArray teamsArray = eventObject.getJSONArray("teams");
            int homeTeamId = teamsArray.getJSONObject(0).getInt("teamId");
            int awayTeamId = teamsArray.getJSONObject(1).getInt("teamId");
            String homeAbbrev = teamsArray.getJSONObject(0).getString("abbreviation");
            String awayAbbrev = teamsArray.getJSONObject(1).getString("abbreviation");
            Map<Object, Object> eventDataMap = new HashMap<>();
            eventDataMap.put("gameDate", gameDate);
            eventDataMap.put(homeTeamId, "v. " + awayAbbrev);
            eventDataMap.put(awayTeamId, "@ " + homeAbbrev);
            allEventData.put(eventId, eventDataMap);
        }
        return allEventData;
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
        statMap.put("gameDate", getEasternTime(playerObject.getJSONObject("gameDate")));
        addProjectionsToMap(playerMap, playerObject, statMap, id);
    }

    private void addProjectionsToMap(Map<Integer, Map<String, Object>> projectionsData, JSONObject playerObject, Map<String, Object> statMap, int playerId) {
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

    public String getEasternTime(JSONObject gameDate) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone(gameDate.getString("dateType")));
        try {
            String dateString = gameDate.getString("full");
            cal.setTime(sdf.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat format = new SimpleDateFormat("EEE h:mma z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("EST"));
        return format.format(cal.getTime());
    }
}
