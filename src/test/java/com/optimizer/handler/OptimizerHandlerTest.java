package com.optimizer.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import handler.OptimizerHandler;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class OptimizerHandlerTest {

    OptimizerHandlerTest() throws IOException {
    }

    private String fakeOptimizerBody = new String(Files.readAllBytes(Paths.get("src/main/resources/optimizerHandlerBody.txt")));
    private String fakeOptimizerWhiteAndBlackListBody = new String(Files.readAllBytes(Paths.get("src/main/resources/optimizerHandlerWhiteAndBlackListBody.txt")));
    private String fakeOptimizerSingleGameBody = new String(Files.readAllBytes(Paths.get("src/main/resources/optimizerHandlerSingleGameBody.txt")));
    private String fakeOptimizerSingleGameWhiteAndBlackListBody = new String(Files.readAllBytes(Paths.get("src/main/resources/optimizerHandlerSingleGameWhiteAndBlackListBody.txt")));

    private OptimizerHandler optimizerHandler = new OptimizerHandler();
    private Map<String, Object> testOptimizer = new ObjectMapper().readValue(fakeOptimizerBody, new TypeReference<Map<String, Object>>(){});
    private Map<String, Object> testOptimizerWhiteAndBlackList = new ObjectMapper().readValue(fakeOptimizerWhiteAndBlackListBody, new TypeReference<Map<String, Object>>(){});
    private Map<String, Object> testOptimizerSingleGame = new ObjectMapper().readValue(fakeOptimizerSingleGameBody, new TypeReference<Map<String, Object>>(){});
    private Map<String, Object> testOptimizerSingleGameWhiteAndBlackList = new ObjectMapper().readValue(fakeOptimizerSingleGameWhiteAndBlackListBody, new TypeReference<Map<String, Object>>(){});

    @Test
    void shouldHandleOptimizerInput() {
        Set<Integer> result = new HashSet<>(optimizerHandler.handleRequest(testOptimizer));
        assertEquals(new HashSet<>(Arrays.asList(828743, 750846, 747861, 653699, 877790, 884013, 600191, 592914, 323)),
                result);
    }

    @Test
    void shouldHandleOptimizerWhiteAndBlackListInput() {
        Set<Integer> result = new HashSet<>(optimizerHandler.handleRequest(testOptimizerWhiteAndBlackList));
        assertEquals(new HashSet<>(Arrays.asList(828743, 456613, 750846, 653699, 877790, 821389, 600191, 592914, 323)),
                result);
        assertTrue(result.contains(456613));
        assertFalse(result.contains(868199));
        assertFalse(result.contains(0));
    }

    @Test
    void shouldHandleOptimizerInputSingleGame() {
        List<Integer> result = optimizerHandler.handleRequest(testOptimizerSingleGame);
        assertEquals(Arrays.asList(877745, 868199, 473742, 448132, 608753), result);
    }

    @Test
    void shouldHandleOptimizerSingleGameWhiteAndBlackListInput() {
        List<Integer> result = optimizerHandler.handleRequest(testOptimizerSingleGameWhiteAndBlackList);
        assertEquals(Arrays.asList(456613, 877745, 473742, 448132, 591586), result);
        assertTrue(result.contains(456613));
        assertFalse(result.contains(868199));
        assertFalse(result.contains(406186));
    }
}
