package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.FanduelData;

import java.util.List;
import java.util.Map;

public class FanduelHandler {
    public List<Map<String, Object>> handleRequest(Map<String, String> input) {
        String dateString = input.get("date");
        DataCollector dataCollector = new DataCollector(new ApiCaller());
        FanduelData fanduelData = new FanduelData(dataCollector, dateString);
        return fanduelData.getAllContestData();
    }
}
