package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.ProjectionsData;

import java.util.Map;

public class ProjectionsHandler {
    private DataCollector dataCollector = new DataCollector(new ApiCaller());
    private ProjectionsData projectionsData = new ProjectionsData(dataCollector);

    public Map<Integer, Map<String, Object>> handleRequest(Map<String, String> input) {
        String sport = input.get("sport");
        if (sport.equals("nfl")) {
            return projectionsData.getNFLFantasyProjections();
        } else if (sport.equals("nhl")) {
            return projectionsData.getNHLFantasyProjections();
        } else {
            return projectionsData.getStandardFantasyProjections(sport);
        }
    }
}
