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
    private Map<String, Object> optimizerInput = new ObjectMapper().readValue(fakeOptimizerBody, new TypeReference<Map<String, Object>>(){});
    private Map<String, Object> optimizerWithWhiteAndBlackListInput = new ObjectMapper().readValue(fakeOptimizerWhiteAndBlackListBody, new TypeReference<Map<String, Object>>(){});

    @Mock
    Adjuster adjuster;

    @Mock
    LineupCompiler lineupCompiler;

    @InjectMocks
    OptimizerHandler optimizerHandler;

    @Captor
    ArgumentCaptor<List<Player>> playerListCaptor1;

    @Captor
    ArgumentCaptor<List<Player>> playerListCaptor2;

    @Captor
    ArgumentCaptor<List<String>> stringListCaptor;

    @Captor
    ArgumentCaptor<Integer> intCaptor;

    Player emptyPlayer = new Player();
    List<Player> emptyLineup = Arrays.asList(emptyPlayer, emptyPlayer, emptyPlayer, emptyPlayer, emptyPlayer,
            emptyPlayer, emptyPlayer, emptyPlayer, emptyPlayer);
    List<Player> whiteListLineup = Arrays.asList(emptyPlayer, new Player(456613), emptyPlayer, emptyPlayer,
            emptyPlayer, emptyPlayer, emptyPlayer, emptyPlayer, emptyPlayer);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(lineupCompiler.outputLineup(anyList(), anyList())).thenReturn(Arrays.asList(1, 2, 3, 4, 5));
    }

    void shouldCollectLineup(List<Player> expectedLineup) {
        verify(adjuster).getWhiteList(playerListCaptor1.capture());
        assertEquals(expectedLineup, playerListCaptor1.getValue());
    }

    void shouldCollectPlayerListAndBlackList(List<Player> expectedBlackList) {
        verify(adjuster).adjustedPlayerList(playerListCaptor2.capture(), playerListCaptor2.capture(), playerListCaptor2.capture());
        List<List<Player>> arguments = playerListCaptor2.getAllValues();
        assertEquals(344, arguments.get(0).size());
        assertEquals(expectedBlackList, arguments.get(2));
    }

    void shouldCollectLineupPositions() {
        verify(adjuster).adjustedLineupPositions(playerListCaptor2.capture(), stringListCaptor.capture());
        assertEquals(Arrays.asList("QB", "RB", "RB", "WR", "WR", "WR", "TE", "RB,WR,TE", "D"), stringListCaptor.getValue());
    }

    void shouldCollectSalaryCap() {
        verify(adjuster).adjustedSalaryCap(playerListCaptor2.capture(), intCaptor.capture());
        assertEquals(60000, intCaptor.getValue());
    }

    @Test
    void shouldHandleOptimizerInput() {
        List<Integer> result = optimizerHandler.handleRequest(optimizerInput);
        shouldCollectLineup(emptyLineup);
        shouldCollectPlayerListAndBlackList(Collections.emptyList());
        shouldCollectLineupPositions();
        shouldCollectSalaryCap();
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result);
    }

    @Test
    void shouldHandleOptimizerWhiteAndBlackListInput() {
        List<Integer> result = optimizerHandler.handleRequest(optimizerWithWhiteAndBlackListInput);
        shouldCollectLineup(whiteListLineup);
        shouldCollectPlayerListAndBlackList(Collections.singletonList(new Player(868199)));
        shouldCollectLineupPositions();
        shouldCollectSalaryCap();
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result);
    }
}
