package com.optimizer.collect;

import api.ApiClient;
import collect.EventData;
import collect.ParticipantsData;
import collect.ProjectionsData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class ProjectionsDataTest implements MockResponses {

    ProjectionsDataTest() throws IOException {
    }

    private ApiClient mockApi = mock(ApiClient.class);
    private EventData nflEventData = new EventData(mockApi, "nfl");
    private ProjectionsData nflProjectionsData = new ProjectionsData(mockApi, "nfl");
    private EventData nhlEventData = new EventData(mockApi, "nhl");
    private ParticipantsData nhlParticipantsData = new ParticipantsData(mockApi, "nhl");
    private ProjectionsData nhlProjectionsData = new ProjectionsData(mockApi, "nhl");
    private EventData nbaEventData = new EventData(mockApi, "nba");
    private ParticipantsData nbaParticipantsData = new ParticipantsData(mockApi, "nba");
    private ProjectionsData projectionsData = new ProjectionsData(mockApi, "nba");
    private String fakeNFLProjectionsResponse = new String(Files.readAllBytes(Paths.get("src/main/resources/nflProjectionsResponse.txt")));
    private String fakeNFLEventsResponse = new String(Files.readAllBytes(Paths.get("src/main/resources/nflEventsResponse.txt")));
    private String fakeNFLOddsResponse = new String(Files.readAllBytes(Paths.get("src/main/resources/nflOddsResponse.txt")));
    private String fakeNBAEventsResponse = new String(Files.readAllBytes(Paths.get("src/main/resources/nbaEventsResponse.txt")));
    private String fakeNBAParticipantsResponse = new String(Files.readAllBytes(Paths.get("src/main/resources/nbaParticipantsResponse.txt")));
    private String fakeNBAProjectionsResponse = new String(Files.readAllBytes(Paths.get("src/main/resources/nbaProjectionsResponse.txt")));
    private String fakeNBAOddsResponse = new String(Files.readAllBytes(Paths.get("src/main/resources/nbaOddsResponse.txt")));
    private String fakeNHLEventsResponse = new String(Files.readAllBytes(Paths.get("src/main/resources/nhlEventsResponse.txt")));
    private String fakeNHLParticipantsResponse = new String(Files.readAllBytes(Paths.get("src/main/resources/nhlParticipantsResponse.txt")));
    private String fakeNHLProjectionsResponse = new String(Files.readAllBytes(Paths.get("src/main/resources/nhlProjectionsResponse.txt")));
    private String fakeNHLOddsResponse = new String(Files.readAllBytes(Paths.get("src/main/resources/nhlOddsResponse.txt")));

    @BeforeEach
    void setUp() {
        when(mockApi.getCurrentEvents("nfl")).thenReturn(fakeNFLEventsResponse);
        when(mockApi.getProjectionsFromThisWeek("nfl")).thenReturn(fakeNFLProjectionsResponse);
        when(mockApi.getOddsData("nfl")).thenReturn(fakeNFLOddsResponse);
        when(mockApi.getCurrentEvents("nhl")).thenReturn(fakeNHLEventsResponse);
        when(mockApi.getParticipants("nhl")).thenReturn(fakeNHLParticipantsResponse);
        when(mockApi.getProjectionsFromEvent("nhl", 2154769)).thenReturn(fakeNHLProjectionsResponse);
        when(mockApi.getOddsData("nhl")).thenReturn(fakeNHLOddsResponse);
        when(mockApi.getCurrentEvents("nba")).thenReturn(fakeNBAEventsResponse);
        when(mockApi.getParticipants("nba")).thenReturn(fakeNBAParticipantsResponse);
        when(mockApi.getProjectionsFromEvent("nba", 2177081)).thenReturn(fakeNBAProjectionsResponse);
        when(mockApi.getOddsData("nba")).thenReturn(fakeNBAOddsResponse);
    }

    @Test
    void shouldGetHomeOrAwayMapFromNFLEvents() {
        Map<Integer, Map<Object, Object>> result = nflEventData.getEventData();
        verify(mockApi).getCurrentEvents("nfl");
        assertEquals("v. NYJ", result.get(2142041).get(366));
        assertEquals("@ Bal", result.get(2142041).get(352));
    }

    @Test
    void shouldGetNFLProjectionsFromThisWeek() {
        Map<Integer, Map<String, Object>> result = nflProjectionsData.getNFLFantasyProjections();
        verify(mockApi, times(1)).getCurrentEvents("nfl");
        verify(mockApi).getProjectionsFromThisWeek("nfl");
        assertEquals("Lamar Jackson", result.get(877745).get("name"));
        assertEquals("Bal", result.get(877745).get("team"));
        assertEquals("v. NYJ", result.get(877745).get("opponent"));
        assertEquals("Thu 8:20PM EST", result.get(366).get("gameDate"));
        assertEquals("-15.0", result.get(366).get("spread"));
        assertEquals(44.5, result.get(366).get("overUnder"));
        assertEquals(25.4156364974765640884448627852073774696, result.get(877745).get("dkProjection"));
        assertEquals(24.1805531418601748497370736365420855337, result.get(877745).get("fdProjection"));
        assertEquals("Jets D/ST", result.get(352).get("name"));
        assertEquals("NYJ", result.get(352).get("team"));
        assertEquals("@ Bal", result.get(352).get("opponent"));
        assertEquals("Thu 8:20PM EST", result.get(352).get("gameDate"));
        assertEquals("+15.0", result.get(352).get("spread"));
        assertEquals(44.5, result.get(352).get("overUnder"));
        assertEquals(3.41, result.get(352).get("dkProjection"));
        assertEquals(3.41, result.get(352).get("fdProjection"));
    }

    @Test
    void shouldGetNHLEventDataFromThisWeek() {
        Map<Integer, Map<Object, Object>> result = nhlEventData.getEventData();
        assertEquals("Tue 7:00PM EST", result.get(2154769).get("gameDate"));
        assertEquals("v. Mon", result.get(2154769).get(4969));
        assertEquals("@ Pit", result.get(2154769).get(4963));
    }

    @Test
    void shouldGetNHLParticipantsData() {
        Map<Integer, Map<String, String>> result = nhlParticipantsData.getParticipantsData();
        assertEquals("Chad Ruhwedel", result.get(732552).get("name"));
        assertEquals("Pit", result.get(732552).get("team"));
        assertEquals("Christian Folin", result.get(824587).get("name"));
        assertEquals("Mon", result.get(824587).get("team"));
    }

    @Test
    void shouldGetNHLProjectionsFromThisWeek() {
        Map<Integer, Map<String, Object>> result = nhlProjectionsData.getNHLFantasyProjections();
        verify(mockApi, times(1)).getCurrentEvents("nhl");
        verify(mockApi, times(1)).getParticipants("nhl");
        assertEquals("Chad Ruhwedel", result.get(732552).get("name"));
        assertEquals("Pit", result.get(732552).get("team"));
        assertEquals("v. Mon", result.get(732552).get("opponent"));
        assertEquals("Tue 7:00PM EST", result.get(732552).get("gameDate"));
        assertEquals("-1.5", result.get(732552).get("spread"));
        assertEquals(6.5, result.get(732552).get("overUnder"));
        assertEquals(1.65178, result.get(732552).get("dkProjection"));
        assertEquals(5.73402, result.get(732552).get("fdProjection"));
        assertEquals("Christian Folin", result.get(824587).get("name"));
        assertEquals("Mon", result.get(824587).get("team"));
        assertEquals("@ Pit", result.get(824587).get("opponent"));
        assertEquals("Tue 7:00PM EST", result.get(824587).get("gameDate"));
        assertEquals("+1.5", result.get(824587).get("spread"));
        assertEquals(6.5, result.get(824587).get("overUnder"));
        assertEquals(0.0, result.get(824587).get("dkProjection"));
        assertEquals(0.0, result.get(824587).get("fdProjection"));
    }

    @Test
    void shouldGetNBAEventDataFromThisWeek() {
        Map<Integer, Map<Object, Object>> result = nbaEventData.getEventData();
        assertEquals("Tue 8:00PM EST", result.get(2177081).get("gameDate"));
        assertEquals("v. Den", result.get(2177081).get(20));
        assertEquals("@ Phi", result.get(2177081).get(7));
    }

    @Test
    void shouldGetNBAParticipantsData() {
        Map<Integer, Map<String, String>> result = nbaParticipantsData.getParticipantsData();
        assertEquals("Al Horford", result.get(280587).get("name"));
        assertEquals("Phi", result.get(280587).get("team"));
        assertEquals("Paul Millsap", result.get(237675).get("name"));
        assertEquals("Den", result.get(237675).get("team"));
    }

    @Test
    void shouldGetNBAProjectionsFromThisWeek() {
        Map<Integer, Map<String, Object>> result = projectionsData.getStandardFantasyProjections();
        verify(mockApi, times(1)).getCurrentEvents("nba");
        verify(mockApi, times(1)).getParticipants("nba");
        assertEquals("Al Horford", result.get(280587).get("name"));
        assertEquals("Phi", result.get(280587).get("team"));
        assertEquals("v. Den", result.get(280587).get("opponent"));
        assertEquals("Tue 8:00PM EST", result.get(280587).get("gameDate"));
        assertEquals("-4.0", result.get(280587).get("spread"));
        assertEquals(207.0, result.get(280587).get("overUnder"));
        assertEquals(29.65199, result.get(280587).get("dkProjection"));
        assertEquals(30.02133, result.get(280587).get("fdProjection"));
        assertEquals("Paul Millsap", result.get(237675).get("name"));
        assertEquals("Den", result.get(237675).get("team"));
        assertEquals("@ Phi", result.get(237675).get("opponent"));
        assertEquals("Tue 8:00PM EST", result.get(237675).get("gameDate"));
        assertEquals("+4.0", result.get(237675).get("spread"));
        assertEquals(207.0, result.get(237675).get("overUnder"));
        assertEquals(21.20313, result.get(237675).get("dkProjection"));
        assertEquals(21.28275, result.get(237675).get("fdProjection"));
    }
}