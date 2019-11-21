package com.optimizer.handler;

import api.ApiCaller;
import api.ApiClient;
import api.DataCollector;
import collect.DraftKingsData;
import com.optimizer.collect.MockResponses;
import handler.DraftKingsHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DraftKingsHandlerTest implements MockResponses {

//    private DraftKingsHandler draftKingsHandler = mock(DraftKingsHandler.class);
//    private ApiClient mockApi = mock(ApiClient.class);
//    private DraftKingsData draftKingsData = new DraftKingsData(mockApi, "sport");
//
//    @BeforeEach
//    void setUp() {
//        when(draftKingsData.getAllContestData()).thenReturn(fakeDraftKingsData);
//        when(mockApi.getDraftKingsData(anyString())).thenReturn(fakeDraftKingsData);
//    }
//
//    @Test
//    void shouldHandleRequestForDraftKingsData() {
//        Map<String, String> testMap = new HashMap<>();
//        testMap.put("sport", "nfl");
//        List<Map<String, Object>> result = draftKingsHandler.handleRequest(testMap);
//        System.out.println(result);
//        assertTrue(result.size() > 0);
//    }
}