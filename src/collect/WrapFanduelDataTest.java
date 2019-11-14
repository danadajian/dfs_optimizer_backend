package collect;

import api.ApiClient;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WrapFanduelDataTest implements MockResponses {

    private ApiClient mockApi = mock(ApiClient.class);
    private WrapFanduelData wrapFanduelData = new WrapFanduelData(mockApi, "NFL", "PIT @ CLE");

    @BeforeEach
    void setUp() {
        when(mockApi.getFanduelData()).thenReturn(fakeFanduelData);
    }

    @Test
    void shouldGetValidContests() {
        List<JSONObject> result = wrapFanduelData.getvalidContests();
        assertEquals(1, result.size());
    }

//    @Test
//    void shouldGetAllContestData() {
//        List<Map<String, Object>> result = wrapFanduelData.getAllContestData();
//        assertEquals("NFL", result.get(0).get("sport"));
//        assertEquals("Showdown Captain Mode (PIT vs CLE)", result.get(0).get("contest"));
//        HashMap players = (HashMap) result.get(0).get("players");
//        HashMap playerInfo1 = (HashMap) players.get(822857);
//        assertEquals("Nick Chubb", playerInfo1.get("name"));
//        assertEquals("RB", playerInfo1.get("position"));
//        assertEquals(10600, playerInfo1.get("salary"));
//        HashMap playerInfo2 = (HashMap) players.get(589991);
//        assertEquals("Jarvis Landry", playerInfo2.get("name"));
//        assertEquals("WR", playerInfo2.get("position"));
//        assertEquals(8000, playerInfo2.get("salary"));
//        assertEquals(38, ((HashMap) result.get(0).get("players")).size());
//    }

}