package optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class LineupValidatorTest {
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

    private LineupValidator lineupValidator = mock(LineupValidator.class);
    private LineupRestrictions lineupRestrictions = mock(LineupRestrictions.class);

    @BeforeEach
    void setUp() {
        reset(lineupValidator, lineupRestrictions);
        when(lineupRestrictions.getDistinctTeamsRequired()).thenReturn(3);
        when(lineupRestrictions.getMaxPlayersPerTeam()).thenReturn(4);
        when(lineupRestrictions.getTeamAgnosticPosition()).thenReturn("QB");
        when(lineupRestrictions.getWhiteListedTeams()).thenReturn(Collections.emptyList());
    }

    @Test
    void shouldContainNoDuplicates() {
        when(lineupValidator.lineupContainsNoDuplicates(anyList())).thenCallRealMethod();
        boolean result = lineupValidator.lineupContainsNoDuplicates(Arrays.asList(rb1, rb2, wr2, wr3, te1, rb3, dst1));
        assertTrue(result);
    }

    @Test
    void shouldContainDuplicates() {
        boolean result = lineupValidator.lineupContainsNoDuplicates(Arrays.asList(rb1, rb2, wr2, wr3, te1, rb2, dst1));
        assertFalse(result);
    }

    @Test
    void shouldSatisfyDistinctTeamsRequired() {
        when(lineupValidator.lineupSatisfiesDistinctTeamsRequired(anyList(), any())).thenCallRealMethod();
        boolean result = lineupValidator.lineupSatisfiesDistinctTeamsRequired(Arrays.asList(rb1, rb2, wr2, wr3, te1, rb3, dst1), lineupRestrictions);
        assertTrue(result);
    }

    @Test
    void shouldNotSatisfyDistinctTeamsRequired() {
        when(lineupValidator.lineupSatisfiesDistinctTeamsRequired(anyList(), any())).thenCallRealMethod();
        boolean result = lineupValidator.lineupSatisfiesDistinctTeamsRequired(Arrays.asList(rb1, rb2, wr1, wr2, te1, wr4, dst1), lineupRestrictions);
        assertFalse(result);
    }

    @Test
    void shouldSatisfyDistinctTeamsRequiredWithWhiteListedTeams() {
        when(lineupValidator.lineupSatisfiesDistinctTeamsRequired(anyList(), any())).thenCallRealMethod();
        when(lineupRestrictions.getWhiteListedTeams()).thenReturn(Collections.singletonList("team3"));
        boolean result = lineupValidator.lineupSatisfiesDistinctTeamsRequired(Arrays.asList(rb1, rb2, wr1, wr2, te1, wr4, dst1), lineupRestrictions);
        assertTrue(result);
    }

    @Test
    void shouldSatisfyMaxPlayersPerTeam() {
        when(lineupValidator.lineupSatisfiesMaxPlayersPerTeam(anyList(), any())).thenCallRealMethod();
        boolean result = lineupValidator.lineupSatisfiesMaxPlayersPerTeam(Arrays.asList(rb1, rb2, wr2, wr3, te1, rb3, dst1), lineupRestrictions);
        assertTrue(result);
    }

    @Test
    void shouldNotSatisfyMaxPlayersPerTeam() {
        when(lineupValidator.lineupSatisfiesMaxPlayersPerTeam(anyList(), any())).thenCallRealMethod();
        boolean result = lineupValidator.lineupSatisfiesMaxPlayersPerTeam(Arrays.asList(rb1, rb2, wr1, wr4, te1, rb3, dst1), lineupRestrictions);
        assertFalse(result);
    }

    @Test
    void shouldNotSatisfyMaxPlayersPerTeamWithWhiteListedTeams() {
        when(lineupRestrictions.getWhiteListedTeams()).thenReturn(Collections.singletonList("team1"));
        when(lineupValidator.lineupSatisfiesMaxPlayersPerTeam(anyList(), any())).thenCallRealMethod();
        boolean result = lineupValidator.lineupSatisfiesMaxPlayersPerTeam(Arrays.asList(rb1, rb2, wr1, wr4, rb3, dst1), lineupRestrictions);
        assertFalse(result);
    }

    @Test
    void shouldSatisfyMaxPlayersPerTeamWithTeamAgnosticPosition() {
        when(lineupValidator.lineupSatisfiesMaxPlayersPerTeam(anyList(), any())).thenCallRealMethod();
        boolean result = lineupValidator.lineupSatisfiesMaxPlayersPerTeam(Arrays.asList(qb0, rb2, wr1, wr4, te1, rb3, dst1), lineupRestrictions);
        assertTrue(result);
    }

    @Test
    void shouldNotSatisfyMaxPlayersPerTeamWithTeamAgnosticPosition() {
        when(lineupValidator.lineupSatisfiesMaxPlayersPerTeam(anyList(), any())).thenCallRealMethod();
        boolean result = lineupValidator.lineupSatisfiesMaxPlayersPerTeam(Arrays.asList(qb0, rb1, wr1, wr4, te1, rb3, dst1), lineupRestrictions);
        assertFalse(result);
    }
}