package com.optimizer.optimize;

import optimize.LineupMatrix;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LineupMatrixTest {
    private List<String> positions = Arrays.asList("QB", "RB", "RB", "WR", "WR", "WR", "TE", "RB,WR,TE", "D");
    private List<String> singleGamePositions = Arrays.asList("any", "any", "any", "any", "any", "any");
    private LineupMatrix lineupMatrix = new LineupMatrix(positions, 3000000);
    private LineupMatrix singleGameMatrix = new LineupMatrix(singleGamePositions, 3000000);
    private LineupMatrix emptyMatrix = new LineupMatrix(Collections.emptyList(), 3000000);

    @Test
    void shouldReturnFrequencyOfPosition() {
        int result = lineupMatrix.positionFrequency("WR");
        assertEquals(3, result);
        int result2 = singleGameMatrix.positionFrequency("any");
        assertEquals(6, result2);
        int result3 = emptyMatrix.positionFrequency("WR");
        assertEquals(0, result3);
    }

    @Test
    void shouldReturnUniquePositions() {
        List<String> result = lineupMatrix.getUniquePositions();
        assertEquals(Arrays.asList("QB", "RB", "WR", "TE", "RB,WR,TE", "D"), result);
        List<String> result2 = singleGameMatrix.getUniquePositions();
        assertEquals(Collections.singletonList("any"), result2);
        List<String> result3 = emptyMatrix.getUniquePositions();
        assertEquals(Collections.emptyList(), result3);
    }

    @Test
    void shouldCalculateTotalCombinations() {
        long result = lineupMatrix.totalCombinations(Arrays.asList(5, 5, 5, 5, 10, 5));
        assertEquals(125000, result);
    }

    @Test
    void shouldReturnValidPositionThresholds() {
        List<Integer> result = lineupMatrix.positionThresholds();
        assertEquals(Arrays.asList(7, 7, 7, 7, 12, 6), result);
        List<Integer> result2 = singleGameMatrix.positionThresholds();
        assertEquals(Collections.singletonList(38), result2);
        List<Integer> result3 = emptyMatrix.positionThresholds();
        assertEquals(Collections.emptyList(), result3);
    }

    @Test
    void shouldReturnPositionThreshold() {
        int result1 = lineupMatrix.positionThreshold("QB");
        assertEquals(7, result1);
        int result2 = lineupMatrix.positionThreshold("RB");
        assertEquals(7, result2);
        int result3 = lineupMatrix.positionThreshold("WR");
        assertEquals(7, result3);
        int result4 = lineupMatrix.positionThreshold("TE");
        assertEquals(7, result4);
        int result5 = lineupMatrix.positionThreshold("RB,WR,TE");
        assertEquals(12, result5);
        int result6 = lineupMatrix.positionThreshold("D");
        assertEquals(6, result6);
        int result7 = singleGameMatrix.positionThreshold("any");
        assertEquals(38, result7);
    }
}
