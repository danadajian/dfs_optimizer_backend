package collect;

import api.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class WrapStatsData {
    private ApiClient apiClient;
    private Map<Integer, String> homeOrAwayMap;

    public WrapStatsData(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public Map<Integer, Map<String, Object>> getFantasyProjections() {
        String apiResponse = apiClient.getProjectionsFromThisWeek();
        JSONObject projectionsJson = new JSONObject(apiResponse).getJSONArray("apiResults").getJSONObject(0)
                .getJSONObject("league").getJSONObject("season").getJSONArray("eventType").getJSONObject(0)
                .getJSONObject("fantasyProjections");
        homeOrAwayMap = getHomeOrAwayMap();
        JSONArray offensiveJson = projectionsJson.getJSONArray("offensiveProjections");
        JSONArray defensiveJson = projectionsJson.getJSONArray("defensiveProjections");
        Map<Integer, Map<String, Object>> resultMap = new HashMap<>();
        resultMap.putAll(getOffensiveProjections(offensiveJson));
        resultMap.putAll(getDefensiveProjections(defensiveJson));
        return resultMap;
    }

    public Map<Integer, String> getHomeOrAwayMap() {
        String apiResponse = apiClient.getEventsFromThisWeek();
        JSONArray eventsArray = new JSONObject(apiResponse).getJSONArray("apiResults").getJSONObject(0)
                .getJSONObject("league").getJSONObject("season").getJSONArray("eventType").getJSONObject(0)
                .getJSONArray("events");
        Map<Integer, String> homeOrAwayMap = new HashMap<>();
        for (Object eventObject : eventsArray) {
            JSONArray teamsArray = ((JSONObject) eventObject).getJSONArray("teams");
            int homeTeamId = teamsArray.getJSONObject(0).getInt("teamId");
            int awayTeamId = teamsArray.getJSONObject(1).getInt("teamId");
            String homeOppString = teamsArray.getJSONObject(0).getString("abbreviation");
            String awayOppString = teamsArray.getJSONObject(1).getString("abbreviation");
            homeOrAwayMap.put(homeTeamId, "v. " + awayOppString);
            homeOrAwayMap.put(awayTeamId, "@ " + homeOppString);
        }
        return homeOrAwayMap;
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

    private void addInfoToPlayerMap(Map<Integer, Map<String, Object>> defensiveMap, JSONObject playerObject,
                                    int teamId, Map<String, Object> statMap) {
        statMap.put("team", playerObject.getJSONObject("team").getString("abbreviation"));
        statMap.put("opponent", homeOrAwayMap.get(playerObject.getJSONObject("team").getInt("teamId")));
        statMap.put("gameDate", getEasternTime(playerObject.getJSONObject("gameDate")));
        statMap.put("dkProjection",  Double.parseDouble(playerObject.getJSONArray("fantasyProjections")
                .getJSONObject(0).getString("points")));
        statMap.put("fdProjection", Double.parseDouble(playerObject.getJSONArray("fantasyProjections")
                .getJSONObject(1).getString("points")));
        defensiveMap.put(teamId, statMap);
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
