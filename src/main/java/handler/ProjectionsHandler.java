package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.stats.NFLProjections;
import collect.stats.NHLProjections;
import collect.stats.StandardProjections;

import java.util.Map;

public class ProjectionsHandler {
    private DataCollector dataCollector = new DataCollector(new ApiCaller());
    private StandardProjections standardProjectionsData = new StandardProjections(dataCollector);
    private NFLProjections nflProjections = new NFLProjections(dataCollector);
    private NHLProjections nhlProjections = new NHLProjections(dataCollector);

    public Map<Integer, Map<String, Object>> handleRequest(Map<String, String> input) {
        String sport = input.get("sport");
        if (sport.equals("nfl")) {
            return nflProjections.getProjectionsData();
        } else if (sport.equals("nhl")) {
            return nhlProjections.getProjectionsData();
        } else {
            return standardProjectionsData.getProjectionsData(sport);
        }
    }
}
