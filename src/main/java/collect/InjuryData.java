package collect;

import api.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.util.HashMap;
import java.util.Map;

public class InjuryData {
    private ApiClient apiClient;

    public InjuryData(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public Map<String, String> getNFLInjuryData() {
        Map<String, String> injuryData = new HashMap<>();
        String scrapedResponse = apiClient.getInjuryData("nfl");
        int startIndex = scrapedResponse.indexOf("<section class=\"Card\">");
        int endIndex = scrapedResponse.indexOf("</section></div></section>") + "</section></div></section>".length();
        String responseSubstring = scrapedResponse.substring(startIndex, endIndex);
        JSONArray teamArray = XML.toJSONObject(responseSubstring).getJSONObject("section").getJSONObject("div")
                .getJSONObject("section").getJSONArray("section");
        for (Object object : teamArray) {
            Object teamObject = ((JSONObject) object).getJSONArray("div").getJSONObject(1)
                    .getJSONObject("div").getJSONArray("div").getJSONObject(1).getJSONObject("table")
                    .getJSONObject("tbody").get("tr");
            JSONArray playerArray = teamObject instanceof JSONArray ?
                    (JSONArray) teamObject : new JSONArray("[" + teamObject + "]");
            for (Object player : playerArray) {
                String playerName = ((JSONObject) player).getJSONArray("td").getJSONObject(0)
                        .getJSONObject("a").getString("content");
                String status = ((JSONObject) player).getJSONArray("td").getJSONObject(3)
                        .getJSONObject("span").getString("content");
                injuryData.put(playerName, status);
            }
        }
        return injuryData;
    }

    public Map<String, String> getStandardInjuryData(String sport) {
        Map<String, String> injuryData = new HashMap<>();
        String scrapedResponse = apiClient.getInjuryData(sport);
        int startIndex = scrapedResponse.indexOf("<table");
        int endIndex = scrapedResponse.indexOf("</table>") + "</table>".length();
        String responseSubstring = scrapedResponse.substring(startIndex, endIndex);
        JSONArray playerArray = XML.toJSONObject(responseSubstring).getJSONObject("table").getJSONArray("tr");
        for (Object object : playerArray) {
            JSONObject jsonObject = (JSONObject) object;
            if (jsonObject.get("td") instanceof JSONArray &&
                    jsonObject.getJSONArray("td").get(0) instanceof JSONObject) {
                String playerName = ((JSONObject) object).getJSONArray("td").getJSONObject(0)
                        .getJSONObject("a").getString("content");
                String status = ((JSONObject) object).getJSONArray("td").getString(1);
                if (!injuryData.containsKey(playerName)) {
                    injuryData.put(playerName, status);
                }
            }
        }
        return injuryData;
    }
}
