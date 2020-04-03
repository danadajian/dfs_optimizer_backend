package optimize;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LineupMatrixTest {
    private List<String> positions = Arrays.asList("QB", "RB", "RB", "WR", "WR", "WR", "TE", "RB,WR,TE", "D");
    private List<String> singleGamePositions = Arrays.asList("any", "any", "any", "any", "any", "any");
    private List<Player> playerPool = Arrays.asList(
            new Player(1, "QB"),
            new Player(2, "RB"),
            new Player(3, "RB"),
            new Player(4, "WR"),
            new Player(5, "WR"),
            new Player(6, "WR"),
            new Player(7, "TE"),
            new Player(8, "RB,WR,TE"),
            new Player(9, "D")
    );
    private LineupMatrix lineupMatrix = new LineupMatrix(positions, playerPool, 10);
    private LineupMatrix singleGameMatrix = new LineupMatrix(singleGamePositions, playerPool, 3000000);
    private LineupMatrix emptyMatrix = new LineupMatrix(Collections.emptyList(), playerPool, 3000000);

    @Test
    void shouldReturnFrequencyOfPositionInLineup() {
        int result = lineupMatrix.getPositionFrequency("WR");
        assertEquals(3, result);
        int result2 = singleGameMatrix.getPositionFrequency("any");
        assertEquals(6, result2);
        int result3 = emptyMatrix.getPositionFrequency("WR");
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
    void shouldReturnPositionCounts() {
        List<Integer> result = lineupMatrix.getPositionCounts(playerPool);
        assertEquals(Arrays.asList(1, 2, 3, 1, 6, 1), result);
        List<Integer> result2 = singleGameMatrix.getPositionCounts(playerPool);
        assertEquals(Collections.singletonList(6), result2);
        List<Integer> result3 = emptyMatrix.getPositionCounts(playerPool);
        assertEquals(Collections.emptyList(), result3);
    }

    @Test
    void shouldCalculateTotalCombinations() {
        long result = lineupMatrix.getAllCombinations(Arrays.asList(5, 5, 5, 5, 10, 5));
        assertEquals(125000, result);
    }

    @Test
    void shouldReturnValidPositionThresholds() {
        List<Integer> result = lineupMatrix.getPositionThresholds(Arrays.asList(20, 35, 40, 17, 58, 24), 3000000);
        assertEquals(Arrays.asList(7, 7, 7, 8, 8, 8), result);
        List<Integer> result2 = singleGameMatrix.getPositionThresholds(Collections.singletonList(50), 3000000);
        assertEquals(Collections.singletonList(38), result2);
        List<Integer> result3 = emptyMatrix.getPositionThresholds(Collections.emptyList(), 3000000);
        assertEquals(Collections.emptyList(), result3);
    }

    @Test
    void shouldReturnPositionThreshold() {
        int result1 = lineupMatrix.getPositionThreshold("QB");
        assertEquals(1, result1);
        int result2 = lineupMatrix.getPositionThreshold("RB");
        assertEquals(2, result2);
        int result3 = lineupMatrix.getPositionThreshold("WR");
        assertEquals(3, result3);
        int result4 = lineupMatrix.getPositionThreshold("TE");
        assertEquals(1, result4);
        int result5 = lineupMatrix.getPositionThreshold("RB,WR,TE");
        assertEquals(6, result5);
        int result6 = lineupMatrix.getPositionThreshold("D");
        assertEquals(1, result6);
        int result7 = singleGameMatrix.getPositionThreshold("any");
        assertEquals(6, result7);
    }
}
