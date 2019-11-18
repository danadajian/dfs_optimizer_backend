package com.optimizer.api;

import api.ApiCaller;
import api.DateOperations;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiCallerTest {
    private String baseCall = "stats/football/nfl/events/";

    @Test
    void shouldReturnData() {
        ApiCaller apiCaller = new ApiCaller();
        apiCaller.callStatsApi(baseCall);
        assertEquals(200, apiCaller.getStatusCode());
    }

    @Test
    void shouldCallFanduelApi() {
        ApiCaller apiCaller = new ApiCaller();
        apiCaller.callFanduelApi(new DateOperations().getTodaysDateString());
        assertEquals(200, apiCaller.getStatusCode());
    }

    @Test
    void shouldCallDraftKingsApi() {
        ApiCaller apiCaller = new ApiCaller();
        apiCaller.callDraftKingsApi();
        assertEquals(200, apiCaller.getStatusCode());
    }
}