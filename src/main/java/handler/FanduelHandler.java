package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.dfs.Fanduel;
import util.AWSClient;
import util.DateOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FanduelHandler {
    private DataCollector dataCollector = new DataCollector(new ApiCaller());
    private Fanduel fanduel = new Fanduel(dataCollector);
    private DateOperations dateOperations = new DateOperations();
    private AWSClient AWSClient = new AWSClient();

    public List<Map<String, Object>> handleRequest(Map<String, String> input) {
        String invocationType = input.getOrDefault("invocationType", "web");
        String date = input.getOrDefault("date", dateOperations.getTodaysDateString());
        List<Map<String, Object>> result = fanduel.getAllContestData(date);
        if (invocationType.equals("pipeline")) {
            AWSClient.uploadToS3("fanduelData.json", result);
            return new ArrayList<>();
        } else
            return result;
    }
}
