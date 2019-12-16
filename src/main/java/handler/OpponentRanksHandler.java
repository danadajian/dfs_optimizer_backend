package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.OpponentRanksData;

import java.util.Map;

public class OpponentRanksHandler {
    private DataCollector dataCollector = new DataCollector(new ApiCaller());
    OpponentRanksData opponentRanksData = new OpponentRanksData(dataCollector);

    public Map<String, Map<String, Integer>> handleRequest(Map<String, String> input) {
        String sport = input.get("sport");
        return opponentRanksData.getOpponentRanks(sport);
    }
}
