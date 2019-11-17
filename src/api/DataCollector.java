package api;

public class DataCollector implements ApiClient {
    private ApiCaller apiCaller;

    public DataCollector(ApiCaller apiCaller) {
        this.apiCaller = apiCaller;
    }

    @Override
    public String getEventsFromThisWeek() {
        return apiCaller.callStatsApi("stats/football/nfl/events/", "");
    }

    @Override
    public String getProjectionsFromThisWeek() {
        return apiCaller.callStatsApi("stats/football/nfl/fantasyProjections/weekly/", "");
    }

    @Override
    public String getFanduelData(String dateString) {
        return apiCaller.callFanduelApi(dateString);
    }

    @Override
    public String getDraftKingsData() {
        return apiCaller.callDraftKingsApi();
    }

}
