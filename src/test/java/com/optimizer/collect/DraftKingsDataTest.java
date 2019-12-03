package com.optimizer.collect;

import api.ApiClient;
import collect.DraftKingsData;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DraftKingsDataTest implements MockResponses {

    private ApiClient mockApi = mock(ApiClient.class);
    private DraftKingsData draftKingsData = new DraftKingsData(mockApi, "sport");

    @BeforeEach
    void setUp() {
        when(mockApi.getDraftKingsData(anyString())).thenReturn(fakeDraftKingsData);
    }

    @Test
    void shouldGetFixtureLists() {
        List<JSONObject> result = draftKingsData.getValidContests();
        verify(mockApi).getDraftKingsData(anyString());
        assertEquals(1, result.size());
    }

    @Test
    void shouldGetAllContestData() {
        List<Map<String, Object>> result = draftKingsData.getAllContestData();
        verify(mockApi).getDraftKingsData(anyString());
        assertEquals("PIT vs CLE (11/14)", result.get(0).get("contest"));
        List players = (List) result.get(0).get("players");
        HashMap playerInfo1 = (HashMap) players.get(0);
        assertEquals(822857, playerInfo1.get("playerId"));
        assertEquals("RB", playerInfo1.get("position"));
        assertEquals(10600, playerInfo1.get("salary"));
        HashMap playerInfo2 = (HashMap) players.get(1);
        assertEquals(742390, playerInfo2.get("playerId"));
        assertEquals("RB", playerInfo2.get("position"));
        assertEquals(10800, playerInfo2.get("salary"));
        assertEquals(38, ((List) result.get(0).get("players")).size());
    }

}