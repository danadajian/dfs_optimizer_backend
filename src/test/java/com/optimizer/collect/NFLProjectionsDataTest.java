package com.optimizer.collect;

import api.ApiClient;
import collect.ProjectionsData;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NFLProjectionsDataTest implements MockResponses {

    private ApiClient mockApi = mock(ApiClient.class);
    private ProjectionsData projectionsData = new ProjectionsData(mockApi, "nfl");

    @BeforeEach
    void setUp() {
        when(mockApi.getCurrentEvents(anyString())).thenReturn(fakeNFLEventsResponse);
        when(mockApi.getProjectionsFromThisWeek(anyString())).thenReturn(fakeNFLProjectionsResponse);
    }

    @Test
    void shouldGetHomeOrAwayMapFromEvents() {
        Map<Integer, Map<Object, Object>> result = projectionsData.getEventData();
        verify(mockApi).getCurrentEvents(anyString());
        assertEquals("@ Det", result.get(2142062).get(331));
        assertEquals("v. Ari", result.get(2142140).get(359));
    }

    @Test
    void shouldGetProjectionsFromThisWeek() {
        Map<Integer, Map<String, Object>> result = projectionsData.getFantasyProjections();
        verify(mockApi, times(1)).getCurrentEvents(anyString());
        verify(mockApi).getProjectionsFromThisWeek(anyString());
        assertEquals("Dak Prescott", result.get(591816).get("name"));
        assertEquals("Dal", result.get(591816).get("team"));
        assertEquals("@ Det", result.get(591816).get("opponent"));
        assertEquals("Sun 11/17 1:00PM EST", result.get(347).get("gameDate"));
        assertEquals(26.0332570149543679641473708950911299307, result.get(591816).get("dkProjection"));
        assertEquals(23.8622262095728403380510578974536722008, result.get(591816).get("fdProjection"));
        assertEquals("Vikings D/ST", result.get(347).get("name"));
        assertEquals("Min", result.get(347).get("team"));
        assertEquals("v. Den", result.get(347).get("opponent"));
        assertEquals("Sun 11/17 1:00PM EST", result.get(347).get("gameDate"));
        assertEquals(11.07, result.get(347).get("dkProjection"));
        assertEquals(11.07, result.get(347).get("fdProjection"));
        assertEquals("49ers D/ST", result.get(359).get("name"));
        assertEquals("SF", result.get(359).get("team"));
        assertEquals("v. Ari", result.get(359).get("opponent"));
        assertEquals("Sun 11/17 4:05PM EST", result.get(359).get("gameDate"));
        assertEquals(11.01, result.get(359).get("dkProjection"));
        assertEquals(11.01, result.get(359).get("fdProjection"));
    }
}