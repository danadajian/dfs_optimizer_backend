package handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import optimize.*;
import org.junit.jupiter.api.AfterEach;
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

    String fakeOptimizerBody = new String(Files.readAllBytes(Paths.get("src/main/resources/optimizerHandlerBody.json")));
    String fakeOptimizerWithPipelineInvocationBody = new String(Files.readAllBytes(Paths.get("src/main/resources/optimizerHandlerBodyWithPipelineInvocation.json")));
    Map<String, Object> optimizerInput = new ObjectMapper().readValue(fakeOptimizerBody, new TypeReference<Map<String, Object>>(){});
    Map<String, Object> optimizerInputWithPipelineInvocation = new ObjectMapper().readValue(fakeOptimizerWithPipelineInvocationBody, new TypeReference<Map<String, Object>>(){});

    @Mock
    InputParser inputParser;

    @Mock
    Adjuster adjuster;

    @Mock
    PlayerSelector playerSelector;

    @Mock
    LineupCompiler lineupCompiler;

    @Mock
    Optimizer optimizer;

    @Mock
    AWSClient awsClient;

    @InjectMocks
    OptimizerHandler optimizerHandler;

    List<Player> mockLineup = Collections.emptyList();
    List<Player> mockPlayerPool = Collections.emptyList();
    List<Player> mockWhiteList = Collections.emptyList();
    List<Player> mockBlackList = Collections.emptyList();
    List<String> mockLineupPositions = Collections.emptyList();
    LineupRestrictions mockLineupRestrictions = mock(LineupRestrictions.class);
    List<Player> mockAdjustedPlayerPool = Collections.emptyList();
    List<String> mockAdjustedLineupPositions = Collections.emptyList();
    LineupRestrictions mockAdjustedLineupRestrictions = mock(LineupRestrictions.class);
    int mockAdjustedSalaryCap = 69;
    LineupMatrix lineupMatrix = mock(LineupMatrix.class);
    List<List<Player>> mockTruncatedPlayerPools = Collections.emptyList();
    List<Set<List<Player>>> mockPermutedPlayerPools = Collections.emptyList();

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(inputParser.getLineup(anyMap())).thenReturn(mockLineup);
        when(awsClient.downloadFromS3(anyString())).thenReturn((List<Map<String, Object>>) optimizerInput.get("playerPool"));
        when(inputParser.getPlayerPool(anyList())).thenReturn(mockPlayerPool);
        when(inputParser.getBlackList(anyMap())).thenReturn(mockBlackList);
        when(inputParser.getLineupPositions(anyMap())).thenReturn(mockLineupPositions);
        when(inputParser.getLineupRestrictions(anyMap())).thenReturn(mockLineupRestrictions);
        when(adjuster.getWhiteList(anyList())).thenReturn(mockWhiteList);
        when(adjuster.adjustPlayerPool(anyList(), anyList(), anyList())).thenReturn(mockAdjustedPlayerPool);
        when(adjuster.adjustLineupPositions(anyList(), anyList())).thenReturn(mockAdjustedLineupPositions);
        when(adjuster.adjustLineupRestrictions(any(), anyList())).thenReturn(mockAdjustedLineupRestrictions);
        when(adjuster.adjustSalaryCap(anyList(), anyInt())).thenReturn(mockAdjustedSalaryCap);
        when(playerSelector.getLineupMatrix(anyList(), anyList(), anyLong())).thenReturn(lineupMatrix);
        when(playerSelector.truncatePlayerPoolsByPosition(anyList(), any())).thenReturn(mockTruncatedPlayerPools);
        when(playerSelector.getPlayerPoolCombinations(anyList(), any())).thenReturn(mockPermutedPlayerPools);
        when(lineupCompiler.outputLineupPlayerIds(anyList(), anyList())).thenReturn(Arrays.asList(1, 2, 3, 4, 5));
    }

    @Test
    void shouldHandleOptimizerInput() {
        List<Integer> result = optimizerHandler.handleRequest(optimizerInput);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result);
        verify(adjuster).adjustSalaryCap(mockWhiteList, (int) optimizerInput.get("salaryCap"));
        verify(playerSelector).getLineupMatrix(mockAdjustedLineupPositions, mockAdjustedPlayerPool, (long) optimizerInput.get("maxCombinations"));
        verify(awsClient, never()).uploadToS3(anyString(), any());
        verify(awsClient, never()).downloadFromS3(anyString());
    }

    @Test
    void shouldHandleOptimizerInputWithPipelineInvocation() {
        List<Integer> result = optimizerHandler.handleRequest(optimizerInputWithPipelineInvocation);
        assertEquals(Collections.emptyList(), result);
        verify(adjuster).adjustSalaryCap(mockWhiteList, (int) optimizerInputWithPipelineInvocation.get("salaryCap"));
        verify(playerSelector).getLineupMatrix(mockAdjustedLineupPositions, mockAdjustedPlayerPool, (int) optimizerInputWithPipelineInvocation.get("maxCombinations"));
        verify(awsClient, times(1)).uploadToS3(anyString(), any());
        verify(awsClient, times(1)).downloadFromS3(anyString());
    }

    @AfterEach
    void tearDown() {
        verify(adjuster).getWhiteList(mockLineup);
        verify(adjuster).adjustPlayerPool(mockPlayerPool, mockWhiteList, mockBlackList);
        verify(adjuster).adjustLineupPositions(mockLineup, mockLineupPositions);
        verify(adjuster).adjustLineupRestrictions(mockLineupRestrictions, mockWhiteList);
        verify(playerSelector).truncatePlayerPoolsByPosition(mockAdjustedPlayerPool, lineupMatrix);
        verify(playerSelector).getPlayerPoolCombinations(mockTruncatedPlayerPools, lineupMatrix);
        verify(optimizer).generateOptimalLineup(mockPermutedPlayerPools, mockAdjustedSalaryCap, mockAdjustedLineupRestrictions);
    }
}
