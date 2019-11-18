package collect;

import api.ApiClient;

import java.util.Map;

public class DailyProjectionsData {
    private ApiClient apiClient;
    private String sport;

    public DailyProjectionsData(ApiClient apiClient, String sport) {
        this.apiClient = apiClient;
        this.sport = sport;
    }

    public Map<Integer, Map<String, Object>> getFantasyProjections() {
        return null;
    }
}
