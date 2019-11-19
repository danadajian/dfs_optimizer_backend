package api;

public class DataCollector implements ApiClient {
    private ApiCaller apiCaller;

    public DataCollector(ApiCaller apiCaller) {
        this.apiCaller = apiCaller;
    }

    @Override
    public String getEventsFromThisWeek(String sport) {
        return apiCaller.callStatsApi("stats/" + getSportName(sport) + "/" + sport + "/events/");
    }

    @Override
    public String getProjectionsFromThisWeek(String sport) {
        String baseCall = "stats/" + getSportName(sport) + "/" + sport + "/fantasyProjections/weekly";
        return apiCaller.callStatsApi(baseCall);
    }

    @Override
    public String getProjectionsFromEvent(String sport, int eventId) {
        String baseCall = "stats/" + getSportName(sport) + "/" + sport + "/fantasyProjections/" + eventId;
        return apiCaller.callStatsApi(baseCall);
    }

    @Override
    public String getFanduelData(String dateString) {
        return apiCaller.callFanduelApi(dateString);
    }

    @Override
    public String getDraftKingsData() {
        return apiCaller.callDraftKingsApi();
    }

    private String getSportName(String sport) {
        switch (sport) {
            case "nfl":
                return "football";
            case "mlb":
                return "baseball";
            case "nba":
                return "basketball";
            case "nhl":
                return "hockey";
            default:
                return "";
        }
    }
}
