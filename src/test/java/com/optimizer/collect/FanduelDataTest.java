package com.optimizer.collect;

import api.ApiClient;
import collect.FanduelData;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FanduelDataTest implements MockResponses {

    private ApiClient mockApi = mock(ApiClient.class);
    private FanduelData fanduelData = new FanduelData(mockApi, "testDateString");

    @BeforeEach
    void setUp() {
        when(mockApi.getFanduelData(anyString())).thenReturn(fakeFanduelData);
    }

    @Test
    void shouldGetValidContests() {
        List<JSONObject> result = fanduelData.getValidContests();
        verify(mockApi).getFanduelData(anyString());
        assertEquals(1, result.size());
    }

    @Test
    void shouldGetAllContestData() {
        List<Map<String, Object>> result = fanduelData.getAllContestData();
        verify(mockApi).getFanduelData(anyString());
        assertEquals("NFL", result.get(0).get("sport"));
        assertEquals("PIT @ CLE", result.get(0).get("contest"));
        List players = (List) result.get(0).get("players");
        HashMap playerInfo1 = (HashMap) players.get(0);
        assertEquals(748070, playerInfo1.get("playerId"));
        assertEquals("QB", playerInfo1.get("position"));
        assertEquals(15500, playerInfo1.get("salary"));
        HashMap playerInfo2 = (HashMap) players.get(1);
        assertEquals(742390, playerInfo2.get("playerId"));
        assertEquals("RB", playerInfo2.get("position"));
        assertEquals(14500, playerInfo2.get("salary"));
        assertEquals(44, ((List) result.get(0).get("players")).size());
    }

}