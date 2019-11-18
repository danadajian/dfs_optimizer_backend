package com.optimizer.collect;

import api.ApiClient;
import collect.WeeklyProjectionsData;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeeklyProjectionsDataTest implements MockResponses {

    private ApiClient mockApi = mock(ApiClient.class);
    private WeeklyProjectionsData weeklyProjectionsData = new WeeklyProjectionsData(mockApi, "sport");

    @BeforeEach
    void setUp() {
        when(mockApi.getEventsFromThisWeek(anyString())).thenReturn(fakeEventsResponse);
        when(mockApi.getProjectionsFromThisWeek(anyString())).thenReturn(fakeProjectionsResponse);
    }

    @Test
    void shouldGetHomeOrAwayMapFromEvents() {
        Map<Integer, String> result = weeklyProjectionsData.getHomeOrAwayMap();
        verify(mockApi).getEventsFromThisWeek(anyString());
        assertEquals("@ Det", result.get(331));
        assertEquals("v. Ari", result.get(359));
    }

    @Test
    void shouldGetProjectionsFromThisWeek() {
        Map<Integer, Map<String, Object>> result = weeklyProjectionsData.getFantasyProjections();
        verify(mockApi, times(1)).getEventsFromThisWeek(anyString());
        verify(mockApi).getProjectionsFromThisWeek(anyString());
        assertEquals("Dak Prescott", result.get(591816).get("name"));
        assertEquals("Dal", result.get(591816).get("team"));
        assertEquals("@ Det", result.get(591816).get("opponent"));
        assertEquals("Sun 1:00PM EST", result.get(347).get("gameDate"));
        assertEquals(26.0332570149543679641473708950911299307, result.get(591816).get("dkProjection"));
        assertEquals(23.8622262095728403380510578974536722008, result.get(591816).get("fdProjection"));
        assertEquals("Vikings D/ST", result.get(347).get("name"));
        assertEquals("Min", result.get(347).get("team"));
        assertEquals("v. Den", result.get(347).get("opponent"));
        assertEquals("Sun 1:00PM EST", result.get(347).get("gameDate"));
        assertEquals(11.07, result.get(347).get("dkProjection"));
        assertEquals(11.07, result.get(347).get("fdProjection"));
        assertEquals("49ers D/ST", result.get(359).get("name"));
        assertEquals("SF", result.get(359).get("team"));
        assertEquals("v. Ari", result.get(359).get("opponent"));
        assertEquals("Sun 4:05PM EST", result.get(359).get("gameDate"));
        assertEquals(11.01, result.get(359).get("dkProjection"));
        assertEquals(11.01, result.get(359).get("fdProjection"));
    }

    @Test
    void shouldGetEasternTimeFromGameDateJson() {
        JSONObject exampleGameDate = new JSONObject("{\"year\":2019,\"month\":11,\"date\":17,\"hour\":18,\"minute\":0,\"full\":\"2019-11-17T18:00:00\",\"dateType\":\"utc\"}");
        String result = weeklyProjectionsData.getEasternTime(exampleGameDate);
        assertEquals("Sun 1:00PM EST", result);
    }
}