package collect;

import api.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;
import util.DateOperations;

import java.util.HashMap;
import java.util.Map;

public class EventData {
    private ApiClient apiClient;
    private String sport;

    public EventData(ApiClient apiClient, String sport) {
        this.apiClient = apiClient;
        this.sport = sport;
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
            JSONObject dateObject = eventObject.getJSONArray("startDate").getJSONObject(1);
            String gameDate = new DateOperations().getEasternTime(dateObject.getString("full"),
                    dateObject.getString("dateType"), "EEE h:mma z");
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
}
