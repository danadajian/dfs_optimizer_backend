package com.optimizer.handler;

import collect.stats.NFLProjections;
import collect.stats.NHLProjections;
import collect.stats.StandardProjections;
import handler.ProjectionsHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    @InjectMocks
    ProjectionsHandler projectionsHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldHandleRequestForMLBProjections() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("sport", "mlb");
        projectionsHandler.handleRequest(testMap);
        verify(standardProjectionsData).getProjectionsData("mlb");
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
}