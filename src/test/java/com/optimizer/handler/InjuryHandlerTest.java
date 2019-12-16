package com.optimizer.handler;

import collect.InjuryData;
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
    InjuryData injuryData;

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
        verify(injuryData).getStandardInjuryData("mlb");
    }

    @Test
    void handleNFLRequest() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("sport", "nfl");
        injuryHandler.handleRequest(testMap);
        verify(injuryData).getNFLInjuryData();
    }

    @Test
    void handleNBARequest() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("sport", "nba");
        injuryHandler.handleRequest(testMap);
        verify(injuryData).getStandardInjuryData("nba");
    }

    @Test
    void handleNHLRequest() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("sport", "nhl");
        injuryHandler.handleRequest(testMap);
        verify(injuryData).getStandardInjuryData("nhl");
    }
}