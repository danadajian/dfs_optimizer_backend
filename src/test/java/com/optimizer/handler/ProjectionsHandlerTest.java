package com.optimizer.handler;

import collect.ProjectionsData;
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
    ProjectionsData projectionsData;

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
        verify(projectionsData).getStandardFantasyProjections("mlb");
    }

    @Test
    void shouldHandleRequestForNFLProjections() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("sport", "nfl");
        projectionsHandler.handleRequest(testMap);
        verify(projectionsData).getNFLFantasyProjections();
    }

    @Test
    void shouldHandleRequestForNBAProjections() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("sport", "nba");
        projectionsHandler.handleRequest(testMap);
        verify(projectionsData).getStandardFantasyProjections("nba");
    }

    @Test
    void shouldHandleRequestForNHLProjections() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("sport", "nhl");
        projectionsHandler.handleRequest(testMap);
        verify(projectionsData).getNHLFantasyProjections();
    }
}