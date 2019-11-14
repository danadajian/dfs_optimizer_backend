package api;

public interface ApiClient {
    String getEventsFromThisWeek();
    String getProjectionsFromThisWeek();
    // String getWeather(String date);
    String getFanduelData();
    String getDraftKingsData();
}
