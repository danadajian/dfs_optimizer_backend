package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.ProjectionsData;

import java.util.Map;

public class ProjectionsHandler {
    public Map<Integer, Map<String, Object>> handleRequest(Map<String, String> input) {
        String sport = input.get("sport");
        DataCollector dataCollector = new DataCollector(new ApiCaller());
        ProjectionsData projectionsData = new ProjectionsData(dataCollector, sport);
        if (sport.equals("nfl")) {
            return projectionsData.getNFLFantasyProjections();
        } else if (sport.equals("nhl")) {
            return projectionsData.getNHLFantasyProjections();
        } else {
            return projectionsData.getStandardFantasyProjections();
        }
    }
}
