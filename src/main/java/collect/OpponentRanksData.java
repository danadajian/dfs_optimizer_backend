package collect;

import api.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.util.HashMap;
import java.util.Map;

public class OpponentRanksData {
    private ApiClient apiClient;

    public OpponentRanksData(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public Map<String, Map<String, Integer>> getNFLOpponentRanks() {
        Map<String, Map<String, Integer>> opponentRanksMap = new HashMap<>();
        String scrapedResponse = apiClient.getFantasyProsData();
        int startIndex = scrapedResponse.indexOf("<tbody>");
        int endIndex = scrapedResponse.indexOf("</tbody>") + "</tbody>".length();
        String responseSubstring = scrapedResponse.substring(startIndex, endIndex);
        String adjustedResponse = responseSubstring.replace("</td><tr>", "</td></tr>");
        JSONObject rankingsJson = XML.toJSONObject(adjustedResponse).getJSONObject("tbody");
        JSONArray rankingsArray = rankingsJson.getJSONArray("tr");
        for (Object object : rankingsArray) {
            Map<String, Integer> rankMap = new HashMap<>();
            JSONArray teamArray = ((JSONObject) object).getJSONArray("td");
            String teamName = teamArray.getJSONObject(0).getJSONObject("a").getString("content");
            int qbRank = getRank(teamArray, 1);
            int rbRank = getRank(teamArray, 3);
            int wrRank = getRank(teamArray, 5);
            int teRank = getRank(teamArray, 7);
            int kRank = getRank(teamArray, 9);
            int dstRank = getRank(teamArray, 11);
            rankMap.put("QB", 33 - qbRank);
            rankMap.put("RB", 33 - rbRank);
            rankMap.put("WR", 33 - wrRank);
            rankMap.put("TE", 33 -  teRank);
            rankMap.put("K", 33 -  kRank);
            rankMap.put("D/ST", 33 - dstRank);
            opponentRanksMap.put(teamName, rankMap);
        }
        return opponentRanksMap;
    }

    private int getRank(JSONArray teamArray, int index) {
        return teamArray.getJSONObject(index).get("span") instanceof JSONObject ?
                teamArray.getJSONObject(index).getJSONObject("span").getInt("content") :
                teamArray.getJSONObject(index).getInt("span");
    }
}
