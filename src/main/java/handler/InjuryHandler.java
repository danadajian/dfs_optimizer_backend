package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.InjuryData;

import java.util.Map;

public class InjuryHandler {
    private DataCollector dataCollector = new DataCollector(new ApiCaller());
    InjuryData injuryData = new InjuryData(dataCollector);

    public Map<String, String> handleRequest(Map<String, String> input) {
        String sport = input.get("sport");
        return sport.equals("nfl") ? injuryData.getNFLInjuryData() : injuryData.getStandardInjuryData(sport);
    }
}
