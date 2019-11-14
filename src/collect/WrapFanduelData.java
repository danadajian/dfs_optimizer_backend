package collect;

import api.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.util.*;

class WrapFanduelData {
    private final List<String> supportedSports = Arrays.asList("NFL", "MLB", "NBA", "NHL");
    private final List<String> supportedContests = Arrays.asList("Thu-Mon", "Main", "Sun-Mon");
    private ApiClient apiClient;

    WrapFanduelData(ApiClient apiClient, String sport, String contest) {
        this.apiClient = apiClient;
    }

//    Map<Integer, Map<String, Object>> getAllContestData() {
//        JSONArray playerPool = getFixtureLists().getJSONArray("player");
//        Map<Integer, Map<String, Object>> playerMap = new HashMap<>();
//        for (Object object : playerPool) {
//            JSONObject playerObject = (JSONObject) object;
//            int playerId = playerObject.getInt("statsid");
//            Map<String, Object> infoMap = new HashMap<>();
//            infoMap.put("position", playerObject.getString("position"));
//            infoMap.put("salary", playerObject.getInt("salary"));
//            playerMap.put(playerId, infoMap);
//        }
//        return playerMap;
//    }

    List<JSONObject> getvalidContests() {
        String apiResponse = apiClient.getFanduelData();
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
