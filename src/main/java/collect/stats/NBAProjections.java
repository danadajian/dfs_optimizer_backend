package collect.stats;

import api.ApiClient;
import collect.misc.Odds;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static collect.stats.MapAdder.collectProjectionsData;

public class NBAProjections extends Projections {
    private ApiClient apiClient;
    private String sport;

    public NBAProjections(ApiClient apiClient) {
        this.apiClient = apiClient;
        this.sport = "nba";
    }

    public Map<Integer, Map<String, Object>> getProjectionsData() {
        Map<Integer, Map<String, Object>> projectionsData = new HashMap<>();
        Map<Integer, Map<Object, Object>> eventData = new Events(apiClient, sport).getEventData();
        Set<Integer> eventIds = eventData.keySet();
        Map<Integer, Map<String, String>> participantsData = new Participants(apiClient, sport).getParticipantsData();
        Map<Integer, Map<String, Number>> oddsData = new Odds(apiClient, sport).getOddsData();
        for (int eventId : eventIds) {
            String apiResponse = apiClient.getProjectionsFromEvent(sport, eventId);
            if (apiResponse.length() > 0) {
                JSONObject projectionsJson = new JSONObject(apiResponse).getJSONArray("apiResults").getJSONObject(0)
                        .getJSONObject("league").getJSONObject("season").getJSONArray("eventType").getJSONObject(0)
                        .getJSONObject("fantasyProjections");
                for (Object object : projectionsJson.getJSONArray("teams")) {
                    int teamId = ((JSONObject) object).getInt("teamId");
                    JSONArray playerArray = ((JSONObject) object).getJSONArray("players");
                    collectProjectionsData(playerArray, projectionsData, participantsData, eventData, oddsData, eventId, teamId);
                }
            }
        }
        return projectionsData;
    }
}
