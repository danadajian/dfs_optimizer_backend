package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.WrapStatsData;

import java.util.Map;

public class StatsHandler {
    public Map<Integer, Map<String, Object>> handleRequest(Map<String,String> input) {
        DataCollector dataCollector = new DataCollector(new ApiCaller());
        WrapStatsData wrapStatsData = new WrapStatsData(dataCollector);
        return wrapStatsData.getFantasyProjections();
    }
}
