package api;

import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class CallApiTest {
    private String baseCall = "stats/football/nfl/teams/";

    @Test
    void shouldReturnData() {
        CallApi callApi = new CallApi();
        callApi.callStatsApi(baseCall, "");
        assertEquals(200, callApi.getStatusCode());
    }
    @Test
    void shouldReturnNoData() {
        CallApi callApi = new CallApi();
        callApi.callStatsApi(baseCall, "&season=2100");
        assertEquals(404, callApi.getStatusCode());
    }

    @Test
    void shouldReturnErrorCode() {
        CallApi callApi = new CallApi();
        callApi.callStatsApi(baseCall, "thisIsAMalformedUrl");
        assertEquals(403, callApi.getStatusCode());
    }

    @Test
    void shouldCallFanduelApi() {
        CallApi callApi = new CallApi();
        callApi.callFanduelApi(Calendar.THURSDAY);
        assertEquals(200, callApi.getStatusCode());
    }

    @Test
    void shouldCallDraftKingsApi() {
        CallApi callApi = new CallApi();
        callApi.callDraftKingsApi();
        assertEquals(200, callApi.getStatusCode());
    }
}