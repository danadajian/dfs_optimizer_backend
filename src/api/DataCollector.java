package api;

import java.util.Calendar;

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

//    @Override
//    public String getWeather(String date) {
//        return apiCaller.callStatsApi("stats/football/nfl/weatherforecasts/", "");
//    }

    @Override
    public String getFanduelData() {
        return apiCaller.callFanduelApi();
    }

    @Override
    public String getDraftKingsData() {
        return apiCaller.callDraftKingsApi();
    }

}
