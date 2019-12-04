package com.optimizer.collect;

import api.ApiClient;
import collect.EventData;
import collect.ParticipantsData;
import collect.ProjectionsData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class ProjectionsDataTest implements MockResponses {

    private ApiClient mockApi = mock(ApiClient.class);
    private EventData nflEventData = new EventData(mockApi, "nfl");
    private ProjectionsData nflProjectionsData = new ProjectionsData(mockApi, "nfl");
    private EventData nhlEventData = new EventData(mockApi, "nhl");
    private ParticipantsData nhlParticipantsData = new ParticipantsData(mockApi, "nhl");
    private ProjectionsData nhlProjectionsData = new ProjectionsData(mockApi, "nhl");
    private EventData nbaEventData = new EventData(mockApi, "nba");
    private ParticipantsData nbaParticipantsData = new ParticipantsData(mockApi, "nba");
    private ProjectionsData projectionsData = new ProjectionsData(mockApi, "nba");

    @BeforeEach
    void setUp() {
        when(mockApi.getCurrentEvents("nfl")).thenReturn(fakeNFLEventsResponse);
        when(mockApi.getProjectionsFromThisWeek("nfl")).thenReturn(fakeNFLProjectionsResponse);
        when(mockApi.getCurrentEvents("nhl")).thenReturn(fakeNHLEventsResponse);
        when(mockApi.getParticipants("nhl")).thenReturn(fakeNHLParticipantsResponse);
        when(mockApi.getProjectionsFromEvent("nhl", 2154418)).thenReturn(fakeNHLProjectionsResponse);
        when(mockApi.getCurrentEvents("nba")).thenReturn(fakeNBAEventsResponse);
        when(mockApi.getParticipants("nba")).thenReturn(fakeNBAParticipantsResponse);
        when(mockApi.getProjectionsFromEvent("nba", 2177431)).thenReturn(fakeNBAProjectionsResponse);
    }

    @Test
    void shouldGetHomeOrAwayMapFromNFLEvents() {
        Map<Integer, Map<Object, Object>> result = nflEventData.getEventData();
        verify(mockApi).getCurrentEvents("nfl");
        assertEquals("@ Det", result.get(2142062).get(331));
        assertEquals("v. Ari", result.get(2142140).get(359));
    }

    @Test
    void shouldGetNFLProjectionsFromThisWeek() {
        Map<Integer, Map<String, Object>> result = nflProjectionsData.getNFLFantasyProjections();
        verify(mockApi, times(1)).getCurrentEvents("nfl");
        verify(mockApi).getProjectionsFromThisWeek("nfl");
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
    void shouldGetNHLEventDataFromThisWeek() {
        Map<Integer, Map<Object, Object>> result = nhlEventData.getEventData();
        assertEquals("Wed 9:30PM EST", result.get(2154418).get("gameDate"));
        assertEquals("v. Ott", result.get(2154418).get(4959));
        assertEquals("@ Edm", result.get(2154418).get(4967));
    }

    @Test
    void shouldGetNHLParticipantsData() {
        Map<Integer, Map<String, String>> result = nhlParticipantsData.getParticipantsData();
        assertEquals("Brandon Manning", result.get(621383).get("name"));
        assertEquals("Edm", result.get(621383).get("team"));
        assertEquals("Joel Persson", result.get(1116809).get("name"));
        assertEquals("Edm", result.get(1116809).get("team"));
    }

    @Test
    void shouldGetNHLProjectionsFromThisWeek() {
        Map<Integer, Map<String, Object>> result = nhlProjectionsData.getNHLFantasyProjections();
        verify(mockApi, times(1)).getCurrentEvents("nhl");
        verify(mockApi, times(1)).getParticipants("nhl");
        assertEquals("James Neal", result.get(301959).get("name"));
        assertEquals("Edm", result.get(301959).get("team"));
        assertEquals("v. Ott", result.get(301959).get("opponent"));
        assertEquals("Wed 9:30PM EST", result.get(301959).get("gameDate"));
        assertEquals(3.89491, result.get(301959).get("dkProjection"));
        assertEquals(14.42384, result.get(301959).get("fdProjection"));
        assertEquals("Mike Smith", result.get(172862).get("name"));
        assertEquals("Edm", result.get(172862).get("team"));
        assertEquals("v. Ott", result.get(172862).get("opponent"));
        assertEquals("Wed 9:30PM EST", result.get(172862).get("gameDate"));
        assertEquals(0.0, result.get(172862).get("dkProjection"));
        assertEquals(0.0, result.get(172862).get("fdProjection"));
    }

    @Test
    void shouldGetNBAEventDataFromThisWeek() {
        Map<Integer, Map<Object, Object>> result = nbaEventData.getEventData();
        assertEquals("Tue 8:00PM EST", result.get(2177431).get("gameDate"));
        assertEquals("v. GS", result.get(2177431).get(29));
        assertEquals("@ Mem", result.get(2177431).get(9));
    }

    @Test
    void shouldGetNBAParticipantsData() {
        Map<Integer, Map<String, String>> result = nbaParticipantsData.getParticipantsData();
        assertEquals("Vince Carter", result.get(3230).get("name"));
        assertEquals("Atl", result.get(3230).get("team"));
        assertEquals("Stephen Curry", result.get(338365).get("name"));
        assertEquals("GS", result.get(338365).get("team"));
    }

    @Test
    void shouldGetNBAProjectionsFromThisWeek() {
        Map<Integer, Map<String, Object>> result = projectionsData.getStandardFantasyProjections();
        verify(mockApi, times(1)).getCurrentEvents("nba");
        verify(mockApi, times(1)).getParticipants("nba");
        assertEquals("Stephen Curry", result.get(338365).get("name"));
        assertEquals("GS", result.get(338365).get("team"));
        assertEquals("@ Mem", result.get(338365).get("opponent"));
        assertEquals("Tue 8:00PM EST", result.get(338365).get("gameDate"));
        assertEquals(0.0, result.get(338365).get("dkProjection"));
        assertEquals(0.0, result.get(338365).get("fdProjection"));
    }
}