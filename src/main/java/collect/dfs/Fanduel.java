package collect.dfs;

import api.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.util.*;

public class Fanduel extends DFS {
    private ApiClient apiClient;

    public Fanduel(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public List<Map<String, Object>> getAllContestData(String date) {
        List<Map<String, Object>> allContestInfo = new ArrayList<>();
        getValidContests(date).forEach((JSONObject event) -> {
            Map<String, Object> contestMap = new HashMap<>();
            contestMap.put("sport", event.getString("sport"));
            String contest = event.getJSONObject("game").getString("label");
            contestMap.put("contest", contest);
            JSONArray playerPool = event.getJSONArray("player");
            List<Map<String, Object>> playerList = new ArrayList<>();
            for (Object object : playerPool) {
                JSONObject playerObject = (JSONObject) object;
                Map<String, Object> infoMap = new HashMap<>();
                if (playerObject.get("statsid").toString().length() > 0)
                    infoMap.put("playerId", playerObject.getInt("statsid"));
                infoMap.put("name", playerObject.getString("name"));
                infoMap.put("team", playerObject.getString("team"));
                infoMap.put("position", playerObject.getString("position"));
                infoMap.put("salary", playerObject.getInt("salary"));
                playerList.add(infoMap);
            }
            contestMap.put("players", playerList);
            allContestInfo.add(contestMap);
        });
        return allContestInfo;
    }

    public List<JSONObject> getValidContests(String date) {
        String apiResponse = apiClient.getFanduelData(date);
        List<JSONObject> validContests = new ArrayList<>();
        JSONObject xmlResponse = XML.toJSONObject(apiResponse);
        if (xmlResponse.has("data")) {
            JSONArray contests = xmlResponse.getJSONObject("data").getJSONArray("fixturelist");
            for (Object object : contests) {
                JSONObject contest = (JSONObject) object;
                validContests.add(contest);
            }
        }
        return validContests;
    }
}
