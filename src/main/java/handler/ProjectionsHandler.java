package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.ProjectionsData;

import java.util.Map;

public class ProjectionsHandler {
    public Map<Integer, Map<String, Object>> handleRequest(Map<String, String> input) {
        DataCollector dataCollector = new DataCollector(new ApiCaller());
        String sport = input.get("sport");
        return new ProjectionsData(dataCollector, sport).getFantasyProjections();
    }
}
