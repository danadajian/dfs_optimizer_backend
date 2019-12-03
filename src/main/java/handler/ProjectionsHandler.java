package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.NFLProjectionsData;
import collect.StandardProjectionsData;

import java.util.Map;

public class ProjectionsHandler {
    public Map<Integer, Map<String, Object>> handleRequest(Map<String, String> input) {
        String sport = input.get("sport");
        DataCollector dataCollector = new DataCollector(new ApiCaller());
        if (sport.equals("nfl")) {
            NFLProjectionsData nflProjectionsData = new NFLProjectionsData(dataCollector);
            return nflProjectionsData.getFantasyProjections();
        } else {
            StandardProjectionsData standardProjectionsData = new StandardProjectionsData(dataCollector, sport);
            return standardProjectionsData.getFantasyProjections();
        }
    }
}
