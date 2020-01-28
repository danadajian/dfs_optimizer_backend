package collect;

import api.ApiClient;
import collect.dfs.Fanduel;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FanduelTest implements MockResponses {

    private ApiClient mockApi = mock(ApiClient.class);
    private Fanduel fanduel = new Fanduel(mockApi);

    @BeforeEach
    void setUp() {
        when(mockApi.getFanduelData(anyString())).thenReturn(fakeFanduelData);
    }

    @Test
    void shouldGetValidContests() {
        List<JSONObject> result = fanduel.getValidContests("testDateString");
        verify(mockApi).getFanduelData(anyString());
        assertEquals(1, result.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldGetAllContestData() {
        List<Map<String, Object>> result = fanduel.getAllContestData("testDateString");
        verify(mockApi).getFanduelData(anyString());
        assertEquals("NFL", result.get(0).get("sport"));
        assertEquals("PIT @ CLE", result.get(0).get("contest"));
        List<Map<String, Object>> players = (List<Map<String, Object>>) result.get(0).get("players");
        Map<String, Object> playerInfo1 = players.get(0);
        assertEquals(748070, playerInfo1.get("playerId"));
        assertEquals("Baker Mayfield", playerInfo1.get("name"));
        assertEquals("CLE", playerInfo1.get("team"));
        assertEquals("QB", playerInfo1.get("position"));
        assertEquals(15500, playerInfo1.get("salary"));
        Map<String, Object> playerInfo2 = players.get(1);
        assertNull(playerInfo2.get("playerId"));
        assertEquals("James Conner", playerInfo2.get("name"));
        assertEquals("PIT", playerInfo2.get("team"));
        assertEquals("RB", playerInfo2.get("position"));
        assertEquals(14500, playerInfo2.get("salary"));
        assertEquals(44, players.size());
    }

}