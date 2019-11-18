package api;

public interface ApiClient {
    String getEventsFromThisWeek();
    String getProjectionsFromThisWeek();
    String getFanduelData(String dateString);
    String getDraftKingsData();
}
