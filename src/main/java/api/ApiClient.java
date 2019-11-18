package api;

public interface ApiClient {
    String getEventsFromThisWeek(String sport);
    String getProjectionsFromThisWeek(String sport);
    String getProjectionsFromEvent(String sport, int eventId);
    String getFanduelData(String dateString);
    String getDraftKingsData();
}
