package optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
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

    private List<Set<List<Player>>> playerPools = Arrays.asList(
            Stream.of(Collections.singletonList(qb0)).collect(Collectors.toSet()),
            Stream.of(Arrays.asList(rb1, rb2), Arrays.asList(rb1, rb3), Arrays.asList(rb1, rb4), Arrays.asList(rb2, rb3), Arrays.asList(rb2, rb4), Arrays.asList(rb3, rb4)).collect(Collectors.toSet()),
            Stream.of(Arrays.asList(wr1, wr2, wr3), Arrays.asList(wr1, wr2, wr4), Arrays.asList(wr1, wr3, wr4), Arrays.asList(wr2, wr3, wr4)).collect(Collectors.toSet()),
            Stream.of(Collections.singletonList(te1)).collect(Collectors.toSet()),
            Stream.of(Collections.singletonList(dst1)).collect(Collectors.toSet())
    );

    @Mock
    LineupRestrictions lineupRestrictions;

    @InjectMocks
    Optimizer optimizer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(lineupRestrictions.getDistinctTeamsRequired()).thenReturn(3);
        when(lineupRestrictions.getMaxPlayersPerTeam()).thenReturn(4);
        when(lineupRestrictions.getTeamAgnosticPosition()).thenReturn("QB");
    }

    @Test
    void shouldDetermineBetterLineupCanBeFound() {
        List<Player> currentLineup = Arrays.asList(qb0, rb2, rb3, wr1, wr2, wr4);
        assertTrue(optimizer.canFindABetterLineup(currentLineup, playerPools, 70, 50000));
    }

    @Test
    void shouldDetermineBetterLineupCannotBeFoundDueToMaxPoints() {
        List<Player> currentLineup = Arrays.asList(qb0, rb2, rb3, wr1, wr2, wr4);
        assertFalse(optimizer.canFindABetterLineup(currentLineup, playerPools, 91.5, 50000));
    }

    @Test
    void shouldDetermineBetterLineupCannotBeFoundDueToSalary() {
        List<Player> currentLineup = Arrays.asList(qb0, rb2, rb3, wr1, wr2, wr3);
        assertFalse(optimizer.canFindABetterLineup(currentLineup, playerPools, 0, 53000));
    }

    @Test
    void shouldGetDistinctNumberOfPositionTypesFilled() {
        List<Player> currentLineup = Arrays.asList(rb1, rb2, rb3, wr1, wr2, wr3, wr4, te1);
        int result = optimizer.getDistinctNumberOfPositionTypesFilled(currentLineup);
        assertEquals(result, 3);
    }

    @Test
    void shouldGetDistinctNumberOfPositionTypesFilledWithFlex() {
        List<Player> currentLineup = Arrays.asList(qb0, rb2, rb3, wr1, wr2, wr3, te1, rb1, dst1);
        int result = optimizer.getDistinctNumberOfPositionTypesFilled(currentLineup);
        assertEquals(result, 6);
    }

    @Test
    void shouldGetMaxPointsToAdd() {
        List<Set<List<Player>>> remainingPlayerPools = Arrays.asList(
                Stream.of(Arrays.asList(wr1, wr2, wr3), Arrays.asList(wr1, wr2, wr4), Arrays.asList(wr1, wr3, wr4), Arrays.asList(wr2, wr3, wr4)).collect(Collectors.toSet()),
                Stream.of(Collections.singletonList(te1)).collect(Collectors.toSet()),
                Stream.of(Collections.singletonList(dst1)).collect(Collectors.toSet())
        );
        double result = optimizer.getMaxPointsToAdd(remainingPlayerPools);
        assertEquals(result, 58.5);
    }

    @Test
    void shouldGetMinSalaryToAdd() {
        List<Set<List<Player>>> remainingPlayerPools = Arrays.asList(
                Stream.of(Arrays.asList(wr1, wr2, wr3), Arrays.asList(wr1, wr2, wr4), Arrays.asList(wr1, wr3, wr4), Arrays.asList(wr2, wr3, wr4)).collect(Collectors.toSet()),
                Stream.of(Collections.singletonList(te1)).collect(Collectors.toSet()),
                Stream.of(Collections.singletonList(dst1)).collect(Collectors.toSet())
        );
        int result = optimizer.getMinSalaryToAdd(remainingPlayerPools);
        assertEquals(result, 31300);
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
    void shouldCalculateTotalProjection() {
        double result = optimizer.totalProjection(Arrays.asList(rb1, rb2, rb3));
        assertEquals(result, 47.9);
    }

    @Test
    void shouldCalculateTotalSalary() {
        int result = optimizer.totalSalary(Arrays.asList(rb1, rb2, rb3));
        assertEquals(result, 20500);
    }

    @Test
    void shouldGenerateOptimalLineup() {
        List<Player> result = optimizer.generateOptimalLineup(playerPools, 50000, lineupRestrictions);
        assertEquals(result, Arrays.asList(qb0, rb2, rb3, wr1, wr2, wr4, te1, dst1));
    }
}
