package api;

public interface ApiClient {
    String getCurrentEvents(String sport);
    String getProjectionsFromThisWeek(String sport);
    String getProjectionsFromEvent(String sport, int eventId);
    String getParticipants(String sport);
    String getFanduelData(String dateString);
    String getDraftKingsData(String sport);
}
