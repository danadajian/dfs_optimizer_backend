package collect;

import api.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.util.*;

public class FanduelData {
    private final List<String> supportedSports = Arrays.asList("NFL", "MLB", "NBA", "NHL");
    private final List<String> supportedContests = Arrays.asList("Thu-Mon", "Main", "Sun-Mon");
    private ApiClient apiClient;
    private String dateString;

    public FanduelData(ApiClient apiClient, String dateString) {
        this.apiClient = apiClient;
        this.dateString = dateString;
    }

    public List<Map<String, Object>> getAllContestData() {
        List<Map<String, Object>> allContestInfo = new ArrayList<>();
        getValidContests().forEach((JSONObject event) -> {
            Map<String, Object> contestMap = new HashMap<>();
            contestMap.put("site", "fd");
            contestMap.put("sport", event.get("sport"));
            contestMap.put("contest", event.getJSONObject("game").getString("label"));
            JSONArray playerPool = event.getJSONArray("player");
            Map<Integer, Map<String, Object>> playerMap = new HashMap<>();
            for (Object object : playerPool) {
                JSONObject playerObject = (JSONObject) object;
                    Map<String, Object> infoMap = new HashMap<>();
                    infoMap.put("position", playerObject.getString("position").equals("D") ?
                            "D/ST" : playerObject.getString("position"));
                    infoMap.put("salary", playerObject.getInt("salary"));
                    playerMap.put(playerObject.getInt("statsid"), infoMap);
            }
            contestMap.put("players", playerMap);
            allContestInfo.add(contestMap);
        });
        return allContestInfo;
    }

    public List<JSONObject> getValidContests() {
        String apiResponse = apiClient.getFanduelData(dateString);
        List<JSONObject> validContests = new ArrayList<>();
        JSONArray contests = XML.toJSONObject(apiResponse).getJSONObject("data").getJSONArray("fixturelist");
        for (Object object : contests) {
            JSONObject contest = (JSONObject) object;
            String contestName = contest.getJSONObject("game").getString("label");
            if (supportedSports.contains(contest.getString("sport")) &&
                    (supportedContests.contains(contestName) ||
                            (contestName.split(" ").length == 3 && contestName.contains("@")))) {
                validContests.add(contest);
            }
        }
        return validContests;
    }
}
