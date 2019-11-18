package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.DailyProjectionsData;
import collect.WeeklyProjectionsData;

import java.util.Map;

public class ProjectionsHandler {
    public Map<Integer, Map<String, Object>> handleRequest(Map<String, String> input) {
        DataCollector dataCollector = new DataCollector(new ApiCaller());
        String sport = input.get("sport");
        if (sport.equals("nfl")) {
            WeeklyProjectionsData weeklyProjectionsData = new WeeklyProjectionsData(dataCollector, sport);
            return weeklyProjectionsData.getFantasyProjections();
        } else {
            DailyProjectionsData dailyProjectionsData = new DailyProjectionsData(dataCollector, sport);
            return dailyProjectionsData.getFantasyProjections();
        }
    }
}
