package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.dfs.DraftKings;

import java.util.List;
import java.util.Map;

public class DraftKingsHandler {
    DataCollector dataCollector = new DataCollector(new ApiCaller());
    DraftKings draftKings = new DraftKings(dataCollector);

    public List<Map<String, Object>> handleRequest(Map<String, String> input) {
        String sport = input.get("sport");
        return draftKings.getAllContestData(sport);
    }
}
