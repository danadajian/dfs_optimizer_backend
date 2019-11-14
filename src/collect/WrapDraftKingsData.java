package collect;

import api.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class WrapDraftKingsData {
    private final List<String> supportedSports = Arrays.asList("NFL", "MLB", "NBA", "NHL");
    private final List<String> supportedGameTypes = Arrays.asList("Classic", "Showdown Captain Mode");
    private final List<String> supportedContests = Arrays.asList(" (Thu-Mon)", " (Sun-Mon)");
    private ApiClient apiClient;

    WrapDraftKingsData(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    List<Map<String, Object>> getAllContestData() {
        List<Map<String, Object>> allContestInfo = new ArrayList<>();
        getValidContests().forEach((JSONObject event) -> {
            Map<String, Object> contestMap = new HashMap<>();
            contestMap.put("sport", event.get("sport"));
            contestMap.put("contest", event.getString("gameType") +
                    (event.getString("suffix") == null ? " Main" : event.getString("suffix")));
            JSONArray playerPool = event.getJSONArray("draftPool");
            Map<Integer, Map<String, Object>> playerMap = new HashMap<>();
            for (Object object : playerPool) {
                JSONObject playerObject = (JSONObject) object;
                if (!playerObject.get("rosterSlots").toString().equals("[\"CPT\"]")) {
                    Map<String, Object> infoMap = new HashMap<>();
                    infoMap.put("name", playerObject.getString("name"));
                    infoMap.put("position", playerObject.getString("position"));
                    infoMap.put("salary", playerObject.getInt("salary"));
                    playerMap.put(playerObject.getInt("playerId"), infoMap);
                }
            }
            contestMap.put("players", playerMap);
            allContestInfo.add(contestMap);
        });
        return allContestInfo;
    }

    List<JSONObject> getValidContests() {
        String apiResponse = apiClient.getDraftKingsData();
        List<JSONObject> validContests = new ArrayList<>();
        JSONArray contests = new JSONObject(apiResponse).getJSONArray("draftPool");
        for (Object object : contests) {
            JSONObject event = (JSONObject) object;
            if (supportedSports.contains(event.getString("sport")) &&
                    supportedGameTypes.contains(event.getString("gameType")) &&
                    (supportedContests.contains(event.getString("suffix")) ||
                            event.getString("suffix") == null ||
                            event.getString("gameType").equals("Showdown Captain Mode")) &&
                    event.getJSONArray("draftPool").length() > 0) {
                validContests.add(event);
            }
        }
        return validContests;
    }
}
