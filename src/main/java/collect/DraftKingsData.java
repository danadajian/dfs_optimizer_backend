package collect;

import api.ApiClient;
import util.DateOperations;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class DraftKingsData extends DfsData {
    private final List<String> supportedGameTypes = Arrays.asList("Classic", "Showdown Captain Mode", "Showdown");
    private final List<String> supportedContests = Arrays.asList(" (Thu-Mon)", " (Sun-Mon)", " (Thu)", " (Sat)");
    private ApiClient apiClient;

    public DraftKingsData(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public List<Map<String, Object>> getAllContestData(String sport) {
        List<Map<String, Object>> allContestInfo = new ArrayList<>();
        getValidContests(sport).forEach((JSONObject event) -> {
            Map<String, Object> contestMap = new HashMap<>();
            String contest;
            String gameDate = event.getJSONArray("competitions").getJSONObject(0).getString("startTime");
            String easternTime = new DateOperations().getEasternTime(gameDate, "UTC", "MM/dd");
            if (event.has("suffix")) {
                String contestName = event.getString("suffix").substring(2, event.getString("suffix").length() - 1);
                contest = contestName + " (" + easternTime + ")";
            } else {
                contest = "Main (" + easternTime + ")";
            }
            contestMap.put("contest", contest);
            JSONArray playerPool = event.getJSONArray("draftPool");
            List<Map<String, Object>> playerList = new ArrayList<>();
            for (Object object : playerPool) {
                JSONObject playerObject = (JSONObject) object;
                if (!playerObject.get("rosterSlots").toString().equals("[\"CPT\"]")) {
                    Map<String, Object> infoMap = new HashMap<>();
                    infoMap.put("playerId", playerObject.getInt("playerId"));
                    infoMap.put("position", playerObject.getString("position"));
                    infoMap.put("salary", playerObject.getInt("salary"));
                    playerList.add(infoMap);
                }
            }
            contestMap.put("players", playerList);
            allContestInfo.add(contestMap);
        });
        return allContestInfo;
    }

    public List<JSONObject> getValidContests(String sport) {
        String apiResponse = apiClient.getDraftKingsData(sport);
        List<JSONObject> validContests = new ArrayList<>();
        JSONArray contests = new JSONObject(apiResponse).getJSONArray("draftPool");
        for (Object object : contests) {
            JSONObject event = (JSONObject) object;
            if (supportedGameTypes.contains(event.getString("gameType")) &&
                    (!event.has("suffix") ||
                            supportedContests.contains(event.getString("suffix")) ||
                            event.getString("gameType").contains("Showdown")) &&
                    event.getJSONArray("draftPool").length() > 0) {
                validContests.add(event);
            }
        }
        return validContests;
    }
}
