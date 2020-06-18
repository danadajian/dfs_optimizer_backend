package optimize;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OptimizerTest {
    Player qb0 = new Player(1, "QB", "team1", 0, 0);
    Player rb1 = new Player(3, "RB", "team1", 15.0, 5000);
    Player rb2 = new Player(4, "RB", "team2", 18.9, 8000);
    Player rb3 = new Player(5, "RB", "team3", 14.0, 7500);
    Player rb4 = new Player(6, "RB", "team4", 12.7, 7200);
    Player wr1 = new Player(9, "WR", "team1", 22.9, 10000);
    Player wr2 = new Player(10, "WR", "team2", 11.6, 7900);
    Player wr3 = new Player(11, "WR", "team3", 5.5, 8400);
    Player wr4 = new Player(12, "WR", "team1", 7.9, 3700);
    Player te1 = new Player(13, "TE", "team1", 10.1, 6700);
    Player dst1 = new Player(15, "D", "team1", 6.0, 4600);

    List<Set<List<Player>>> playerPools = Arrays.asList(
            Stream.of(Collections.singletonList(qb0)).collect(Collectors.toSet()),
            Stream.of(Arrays.asList(rb1, rb2), Arrays.asList(rb1, rb3), Arrays.asList(rb1, rb4), Arrays.asList(rb2, rb3), Arrays.asList(rb2, rb4), Arrays.asList(rb3, rb4)).collect(Collectors.toSet()),
            Stream.of(Arrays.asList(wr1, wr2, wr3), Arrays.asList(wr1, wr2, wr4), Arrays.asList(wr1, wr3, wr4), Arrays.asList(wr2, wr3, wr4)).collect(Collectors.toSet()),
            Stream.of(Collections.singletonList(te1)).collect(Collectors.toSet()),
            Stream.of(Collections.singletonList(dst1)).collect(Collectors.toSet())
    );

    Optimizer optimizer = mock(Optimizer.class);
    LineupRestrictions lineupRestrictions = mock(LineupRestrictions.class);
    LineupValidator lineupValidator = mock(LineupValidator.class);

    @BeforeEach
    void setUp() {
        doCallRealMethod().when(optimizer).setSalaryCap(anyInt());
        doCallRealMethod().when(optimizer).setMaxPoints(anyDouble());
        doCallRealMethod().when(optimizer).setPlayerPools(anyList());
        doCallRealMethod().when(optimizer).setLineupValidator(any());
        when(optimizer.getMaxPoints()).thenCallRealMethod();
        when(optimizer.getOptimalLineup()).thenCallRealMethod();
    }

    @AfterEach
    void tearDown() {
        reset(optimizer, lineupRestrictions, lineupValidator);
    }

    @Test
    void shouldSetNewMaxPointsAndLineup() {
        doCallRealMethod().when(optimizer).optimize(anyList(), anyInt());
        when(optimizer.lineupIsBetter(anyList())).thenReturn(true);
        when(lineupValidator.lineupSatisfiesDistinctTeamsRequired(anyList(), any())).thenReturn(true);
        when(optimizer.totalProjection(anyList())).thenReturn(6.9);
        optimizer.setPlayerPools(playerPools);
        optimizer.setLineupValidator(lineupValidator);
        optimizer.optimize(Arrays.asList(qb0, rb1, rb2), 4);
        assertEquals(optimizer.getMaxPoints(), 6.9);
        assertEquals(optimizer.getOptimalLineup(), Arrays.asList(qb0, rb1, rb2));
        verify(optimizer, never()).recursivelyCheckLineups(anyList(), anyInt());
    }

    @Test
    void shouldContinueCheckingLineups() {
        doCallRealMethod().when(optimizer).optimize(anyList(), anyInt());
        when(optimizer.lineupIsBetter(anyList())).thenReturn(true);
        when(lineupValidator.lineupContainsNoDuplicates(anyList())).thenReturn(true);
        when(lineupValidator.lineupSatisfiesMaxPlayersPerTeam(anyList(), any())).thenReturn(true);
        when(optimizer.totalProjection(anyList())).thenReturn(6.9);
        optimizer.setPlayerPools(playerPools);
        optimizer.setLineupValidator(lineupValidator);
        optimizer.optimize(Arrays.asList(qb0, rb1, rb2), 1);
        assertEquals(optimizer.getMaxPoints(), 0);
        assertNull(optimizer.getOptimalLineup());
        verify(optimizer, times(1)).recursivelyCheckLineups(Arrays.asList(qb0, rb1, rb2), 2);
    }

    @Test
    void shouldRecursivelyCheckLineups() {
        when(optimizer.canFindABetterLineup(anyList(), anyList())).thenReturn(true);
        when(lineupValidator.lineupContainsNoDuplicates(anyList())).thenReturn(true);
        when(lineupValidator.lineupSatisfiesMaxPlayersPerTeam(anyList(), any())).thenReturn(true);
        doNothing().when(optimizer).optimize(anyList(), anyInt());
        doCallRealMethod().when(optimizer).recursivelyCheckLineups(anyList(), anyInt());
        optimizer.setPlayerPools(playerPools);
        optimizer.setLineupValidator(lineupValidator);
        optimizer.recursivelyCheckLineups(Collections.singletonList(qb0), 1);
        verify(optimizer, times(1)).optimize(Arrays.asList(qb0, rb1, rb3), 1);
        verify(optimizer, times(1)).optimize(Arrays.asList(qb0, rb1, rb4), 1);
        verify(optimizer, times(1)).optimize(Arrays.asList(qb0, rb2, rb3), 1);
        verify(optimizer, times(1)).optimize(Arrays.asList(qb0, rb2, rb4), 1);
        verify(optimizer, times(1)).optimize(Arrays.asList(qb0, rb3, rb4), 1);
    }

    @Test
    void shouldDetermineBetterLineupCanBeFound() {
        when(optimizer.getMaxPointsToAdd(anyList())).thenReturn(4.0);
        when(optimizer.getMinSalaryToAdd(anyList())).thenReturn(2);
        when(optimizer.totalProjection(anyList())).thenReturn(3.0);
        when(optimizer.totalSalary(anyList())).thenReturn(2);
        when(optimizer.canFindABetterLineup(anyList(), any())).thenCallRealMethod();
        optimizer.setSalaryCap(5);
        optimizer.setMaxPoints(6.9);
        assertTrue(optimizer.canFindABetterLineup(Collections.emptyList(), Collections.emptyList()));
    }

    @Test
    void shouldDetermineBetterLineupCannotBeFoundDueToMaxPoints() {
        when(optimizer.getMaxPointsToAdd(anyList())).thenReturn(3.0);
        when(optimizer.getMinSalaryToAdd(anyList())).thenReturn(2);
        when(optimizer.totalProjection(anyList())).thenReturn(3.0);
        when(optimizer.totalSalary(anyList())).thenReturn(2);
        when(optimizer.canFindABetterLineup(anyList(), any())).thenCallRealMethod();
        optimizer.setSalaryCap(5);
        optimizer.setMaxPoints(6.9);
        assertFalse(optimizer.canFindABetterLineup(Collections.emptyList(), Collections.emptyList()));
    }

    @Test
    void shouldDetermineBetterLineupCannotBeFoundDueToSalary() {
        when(optimizer.getMaxPointsToAdd(anyList())).thenReturn(4.0);
        when(optimizer.getMinSalaryToAdd(anyList())).thenReturn(4);
        when(optimizer.totalProjection(anyList())).thenReturn(3.0);
        when(optimizer.totalSalary(anyList())).thenReturn(2);
        when(optimizer.canFindABetterLineup(anyList(), any())).thenCallRealMethod();
        optimizer.setSalaryCap(5);
        optimizer.setMaxPoints(6.9);
        assertFalse(optimizer.canFindABetterLineup(Collections.emptyList(), Collections.emptyList()));
    }

    @Test
    void shouldGetDistinctNumberOfPositionTypesFilled() {
        when(optimizer.getDistinctNumberOfPositionTypesFilled(anyList())).thenCallRealMethod();
        List<Player> currentLineup = Arrays.asList(rb1, rb2, rb3, wr1, wr2, wr3, wr4, te1);
        int result = optimizer.getDistinctNumberOfPositionTypesFilled(currentLineup);
        assertEquals(result, 3);
    }

    @Test
    void shouldGetDistinctNumberOfPositionTypesFilledWithFlex() {
        when(optimizer.getDistinctNumberOfPositionTypesFilled(anyList())).thenCallRealMethod();
        List<Player> currentLineup = Arrays.asList(qb0, rb2, rb3, wr1, wr2, wr3, te1, rb1, dst1);
        int result = optimizer.getDistinctNumberOfPositionTypesFilled(currentLineup);
        assertEquals(result, 6);
    }

    @Test
    void shouldGetMaxPointsToAdd() {
        when(optimizer.totalProjection(anyList())).thenCallRealMethod();
        when(optimizer.getMaxPointsToAdd(anyList())).thenCallRealMethod();
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
        when(optimizer.totalSalary(anyList())).thenCallRealMethod();
        when(optimizer.getMinSalaryToAdd(anyList())).thenCallRealMethod();
        List<Set<List<Player>>> remainingPlayerPools = Arrays.asList(
                Stream.of(Arrays.asList(wr1, wr2, wr3), Arrays.asList(wr1, wr2, wr4), Arrays.asList(wr1, wr3, wr4),
                        Arrays.asList(wr2, wr3, wr4)).collect(Collectors.toSet()),
                Stream.of(Collections.singletonList(te1)).collect(Collectors.toSet()),
                Stream.of(Collections.singletonList(dst1)).collect(Collectors.toSet())
        );
        int result = optimizer.getMinSalaryToAdd(remainingPlayerPools);
        assertEquals(result, 31300);
    }

    @Test
    void shouldDetermineLineupIsBetter() {
        when(optimizer.lineupIsBetter(anyList())).thenCallRealMethod();
        when(optimizer.totalSalary(anyList())).thenReturn(68999);
        when(optimizer.totalProjection(anyList())).thenReturn(69.1);
        optimizer.setSalaryCap(69000);
        optimizer.setMaxPoints(6.9);
        boolean result = optimizer.lineupIsBetter(Collections.emptyList());
        assertTrue(result);
    }

    @Test
    void shouldDetermineLineupNotBetterDueToLowerProjection() {
        when(optimizer.lineupIsBetter(anyList())).thenCallRealMethod();
        when(optimizer.totalSalary(anyList())).thenReturn(68999);
        when(optimizer.totalProjection(anyList())).thenReturn(7.0);
        optimizer.setSalaryCap(69000);
        optimizer.setMaxPoints(69.0);
        boolean result = optimizer.lineupIsBetter(Collections.emptyList());
        assertFalse(result);
    }

    @Test
    void shouldDetermineLineupNotBetterDueToHigherSalary() {
        when(optimizer.lineupIsBetter(anyList())).thenCallRealMethod();
        when(optimizer.totalSalary(anyList())).thenReturn(69001);
        when(optimizer.totalProjection(anyList())).thenReturn(69.1);
        optimizer.setSalaryCap(69000);
        optimizer.setMaxPoints(69.0);
        boolean result = optimizer.lineupIsBetter(Collections.emptyList());
        assertFalse(result);
    }

    @Test
    void shouldCalculateTotalProjection() {
        when(optimizer.totalProjection(anyList())).thenCallRealMethod();
        double result = optimizer.totalProjection(Arrays.asList(rb1, rb2, rb3));
        assertEquals(result, 47.9);
    }

    @Test
    void shouldCalculateTotalSalary() {
        when(optimizer.totalSalary(anyList())).thenCallRealMethod();
        int result = optimizer.totalSalary(Arrays.asList(rb1, rb2, rb3));
        assertEquals(result, 20500);
    }

    @Test
    void shouldGenerateOptimalLineup() {
        when(lineupRestrictions.getDistinctTeamsRequired()).thenReturn(3);
        when(lineupRestrictions.getMaxPlayersPerTeam()).thenReturn(4);
        when(lineupRestrictions.getTeamAgnosticPosition()).thenReturn("QB");
        when(lineupRestrictions.getWhiteListedTeams()).thenReturn(Collections.emptyList());
        List<Player> result = new Optimizer().generateOptimalLineup(playerPools, 50000, lineupRestrictions);
        assertEquals(Arrays.asList(qb0, rb2, rb3, wr1, wr2, wr4, te1, dst1), result);
    }
}
