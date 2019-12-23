package com.optimizer.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import handler.OptimizerHandler;
import optimize.Adjuster;
import optimize.LineupCompiler;
import optimize.Optimizer;
import optimize.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OptimizerHandlerTest {

    OptimizerHandlerTest() throws IOException {
    }

    private String fakeOptimizerBody = new String(Files.readAllBytes(Paths.get("src/main/resources/optimizerHandlerBody.txt")));
    private String fakeOptimizerWhiteAndBlackListBody = new String(Files.readAllBytes(Paths.get("src/main/resources/optimizerHandlerWhiteAndBlackListBody.txt")));
    private String fakeOptimizerSingleGameBody = new String(Files.readAllBytes(Paths.get("src/main/resources/optimizerHandlerSingleGameBody.txt")));
    private String fakeOptimizerSingleGameWhiteAndBlackListBody = new String(Files.readAllBytes(Paths.get("src/main/resources/optimizerHandlerSingleGameWhiteAndBlackListBody.txt")));

    private Map<String, Object> optimizerInput = new ObjectMapper().readValue(fakeOptimizerBody, new TypeReference<Map<String, Object>>(){});
    private Map<String, Object> optimizerWithWhiteAndBlackListInput = new ObjectMapper().readValue(fakeOptimizerWhiteAndBlackListBody, new TypeReference<Map<String, Object>>(){});
    private Map<String, Object> optimizerSingleGameInput = new ObjectMapper().readValue(fakeOptimizerSingleGameBody, new TypeReference<Map<String, Object>>(){});
    private Map<String, Object> optimizerSingleGameWhiteAndBlackListInput = new ObjectMapper().readValue(fakeOptimizerSingleGameWhiteAndBlackListBody, new TypeReference<Map<String, Object>>(){});

    @Mock
    Adjuster adjuster;

    @Mock
    Optimizer optimizer;

    @Mock
    LineupCompiler lineupCompiler;

    @InjectMocks
    OptimizerHandler optimizerHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(adjuster.getWhiteList(anyList())).thenReturn(Collections.emptyList());
        when(adjuster.adjustedPlayerList(anyList(), anyList(), anyList())).thenReturn(Collections.emptyList());
        when(adjuster.adjustedLineupPositions(anyList(), anyList())).thenReturn(Collections.emptyList());
        when(adjuster.adjustedSalaryCap(anyList(), anyInt())).thenReturn(0);
        when(optimizer.generateOptimalPlayers(anyList(), any(), anyInt())).thenReturn(Collections.emptyList());
        when(lineupCompiler.outputLineup(anyList(), anyList())).thenReturn(Arrays.asList(1, 2, 3, 4, 5));
    }

    @Test
    void shouldHandleOptimizerInput() {
        List<Integer> result = optimizerHandler.handleRequest(optimizerInput);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result);
    }

    @Test
    void shouldHandleOptimizerWhiteAndBlackListInput() {
        List<Integer> result = optimizerHandler.handleRequest(optimizerWithWhiteAndBlackListInput);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result);
    }

    @Test
    void shouldHandleOptimizerInputSingleGame() {
        List<Integer> result = optimizerHandler.handleRequest(optimizerSingleGameInput);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result);
    }

    @Test
    void shouldHandleOptimizerSingleGameWhiteAndBlackListInput() {
        List<Integer> result = optimizerHandler.handleRequest(optimizerSingleGameWhiteAndBlackListInput);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result);
    }
}
