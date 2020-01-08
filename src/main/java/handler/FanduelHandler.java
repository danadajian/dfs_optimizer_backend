package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.dfs.Fanduel;

import java.util.List;
import java.util.Map;

public class FanduelHandler {
    private DataCollector dataCollector = new DataCollector(new ApiCaller());
    Fanduel fanduel = new Fanduel(dataCollector);

    public List<Map<String, Object>> handleRequest(Map<String, String> input) {
        String date = input.get("date");
        return fanduel.getAllContestData(date);
    }
}
