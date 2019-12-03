package com.optimizer.collect;

import api.ApiClient;
import collect.EventData;
import collect.StandardProjectionsData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class StandardProjectionsDataTest implements MockResponses {

    private ApiClient mockApi = mock(ApiClient.class);
    private EventData eventData = new EventData(mockApi, "sport");
    private StandardProjectionsData standardProjectionsData = new StandardProjectionsData(mockApi, "sport");

    @BeforeEach
    void setUp() {
        when(mockApi.getCurrentEvents(anyString())).thenReturn(fakeNBAEventsResponse);
        when(mockApi.getParticipants(anyString())).thenReturn(fakeParticipantsResponse);
        when(mockApi.getProjectionsFromEvent(anyString(), anyInt())).thenReturn(fakeNBAProjectionsResponse);
    }

    @Test
    void shouldGetEventDataFromThisWeek() {
        Map<Integer, Map<Object, Object>> result = eventData.getEventData();
        assertEquals("Tue 8:00PM EST", result.get(2177431).get("gameDate"));
        assertEquals("v. GS", result.get(2177431).get(29));
        assertEquals("@ Mem", result.get(2177431).get(9));
    }

    @Test
    void shouldGetParticipantsData() {
        Map<Integer, Map<String, String>> result = standardProjectionsData.getParticipantsData();
        assertEquals("Vince Carter", result.get(3230).get("name"));
        assertEquals("Atl", result.get(3230).get("team"));
        assertEquals("Stephen Curry", result.get(338365).get("name"));
        assertEquals("GS", result.get(338365).get("team"));
    }

    @Test
    void shouldGetProjectionsFromThisWeek() {
        Map<Integer, Map<String, Object>> result = standardProjectionsData.getFantasyProjections();
        verify(mockApi, times(1)).getCurrentEvents(anyString());
        verify(mockApi, times(1)).getParticipants(anyString());
        assertEquals("Stephen Curry", result.get(338365).get("name"));
        assertEquals("GS", result.get(338365).get("team"));
        assertEquals("@ Mem", result.get(338365).get("opponent"));
        assertEquals("Tue 8:00PM EST", result.get(338365).get("gameDate"));
        assertEquals(0.0, result.get(338365).get("dkProjection"));
        assertEquals(0.0, result.get(338365).get("fdProjection"));
    }
}