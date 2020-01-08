package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.misc.OpponentRanks;

import java.util.Map;

public class OpponentRanksHandler {
    private DataCollector dataCollector = new DataCollector(new ApiCaller());
    OpponentRanks opponentRanks = new OpponentRanks(dataCollector);

    public Map<String, Map<String, Integer>> handleRequest(Map<String, String> input) {
        String sport = input.get("sport");
        return opponentRanks.getOpponentRanks(sport);
    }
}
