package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.InjuryData;

import java.util.Map;

public class InjuryHandler {
    public Map<String, String> handleRequest(Map<String, String> input) {
        String sport = input.get("sport");
        DataCollector dataCollector = new DataCollector(new ApiCaller());
        InjuryData injuryData = new InjuryData(dataCollector, sport);
        if (sport.equals("nfl"))
            return injuryData.getNFLInjuryData();
        else
            return injuryData.getStandardInjuryData();
    }
}
