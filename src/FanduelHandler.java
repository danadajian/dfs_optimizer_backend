import api.ApiCaller;
import api.DataCollector;
import collect.WrapFanduelData;

import java.util.List;
import java.util.Map;

public class FanduelHandler {
    public List<Map<String, Object>> handleRequest(Map<String,String> input) {
        String dateString = input.get("date");
        DataCollector dataCollector = new DataCollector(new ApiCaller());
        WrapFanduelData wrapFanduelData = new WrapFanduelData(dataCollector, dateString);
        return wrapFanduelData.getAllContestData();
    }
}
