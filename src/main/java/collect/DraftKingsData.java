package collect;

import api.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class DraftKingsData {
    private final List<String> supportedGameTypes = Arrays.asList("Classic", "Showdown Captain Mode", "Showdown");
    private final List<String> supportedContests = Arrays.asList(" (Thu-Mon)", " (Sun-Mon)");
    private ApiClient apiClient;
    private String sport;

    public DraftKingsData(ApiClient apiClient, String sport) {
        this.apiClient = apiClient;
        this.sport = sport;
    }

    public List<Map<String, Object>> getAllContestData() {
        List<Map<String, Object>> allContestInfo = new ArrayList<>();
        getValidContests().forEach((JSONObject event) -> {
            Map<String, Object> contestMap = new HashMap<>();
            String contest = event.has("suffix") ? event.getString("suffix")
                    .substring(2, event.getString("suffix").length() - 1) :
                    "Main ("
                            + event.getJSONArray("competitions").getJSONObject(0).getString("startTime")
                            .split("T")[0].substring(5)
                            + ")";
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

    public List<JSONObject> getValidContests() {
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
