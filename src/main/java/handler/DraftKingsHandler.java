package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.WrapDraftKingsData;

import java.util.List;
import java.util.Map;

public class DraftKingsHandler {
    public List<Map<String, Object>> handleRequest(Map<String,String> input) {
        DataCollector dataCollector = new DataCollector(new ApiCaller());
        WrapDraftKingsData wrapDraftKingsData = new WrapDraftKingsData(dataCollector);
        return wrapDraftKingsData.getAllContestData();
    }
}
