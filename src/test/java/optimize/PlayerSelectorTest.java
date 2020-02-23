package optimize;

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

class PlayerSelectorTest {

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

    private List<Player> playerPool = Arrays.asList(qb0, rb1, rb2, rb3, rb4, wr1, wr2, wr3, wr4, te1, dst1);
    private int mockPositionFrequency = 2;

    @Mock
    LineupMatrix lineupMatrix;

    @Mock
    LineupRestrictions lineupRestrictions;

    @InjectMocks
    PlayerSelector playerSelector;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(lineupMatrix.getUniquePositions()).thenReturn(Arrays.asList("RB", "WR", "TE", "D"));
        when(lineupMatrix.getPositionThreshold(anyString())).thenReturn(1);
        when(lineupMatrix.getPositionFrequency(anyString())).thenReturn(mockPositionFrequency);
        when(lineupRestrictions.getDistinctTeamsRequired()).thenReturn(3);
        when(lineupRestrictions.getMaxPlayersPerTeam()).thenReturn(4);
        when(lineupRestrictions.getTeamAgnosticPosition()).thenReturn("QB");
    }

    @Test
    void shouldReturnTruncatedPools() {
        List<List<Player>> result = playerSelector.truncatePlayerPoolsByPosition(playerPool, lineupMatrix);
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
        List<Set<List<Player>>> result = playerSelector.getPlayerPoolCombinations(playerPools, lineupMatrix);
        assertEquals(2, result.size());
        assertEquals(CombinatoricsUtils.binomialCoefficient(4, mockPositionFrequency), result.get(0).size());
        assertEquals(CombinatoricsUtils.binomialCoefficient(4, mockPositionFrequency), result.get(1).size());
    }

    @Test
    void shouldCheckIfPlayerHasValidLineupPosition() {
        assertTrue(playerSelector.playerHasValidPosition(new Player(0, "G"), "any"));
        assertTrue(playerSelector.playerHasValidPosition(new Player(0, "C/1B"), "1B"));
        assertTrue(playerSelector.playerHasValidPosition(new Player(0, "SF"), "SF,PF"));
        assertFalse(playerSelector.playerHasValidPosition(new Player(0, "RB"), "WR"));
    }
}