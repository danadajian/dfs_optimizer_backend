package collect;

import api.ApiCaller;
import api.DataCollector;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class AggregateDataIntegrationTest {

    private ApiCaller apiCaller = new ApiCaller();
    private DataCollector dataCollector = new DataCollector(apiCaller);
    private AggregateData aggregateData = new AggregateData(dataCollector);

    @Test
    void shouldGetDfsPlayerData() {
        List<Map<String, Object>> result = aggregateData.getDfsPlayerData();
    }

}
