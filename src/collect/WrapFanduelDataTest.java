package collect;

import api.ApiClient;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WrapFanduelDataTest implements MockResponses {

    private ApiClient mockApi = mock(ApiClient.class);
    private WrapFanduelData wrapFanduelData = new WrapFanduelData(mockApi);

    @BeforeEach
    void setUp() {
        when(mockApi.getFanduelData()).thenReturn(fakeFanduelData);
    }

    @Test
    void shouldGetValidContests() {
        List<JSONObject> result = wrapFanduelData.getValidContests();
        verify(mockApi).getFanduelData();
        assertEquals(1, result.size());
    }

    @Test
    void shouldGetAllContestData() {
        List<Map<String, Object>> result = wrapFanduelData.getAllContestData();
        verify(mockApi).getFanduelData();
        assertEquals("NFL", result.get(0).get("sport"));
        assertEquals("PIT @ CLE", result.get(0).get("contest"));
        HashMap players = (HashMap) result.get(0).get("players");
        HashMap playerInfo1 = (HashMap) players.get(589984);
        assertEquals("WR", playerInfo1.get("position"));
        assertEquals(11500, playerInfo1.get("salary"));
        HashMap playerInfo2 = (HashMap) players.get(746613);
        assertEquals("RB", playerInfo2.get("position"));
        assertEquals(9000, playerInfo2.get("salary"));
        assertEquals(44, ((HashMap) result.get(0).get("players")).size());
    }

}