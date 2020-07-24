package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.stats.MLBProjections;
import collect.stats.NFLProjections;
import collect.stats.NHLProjections;
import collect.stats.NBAProjections;
import util.AWSClient;

import java.util.HashMap;
import java.util.Map;

public class ProjectionsHandler {
    private DataCollector dataCollector = new DataCollector(new ApiCaller());
    private NBAProjections nbaProjections = new NBAProjections(dataCollector);
    private NFLProjections nflProjections = new NFLProjections(dataCollector);
    private NHLProjections nhlProjections = new NHLProjections(dataCollector);
    private MLBProjections mlbProjections = new MLBProjections(dataCollector);
    private AWSClient AWSClient = new AWSClient();

    public Map<String, Object> handleRequest(Map<String, String> input) {
        String sport = input.get("sport");
        String invocationType = input.getOrDefault("invocationType", "web");
        Map<Integer, Map<String, Object>> result =
                (sport.equals("mlb")) ? mlbProjections.getProjectionsData() :
                        sport.equals("nfl") ? nflProjections.getProjectionsData() :
                                (sport.equals("nhl")) ? nhlProjections.getProjectionsData() :
                                        nbaProjections.getProjectionsData();
        Map<String, Object> resultMap = new HashMap<>();
        if (invocationType.equals("pipeline")) {
            AWSClient.uploadToS3(sport + "ProjectionsData.json", result);
            resultMap.put("sport", sport);
        }
        else {
            resultMap.put("body", result);
        }
        return resultMap;
    }
}
