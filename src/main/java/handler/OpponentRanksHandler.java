package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.OpponentRanksData;

import java.util.Map;

public class OpponentRanksHandler {
    public Map<String, Map<String, Integer>> handleRequest(Map<String, String> input) {
        DataCollector dataCollector = new DataCollector(new ApiCaller());
        OpponentRanksData opponentRanksData = new OpponentRanksData(dataCollector);
        return opponentRanksData.getNFLOpponentRanks();
    }
}
