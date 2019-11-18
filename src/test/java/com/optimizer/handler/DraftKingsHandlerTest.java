package com.optimizer.handler;

import handler.DraftKingsHandler;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DraftKingsHandlerTest {

    @Test
    void shouldHandleRequestForDraftKingsData() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("test", "test");
        List<Map<String, Object>> result = new DraftKingsHandler().handleRequest(testMap);
        System.out.println(result);
        assertTrue(result.size() > 0);
    }
}