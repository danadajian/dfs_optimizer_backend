package com.optimizer.optimize;

import optimize.LineupMatrix;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LineupMatrixTest {
    private List<String> positions = Arrays.asList("QB", "RB", "RB", "WR", "WR", "WR", "TE", "RB,WR,TE", "D");

    private LineupMatrix lineupMatrix = new LineupMatrix(positions, 3000000);

    @Test
    void shouldReturnFrequencyOfPosition() {
        int result = lineupMatrix.positionFrequency("WR");
        assertEquals(3, result);
    }

    @Test
    void shouldReturnPositionFrequencies() {
        List<Integer> result = lineupMatrix.positionFrequencies();
        assertEquals(Arrays.asList(-1, 1, 1, 1, 2, 3), result);
    }

    @Test
    void shouldReturnUniquePositions() {
        List<String> result = lineupMatrix.uniquePositions();
        assertEquals(Arrays.asList("QB", "RB", "WR", "TE", "RB,WR,TE", "D"), result);
    }

    @Test
    void shouldReturnValidPositionThresholds() {
        List<Integer> result = lineupMatrix.positionThresholds();
        assertEquals(Arrays.asList(11, 7, 7, 7, 7, 7), result);
    }

    @Test
    void shouldReturnPositionThreshold() {
        int result2 = lineupMatrix.positionThreshold("RB");
        assertEquals(7, result2);
        int result3 = lineupMatrix.positionThreshold("WR");
        assertEquals(7, result3);
        int result4 = lineupMatrix.positionThreshold("TE");
        assertEquals(7, result4);
        int result5 = lineupMatrix.positionThreshold("RB,WR,TE");
        assertEquals(11, result5);
        int result6 = lineupMatrix.positionThreshold("D");
        assertEquals(7, result6);
    }
}
