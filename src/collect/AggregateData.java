package collect;

import api.ApiClient;

import java.util.*;

public class AggregateData {
    private ApiClient apiClient;
    private Map<Integer, Map<String, Object>> fantasyProjections;

    AggregateData(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public List<Map<String, Object>> getDfsPlayerData() {
        WrapFanduelData fanduelData = new WrapFanduelData(apiClient);
        WrapDraftKingsData draftKingsData = new WrapDraftKingsData(apiClient);
        WrapStatsData statsData = new WrapStatsData(apiClient);
        List<Map<String, Object>> fanduelContestData = fanduelData.getAllContestData();
        List<Map<String, Object>> draftKingsContestData = draftKingsData.getAllContestData();
        fantasyProjections = statsData.getFantasyProjections();
        List<Map<String, Object>> allContestAndPlayerData = new ArrayList<>();
        allContestAndPlayerData.addAll(combineData(fanduelContestData));
        allContestAndPlayerData.addAll(combineData(draftKingsContestData));
        return allContestAndPlayerData;
    }

    private List<Map<String, Object>> combineData(List<Map<String, Object>> contestData) {
        List<Map<String, Object>> allContestAndPlayerData = new ArrayList<>();
        for (Map<String, Object> contest : contestData) {
            String site = (String) contest.get("site");
            Map<String, Object> contestMap = new HashMap<>();
            Map<Integer, Map<String, Object>> finalPlayerMap = new HashMap<>();
            Map contestPlayers = (Map) contest.get("players");
            for (Object object : contestPlayers.keySet()) {
                int playerId = (int) object;
                if (fantasyProjections.get(playerId) != null) {
                    Map<String, Object> playerDataMap = new HashMap<>();
                    playerDataMap.put("name", fantasyProjections.get(playerId).get("name"));
                    playerDataMap.put("team", fantasyProjections.get(playerId).get("team"));
                    playerDataMap.put("opponent", fantasyProjections.get(playerId).get("opponent"));
                    playerDataMap.put("gameDate", fantasyProjections.get(playerId).get("gameDate"));
                    playerDataMap.put("projection", fantasyProjections.get(playerId).get(site + "Projection"));
                    playerDataMap.put("position", ((Map) contestPlayers.get(playerId)).get("position"));
                    playerDataMap.put("salary", ((Map) contestPlayers.get(playerId)).get("salary"));
                    finalPlayerMap.put(playerId, playerDataMap);
                }
            }
            contestMap.put("site", site);
            contestMap.put("sport", contest.get("sport"));
            contestMap.put("contest", contest.get("contest"));
            contestMap.put("players", finalPlayerMap);
            allContestAndPlayerData.add(contestMap);
        }
        return allContestAndPlayerData;
    }
}
