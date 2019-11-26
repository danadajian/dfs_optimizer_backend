package collect;

import api.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.util.*;

public class FanduelData {
    private final List<String> supportedSports = Arrays.asList("NFL", "MLB", "NBA", "NHL");
    private final List<String> supportedContests = Arrays.asList("Thu Only", "Thu-Mon", "Main", "Sun-Mon");
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
            contestMap.put("sport", event.get("sport"));
            String contest = event.getJSONObject("game").getString("label");
            contestMap.put("contest", contest);
            JSONArray playerPool = event.getJSONArray("player");
            List<Map<String, Object>> playerList = new ArrayList<>();
            for (Object object : playerPool) {
                JSONObject playerObject = (JSONObject) object;
                if (playerObject.get("statsid").toString().length() > 0) {
                    Map<String, Object> infoMap = new HashMap<>();
                    infoMap.put("playerId", playerObject.getInt("statsid"));
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
