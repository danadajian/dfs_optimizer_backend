package com.optimizer.handler;

import collect.DraftKingsData;
import com.optimizer.collect.MockResponses;
import handler.DraftKingsHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

class DraftKingsHandlerTest implements MockResponses {

    @Mock
    DraftKingsData draftKingsData;

    @InjectMocks
    DraftKingsHandler draftKingsHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldHandleRequestForDraftKingsData() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("sport", "nfl");
        draftKingsHandler.handleRequest(testMap);
        verify(draftKingsData).getAllContestData("nfl");
    }
}