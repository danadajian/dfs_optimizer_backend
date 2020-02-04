package handler;

import api.ApiCaller;
import api.DataCollector;
import collect.dfs.Fanduel;
import util.S3Upload;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FanduelHandler {
    private DataCollector dataCollector = new DataCollector(new ApiCaller());
    private Fanduel fanduel = new Fanduel(dataCollector);
    private S3Upload s3Upload = new S3Upload();

    public List<Map<String, Object>> handleRequest(Map<String, String> input) {
        String date = input.get("date");
        String invocationType = input.getOrDefault("invocationType", "web");
        List<Map<String, Object>> result = fanduel.getAllContestData(date);
        if (invocationType.equals("pipeline")) {
            s3Upload.uploadToS3("fanduelData.json", result);
            return new ArrayList<>();
        } else
            return result;
    }
}
