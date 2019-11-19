package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.DraftKingsData;

import java.util.List;
import java.util.Map;

public class DraftKingsHandler {
    public List<Map<String, Object>> handleRequest(Map<String, String> input) {
        String sport = input.get("sport");
        DataCollector dataCollector = new DataCollector(new ApiCaller());
        DraftKingsData draftKingsData = new DraftKingsData(dataCollector, sport);
        return draftKingsData.getAllContestData();
    }
}
