package collect.misc;

import api.ApiClient;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OpponentRanksTest {

    OpponentRanksTest() throws IOException {
    }

    private ApiClient mockApi = mock(ApiClient.class);
    private OpponentRanks opponentRanks = new OpponentRanks(mockApi);
    private String fakeFantasyProsData = new String(Files.readAllBytes(Paths.get("src/main/resources/opponentRanksResponse.txt")));

    @BeforeEach
    void setUp() {
        when(mockApi.getOpponentRanksData("nfl")).thenReturn(fakeFantasyProsData);
    }

    @Test
    void shouldGetNFLOpponentRanks() {
        Map<String, Map<String, Integer>> result = opponentRanks.getOpponentRanks("nfl");
        assertEquals(32, result.get("Arizona Cardinals").get("QB"));
        assertEquals(23, result.get("Arizona Cardinals").get("RB"));
        assertEquals(17, result.get("Miami Dolphins").get("TE"));
        assertEquals(30, result.get("New York Giants").get("WR"));
        assertEquals(15, result.get("Detroit Lions").get("D/ST"));
    }
}