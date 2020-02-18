package optimize;

import com.google.common.collect.Sets;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class OptimizerTest {
    private Player qb0 = new Player(1, "QB", "team1", 0, 0);
    private Player rb1 = new Player(3, "RB", "team1", 15.0, 5000);
    private Player rb2 = new Player(4, "RB", "team2", 18.9, 8000);
    private Player rb3 = new Player(5, "RB", "team3", 14.0, 7500);
    private Player rb4 = new Player(6, "RB", "team4", 12.7, 7200);
    private Player wr1 = new Player(9, "WR", "team1", 22.9, 10000);
    private Player wr2 = new Player(10, "WR", "team2", 11.6, 7900);
    private Player wr3 = new Player(11, "WR", "team3", 5.5, 8400);
    private Player wr4 = new Player(12, "WR", "team1", 7.9, 3700);
    private Player te1 = new Player(13, "TE", "team1", 10.1, 6700);
    private Player dst1 = new Player(15, "D", "team1", 6.0, 4600);
    private List<Player> playerPool = Arrays.asList(rb1, rb2, rb3, rb4, wr1, wr2, wr3, wr4, te1, dst1);
    int salaryCap = 22700;
    private List<Set<List<Player>>> mockPermutedPlayerPools = Arrays.asList(
            new HashSet<>(Arrays.asList(Collections.singletonList(rb1), Collections.singletonList(rb2),
                    Collections.singletonList(rb3), Collections.singletonList(rb4))),
            new HashSet<>(Arrays.asList(Arrays.asList(wr1, wr2), Arrays.asList(wr1, wr3),
                    Arrays.asList(wr1, wr4), Arrays.asList(wr2, wr3), Arrays.asList(wr2, wr4),
                    Arrays.asList(wr3, wr4)))
    );
    private int mockPositionThreshold = 1;
    private int mockPositionFrequency = 2;

    @Mock
    LineupMatrix lineupMatrix;

    @Mock
    LineupRestrictions lineupRestrictions;

    @InjectMocks
    Optimizer optimizer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(lineupMatrix.getUniquePositions()).thenReturn(Arrays.asList("RB", "WR", "TE", "D"));
        when(lineupMatrix.getPositionThreshold(anyString())).thenReturn(mockPositionThreshold);
        when(lineupMatrix.getPositionFrequency(anyString())).thenReturn(mockPositionFrequency);
        when(lineupRestrictions.getDistinctTeamsRequired()).thenReturn(3);
        when(lineupRestrictions.getMaxPlayersPerTeam()).thenReturn(4);
        when(lineupRestrictions.getTeamAgnosticPosition()).thenReturn("QB");
    }

    @Test
    void shouldReturnTruncatedPools() {
        List<List<Player>> result = optimizer.truncatePlayerPoolsByPosition(playerPool, lineupMatrix);
        assertEquals(4, result.size());
        assertEquals(result.get(0).size(), 4);
        assertEquals(result.get(1).size(), 4);
        assertEquals(result.get(2).size(), 1);
        assertEquals(result.get(3).size(), 1);
    }

    @Test
    void shouldReturnEachSetOfCombinations() {
        List<List<Player>> playerPools = Arrays.asList(
                Arrays.asList(rb1, rb2, rb3, rb4),
                Arrays.asList(wr1, wr2, wr3, wr4)
        );
        List<Set<List<Player>>> result = optimizer.getPlayerPoolCombinations(playerPools, lineupMatrix);
        assertEquals(2, result.size());
        assertEquals(CombinatoricsUtils.binomialCoefficient(4, mockPositionFrequency), result.get(0).size());
        assertEquals(CombinatoricsUtils.binomialCoefficient(4, mockPositionFrequency), result.get(1).size());
    }

    @Test
    void shouldGetCorrectCartesianProductSize() {
        Set<List<List<Player>>> result = Sets.cartesianProduct(mockPermutedPlayerPools);
        assertEquals(CombinatoricsUtils.binomialCoefficient(4, 1) *
                        CombinatoricsUtils.binomialCoefficient(4, 2), result.size());
    }

    @Test
    void shouldCheckIfPlayerHasValidLineupPosition() {
        assertTrue(optimizer.playerHasValidPosition(new Player(0, "G"), "any"));
        assertTrue(optimizer.playerHasValidPosition(new Player(0, "C/1B"), "1B"));
        assertTrue(optimizer.playerHasValidPosition(new Player(0, "SF"), "SF,PF"));
        assertFalse(optimizer.playerHasValidPosition(new Player(0, "RB"), "WR"));
    }

    @Test
    void shouldCorrectlyCheckForDuplicates() {
        boolean result1 = optimizer.areNoDuplicates(Arrays.asList(rb1, rb2, wr2, wr3, te1, rb3, dst1));
        assertTrue(result1);
        boolean result2 = optimizer.areNoDuplicates(Arrays.asList(rb2, rb1, wr3));
        assertTrue(result2);
        boolean result3 = optimizer.areNoDuplicates(Arrays.asList(rb1, rb2, wr2, wr3, te1, rb2, dst1));
        assertFalse(result3);
    }

    @Test
    void shouldCorrectlyCheckIfSatisfiesDistinctTeamsRequired() {
        boolean result1 = optimizer.satisfiesDistinctTeamsRequired(Arrays.asList(rb1, rb2, wr2, wr3, te1, rb3, dst1), lineupRestrictions);
        assertTrue(result1);
        boolean result2 = optimizer.satisfiesDistinctTeamsRequired(Arrays.asList(rb1, rb2, wr1, wr2, te1, wr4, dst1), lineupRestrictions);
        assertFalse(result2);
    }

    @Test
    void shouldCorrectlyCheckIfSatisfiesMaxPlayersPerTeam() {
        boolean result1 = optimizer.satisfiesMaxPlayersPerTeam(Arrays.asList(rb1, rb2, wr2, wr3, te1, rb3, dst1), lineupRestrictions);
        assertTrue(result1);
        boolean result2 = optimizer.satisfiesMaxPlayersPerTeam(Arrays.asList(rb1, rb2, wr1, wr4, te1, rb3, dst1), lineupRestrictions);
        assertFalse(result2);
    }

    @Test
    void shouldCorrectlyCheckIfSatisfiesMaxPlayersPerTeamWithTeamAgnosticPosition() {
        boolean result1 = optimizer.satisfiesMaxPlayersPerTeam(Arrays.asList(qb0, rb2, wr1, wr4, te1, rb3, dst1), lineupRestrictions);
        assertTrue(result1);
        boolean result2 = optimizer.satisfiesMaxPlayersPerTeam(Arrays.asList(qb0, rb1, wr1, wr4, te1, rb3, dst1), lineupRestrictions);
        assertFalse(result2);
    }

    @Test
    void shouldCorrectlyCheckIfLineupIsBetter() {
        boolean result1 = optimizer.lineupIsBetter(Arrays.asList(rb1, rb2, wr2, wr3, te1, rb3, dst1), 69000, 6.9);
        assertTrue(result1);
        boolean result2 = optimizer.lineupIsBetter(Arrays.asList(rb2, rb1, wr3), 69000, 69.0);
        assertFalse(result2);
        boolean result3 = optimizer.lineupIsBetter(Arrays.asList(rb2, rb1, wr3), 69, 6.9);
        assertFalse(result3);
    }

    @Test
    void shouldReturnBestLineupInCartesianProduct() {
        List<Player> result = optimizer.getBestLineupInCartesianProduct(mockPermutedPlayerPools, salaryCap, lineupRestrictions);
        assertEquals(Arrays.asList(rb3, wr2, wr4), result);
    }
}
