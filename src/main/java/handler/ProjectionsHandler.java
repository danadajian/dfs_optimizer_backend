package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.stats.NFLProjections;
import collect.stats.NHLProjections;
import collect.stats.StandardProjections;
import util.AWSClient;

import java.util.HashMap;
import java.util.Map;

public class ProjectionsHandler {
    private DataCollector dataCollector = new DataCollector(new ApiCaller());
    private StandardProjections standardProjectionsData = new StandardProjections(dataCollector);
    private NFLProjections nflProjections = new NFLProjections(dataCollector);
    private NHLProjections nhlProjections = new NHLProjections(dataCollector);
    private AWSClient AWSClient = new AWSClient();

    public Map<Integer, Map<String, Object>> handleRequest(Map<String, String> input) {
        String sport = input.get("sport");
        String invocationType = input.getOrDefault("invocationType", "web");
        Map<Integer, Map<String, Object>> result =
                sport.equals("nfl") ? nflProjections.getProjectionsData() :
                        (sport.equals("nhl")) ? nhlProjections.getProjectionsData() :
                                standardProjectionsData.getProjectionsData(sport);
        if (invocationType.equals("pipeline")) {
            AWSClient.uploadToS3(sport + "ProjectionsData.json", result);
            return new HashMap<>();
        }
        else
            return result;
    }
}
