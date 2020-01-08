package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.misc.Injuries;

import java.util.Map;

public class InjuryHandler {
    private DataCollector dataCollector = new DataCollector(new ApiCaller());
    Injuries injuries = new Injuries(dataCollector);

    public Map<String, String> handleRequest(Map<String, String> input) {
        String sport = input.get("sport");
        return sport.equals("nfl") ? injuries.getNFLInjuryData() : injuries.getStandardInjuryData(sport);
    }
}
