package handler;

import collect.misc.Injuries;
import handler.InjuryHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;

class InjuryHandlerTest {

    @Mock
    Injuries injuries;

    @InjectMocks
    InjuryHandler injuryHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void handleMLBRequest() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("sport", "mlb");
        injuryHandler.handleRequest(testMap);
        verify(injuries).getStandardInjuryData("mlb");
    }

    @Test
    void handleNFLRequest() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("sport", "nfl");
        injuryHandler.handleRequest(testMap);
        verify(injuries).getNFLInjuryData();
    }

    @Test
    void handleNBARequest() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("sport", "nba");
        injuryHandler.handleRequest(testMap);
        verify(injuries).getStandardInjuryData("nba");
    }

    @Test
    void handleNHLRequest() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("sport", "nhl");
        injuryHandler.handleRequest(testMap);
        verify(injuries).getStandardInjuryData("nhl");
    }
}