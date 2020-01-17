package handler;

import collect.misc.OpponentRanks;
import handler.OpponentRanksHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;

class OpponentRanksHandlerTest {

    @Mock
    OpponentRanks opponentRanks;

    @InjectMocks
    OpponentRanksHandler opponentRanksHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldHandleOpponentRanksRequest() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("sport", "nfl");
        opponentRanksHandler.handleRequest(testMap);
        verify(opponentRanks).getOpponentRanks("nfl");
    }

    @Test
    void shouldHandleOpponentRanksRequestNonNFL() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("sport", "nba");
        opponentRanksHandler.handleRequest(testMap);
        verify(opponentRanks).getOpponentRanks("nba");
    }
}