package handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import optimize.Adjuster;
import optimize.LineupCompiler;
import optimize.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import util.AWSClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OptimizerHandlerTest {

    OptimizerHandlerTest() throws IOException {
    }

    private String fakeOptimizerBody = new String(Files.readAllBytes(Paths.get("src/main/resources/optimizerHandlerBody.txt")));
    private String fakeOptimizerWithPipelineInvocationBody = new String(Files.readAllBytes(Paths.get("src/main/resources/optimizerHandlerBodyWithPipelineInvocation.txt")));
    private String fakeOptimizerWhiteAndBlackListBody = new String(Files.readAllBytes(Paths.get("src/main/resources/optimizerHandlerWhiteAndBlackListBody.txt")));
    private Map<String, Object> optimizerInput = new ObjectMapper().readValue(fakeOptimizerBody, new TypeReference<Map<String, Object>>(){});
    private Map<String, Object> optimizerInputWithPipelineInvocation = new ObjectMapper().readValue(fakeOptimizerWithPipelineInvocationBody, new TypeReference<Map<String, Object>>(){});
    private Map<String, Object> optimizerWithWhiteAndBlackListInput = new ObjectMapper().readValue(fakeOptimizerWhiteAndBlackListBody, new TypeReference<Map<String, Object>>(){});

    @Mock
    Adjuster adjuster;

    @Mock
    LineupCompiler lineupCompiler;

    @Mock
    AWSClient AWSClient;

    @InjectMocks
    OptimizerHandler optimizerHandler;

    @Captor
    ArgumentCaptor<List<Player>> playerPoolCaptor1;

    @Captor
    ArgumentCaptor<List<Player>> playerPoolCaptor2;

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
        when(lineupCompiler.outputLineupPlayerIds(anyList(), anyList())).thenReturn(Arrays.asList(1, 2, 3, 4, 5));
    }

    void shouldCollectLineup(List<Player> expectedLineup) {
        verify(adjuster).getWhiteList(playerPoolCaptor1.capture());
        assertEquals(expectedLineup, playerPoolCaptor1.getValue());
    }

    void shouldCollectPlayerPoolAndBlackList(List<Player> expectedBlackList) {
        verify(adjuster).adjustPlayerPool(playerPoolCaptor2.capture(), playerPoolCaptor2.capture(), playerPoolCaptor2.capture());
        List<List<Player>> arguments = playerPoolCaptor2.getAllValues();
        assertEquals(344, arguments.get(0).size());
        assertEquals(expectedBlackList, arguments.get(2));
    }

    void shouldCollectLineupPositions() {
        verify(adjuster).adjustLineupPositions(playerPoolCaptor2.capture(), stringListCaptor.capture());
        assertEquals(Arrays.asList("QB", "RB", "RB", "WR", "WR", "WR", "TE", "RB,WR,TE", "D"), stringListCaptor.getValue());
    }

    void shouldCollectSalaryCap() {
        verify(adjuster).adjustSalaryCap(playerPoolCaptor2.capture(), intCaptor.capture());
        assertEquals(60000, intCaptor.getValue());
    }

    @Test
    void shouldHandleOptimizerInput() {
        List<Integer> result = optimizerHandler.handleRequest(optimizerInput);
        shouldCollectLineup(emptyLineup);
        shouldCollectPlayerPoolAndBlackList(Collections.emptyList());
        shouldCollectLineupPositions();
        shouldCollectSalaryCap();
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result);
        verify(AWSClient, never()).uploadToS3(anyString(), any());
        verify(AWSClient, never()).sendTextMessage(anyList());
    }

    @Test
    void shouldHandleOptimizerWhiteAndBlackListInput() {
        List<Integer> result = optimizerHandler.handleRequest(optimizerWithWhiteAndBlackListInput);
        shouldCollectLineup(whiteListLineup);
        shouldCollectPlayerPoolAndBlackList(Collections.singletonList(new Player(868199)));
        shouldCollectLineupPositions();
        shouldCollectSalaryCap();
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result);
        verify(AWSClient, never()).uploadToS3(anyString(), any());
        verify(AWSClient, never()).sendTextMessage(anyList());
    }

    @Test
    void shouldHandleOptimizerInputWithPipelineInvocation() {
        List<Integer> result = optimizerHandler.handleRequest(optimizerInputWithPipelineInvocation);
        shouldCollectLineup(emptyLineup);
        shouldCollectPlayerPoolAndBlackList(Collections.emptyList());
        shouldCollectLineupPositions();
        shouldCollectSalaryCap();
        assertEquals(Collections.emptyList(), result);
        verify(AWSClient, times(1)).uploadToS3(anyString(), any());
        verify(AWSClient, times(1)).sendTextMessage(anyList());
    }
}
