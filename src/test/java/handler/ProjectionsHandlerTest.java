package handler;

import collect.stats.MLBProjections;
import collect.stats.NFLProjections;
import collect.stats.NHLProjections;
import collect.stats.StandardProjections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import util.AWSClient;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

class ProjectionsHandlerTest {

    @Mock
    StandardProjections standardProjectionsData;

    @Mock
    NFLProjections nflProjections;

    @Mock
    NHLProjections nhlProjections;

    @Mock
    MLBProjections mlbProjections;

    @Mock
    AWSClient AWSClient;

    @InjectMocks
    ProjectionsHandler projectionsHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(nflProjections.getProjectionsData()).thenReturn(new HashMap<>());
        when(nhlProjections.getProjectionsData()).thenReturn(new HashMap<>());
        when(standardProjectionsData.getProjectionsData(anyString())).thenReturn(new HashMap<>());
    }

    @Test
    void shouldHandleRequestForMLBProjections() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("sport", "mlb");
        projectionsHandler.handleRequest(testMap);
        verify(mlbProjections).getProjectionsData();
    }

    @Test
    void shouldHandleRequestForNFLProjections() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("sport", "nfl");
        projectionsHandler.handleRequest(testMap);
        verify(nflProjections).getProjectionsData();
    }

    @Test
    void shouldHandleRequestForNBAProjections() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("sport", "nba");
        projectionsHandler.handleRequest(testMap);
        verify(standardProjectionsData).getProjectionsData("nba");
    }

    @Test
    void shouldHandleRequestForNHLProjections() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("sport", "nhl");
        projectionsHandler.handleRequest(testMap);
        verify(nhlProjections).getProjectionsData();
    }

    @Test
    void shouldHandlePipelineRequestForProjections() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("sport", "nba");
        testMap.put("invocationType", "pipeline");
        projectionsHandler.handleRequest(testMap);
        verify(AWSClient, times(1))
                .uploadToS3("nbaProjectionsData.json", new HashMap<>());
    }
}