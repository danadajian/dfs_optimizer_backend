package api;

import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class ApiCallerTest {
    private String baseCall = "stats/football/nfl/events/";

    @Test
    void shouldReturnData() {
        ApiCaller apiCaller = new ApiCaller();
        apiCaller.callStatsApi(baseCall, "");
        assertEquals(200, apiCaller.getStatusCode());
    }

    @Test
    void shouldReturnNoData() {
        ApiCaller apiCaller = new ApiCaller();
        apiCaller.callStatsApi(baseCall, "&season=2100");
        assertEquals(404, apiCaller.getStatusCode());
    }

    @Test
    void shouldReturnErrorCode() {
        ApiCaller apiCaller = new ApiCaller();
        apiCaller.callStatsApi(baseCall, "thisIsAMalformedUrl");
        assertEquals(403, apiCaller.getStatusCode());
    }

    @Test
    void shouldCallFanduelApi() {
        ApiCaller apiCaller = new ApiCaller();
        apiCaller.callFanduelApi(Calendar.THURSDAY);
        assertEquals(200, apiCaller.getStatusCode());
    }

    @Test
    void shouldCallDraftKingsApi() {
        ApiCaller apiCaller = new ApiCaller();
        apiCaller.callDraftKingsApi();
        assertEquals(200, apiCaller.getStatusCode());
    }
}