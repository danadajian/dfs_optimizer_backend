package api;

import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static api.CallApi.*;
import static org.junit.jupiter.api.Assertions.*;

class CallApiTest {
    private String baseCall = "stats/football/nfl/teams/";

    @Test
    void shouldReturnData() {
        String result = callStatsApi(baseCall, "");
        assertTrue(result.startsWith("{\"status\":\"OK\""));
    }
    @Test
    void shouldReturnNoData() {
        String result = "";
        try {
            result = callStatsApi(baseCall, "&season=2100");
        } catch (MakeHttpRequest.ApiException e) {
            assertEquals(404, e.getErrorCode());
        }
        assertEquals("", result);
    }

    @Test
    void shouldReturnErrorCode() {
        String result = "";
        try {
            result = callStatsApi(baseCall, "thisIsAMalformedUrl");
        } catch (MakeHttpRequest.ApiException e) {
            assertEquals(403, e.getErrorCode());
        }
        assertEquals("", result);
    }

    @Test
    void shouldCallFanduelApi() {
        String result = callFanduelApi(Calendar.THURSDAY);
        assertTrue(result.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><data>\t<fixturelist>"));
    }

    @Test
    void shouldCallDraftKingsApi() {
        String result = callDraftKingsApi();
        assertTrue(result.startsWith("{\"draftPool\":"));
    }
}