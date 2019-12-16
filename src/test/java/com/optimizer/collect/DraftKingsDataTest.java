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
    private DraftKingsData draftKingsData = new DraftKingsData(mockApi);

    @BeforeEach
    void setUp() {
        when(mockApi.getDraftKingsData(anyString())).thenReturn(fakeDraftKingsData);
    }

    @Test
    void shouldGetFixtureLists() {
        List<JSONObject> result = draftKingsData.getValidContests("nfl");
        verify(mockApi).getDraftKingsData(anyString());
        assertEquals(1, result.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldGetAllContestData() {
        List<Map<String, Object>> result = draftKingsData.getAllContestData("nfl");
        verify(mockApi).getDraftKingsData(anyString());
        assertEquals("PIT vs CLE (11/14)", result.get(0).get("contest"));
        List<Map<String, Object>> players = (List<Map<String, Object>>) result.get(0).get("players");
        Map<String, Object> playerInfo1 = players.get(0);
        assertEquals(822857, playerInfo1.get("playerId"));
        assertEquals("RB", playerInfo1.get("position"));
        assertEquals(10600, playerInfo1.get("salary"));
        Map<String, Object> playerInfo2 = players.get(1);
        assertEquals(742390, playerInfo2.get("playerId"));
        assertEquals("RB", playerInfo2.get("position"));
        assertEquals(10800, playerInfo2.get("salary"));
        assertEquals(38, players.size());
    }

}