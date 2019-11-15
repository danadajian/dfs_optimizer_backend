package collect;

import api.ApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AggregateDataTest implements MockResponses {
    private ApiClient mockApi = mock(ApiClient.class);
    private AggregateData aggregateData = new AggregateData(mockApi);

    @BeforeEach
    void setUp() {
        when(mockApi.getFanduelData()).thenReturn(fakeFanduelData);
        when(mockApi.getDraftKingsData()).thenReturn(fakeDraftKingsData);
        when(mockApi.getProjectionsFromThisWeek()).thenReturn(fakeProjectionsResponse);
        when(mockApi.getEventsFromThisWeek()).thenReturn(fakeEventsResponse);
    }

    @Test
    void shouldGetDfsPlayerData() {
        List<Map<String, Object>> result = aggregateData.getDfsPlayerData();
        verify(mockApi, times(1)).getFanduelData();
        verify(mockApi, times(1)).getDraftKingsData();
        verify(mockApi, times(1)).getProjectionsFromThisWeek();
        verify(mockApi, times(1)).getEventsFromThisWeek();
        assertEquals("fd", result.get(0).get("site"));
        assertEquals("NFL", result.get(0).get("sport"));
        assertEquals("PIT @ CLE", result.get(0).get("contest"));
        HashMap fdPlayers = (HashMap) result.get(0).get("players");
        HashMap playerInfo1 = (HashMap) fdPlayers.get(748070);
        assertEquals("Baker Mayfield", playerInfo1.get("name"));
        assertEquals("Cle", playerInfo1.get("team"));
        assertEquals("v. Pit", playerInfo1.get("opponent"));
        assertEquals("Thu 8:20PM EST", playerInfo1.get("gameDate"));
        assertEquals(15.7830903580811403738955809938621031914, playerInfo1.get("projection"));
        assertEquals("QB", playerInfo1.get("position"));
        assertEquals(15500, playerInfo1.get("salary"));
        HashMap playerInfo2 = (HashMap) fdPlayers.get(835909);
        assertEquals("JuJu Smith-Schuster", playerInfo2.get("name"));
        assertEquals("Pit", playerInfo2.get("team"));
        assertEquals("@ Cle", playerInfo2.get("opponent"));
        assertEquals("Thu 8:20PM EST", playerInfo2.get("gameDate"));
        assertEquals(9.86259269068366311599183345415026981143, playerInfo2.get("projection"));
        assertEquals("WR", playerInfo2.get("position"));
        assertEquals(10500, playerInfo2.get("salary"));

        assertEquals("dk", result.get(1).get("site"));
        assertEquals("NFL", result.get(1).get("sport"));
        assertEquals("Showdown Captain Mode (PIT vs CLE)", result.get(1).get("contest"));
        HashMap dkPlayers = (HashMap) result.get(1).get("players");
        HashMap playerInfo3 = (HashMap) dkPlayers.get(748070);
        assertEquals("Baker Mayfield", playerInfo3.get("name"));
        assertEquals("Cle", playerInfo3.get("team"));
        assertEquals("v. Pit", playerInfo3.get("opponent"));
        assertEquals("Thu 8:20PM EST", playerInfo3.get("gameDate"));
        assertEquals(17.03589782678331730935121967042855912, playerInfo3.get("projection"));
        assertEquals("QB", playerInfo3.get("position"));
        assertEquals(9000, playerInfo3.get("salary"));
        HashMap playerInfo4 = (HashMap) dkPlayers.get(835909);
        assertEquals("JuJu Smith-Schuster", playerInfo4.get("name"));
        assertEquals("Pit", playerInfo4.get("team"));
        assertEquals("@ Cle", playerInfo4.get("opponent"));
        assertEquals("Thu 8:20PM EST", playerInfo4.get("gameDate"));
        assertEquals(12.394318168462334858864779483118843073, playerInfo4.get("projection"));
        assertEquals("WR", playerInfo4.get("position"));
        assertEquals(8600, playerInfo4.get("salary"));
    }
}