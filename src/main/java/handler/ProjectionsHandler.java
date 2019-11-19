package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.ProjectionsData;

import java.util.Map;

public class ProjectionsHandler {
    public Map<Integer, Map<String, Object>> handleRequest(Map<String, String> input) {
        DataCollector dataCollector = new DataCollector(new ApiCaller());
        ProjectionsData projectionsData = new ProjectionsData(dataCollector, input.get("sport"));
        return projectionsData.getFantasyProjections();
    }
}
