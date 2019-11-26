package com.optimizer.optimize;

import optimize.Optimizer;
import optimize.Player;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class OptimizerTest {
    private Player qb1 = new Player(1, "QB", 6.9, 6900);
    private Player qb2 = new Player(2, "QB", 17.0, 6100);
    private Player rb1 = new Player(3, "RB", 15.0, 5000);
    private Player rb2 = new Player(4, "RB", 18.9, 8000);
    private Player rb3 = new Player(5, "RB", 14.0, 7500);
    private Player rb4 = new Player(6, "RB", 12.7, 7200);
    private Player rb5 = new Player(7, "RB", 7.7, 5600);
    private Player rb6 = new Player(8, "RB", 9.1, 4900);
    private Player wr1 = new Player(9, "WR", 22.9, 10000);
    private Player wr2 = new Player(10, "WR", 11.6, 7900);
    private Player wr3 = new Player(11, "WR", 5.5, 8400);
    private Player wr4 = new Player(12, "WR", 7.9, 3700);
    private Player te1 = new Player(13, "TE", 10.1, 6700);
    private Player te2 = new Player(14, "TE", 8.1, 5200);
    private Player dst1 = new Player(15, "D/ST", 6.0, 4600);
    private Player dst2 = new Player(16, "D/ST", 9.0, 5300);

    private List<Player> playerList = Arrays.asList(
            qb1, qb2, rb1, rb2, rb3, rb4, rb5, rb6, wr1, wr2, wr3, wr4, te1, te2, dst1, dst2
    );

    private List<Player> whiteList = Arrays.asList(new Player(2), new Player(10));

    private List<Player> blackList = Arrays.asList(new Player(4));

    private List<String> lineupMatrix = Arrays.asList(
            "QB", "RB", "RB", "WR", "WR", "WR", "TE", "RB,WR,TE", "D/ST"
    );

    private int salaryCap = 55000;

    private Optimizer optimizer = new Optimizer(playerList, whiteList, blackList, lineupMatrix, salaryCap);

    @Test
    void shouldConsiderTwoListsOfSamePlayersEqual() {
        List<Player> playerList1 = Arrays.asList(new Player(1), new Player(2));
        List<Player> playerList2 = Arrays.asList(new Player(1), new Player(2));
        assertEquals(playerList1, playerList2);
    }

    @Test
    void shouldGetSortedPlayerPools() {
        List<List<Player>> result = optimizer.getSortedPlayerPoolsWithoutBlackList();
        List<List<Player>> expected = Arrays.asList(
                Arrays.asList(qb2, qb1),
                Arrays.asList(rb1, rb3, rb4, rb6, rb5),
                Arrays.asList(rb1, rb3, rb4, rb6, rb5),
                Arrays.asList(wr1, wr2, wr4, wr3),
                Arrays.asList(wr1, wr2, wr4, wr3),
                Arrays.asList(wr1, wr2, wr4, wr3),
                Arrays.asList(te1, te2),
                Arrays.asList(wr1, rb1, rb3, rb4, wr2, te1, rb6, te2, wr4, rb5, wr3),
                Arrays.asList(dst2, dst1)
        );
        assertEquals(expected, result);
    }

    @Test
    void shouldGetLineupWithWhiteListedPlayers() {
        Player emptyPlayer = new Player();
        List<Player> result = optimizer.lineupWithWhiteList();
        List<Player> expected = Arrays.asList(
                qb2, emptyPlayer, emptyPlayer, wr2, emptyPlayer, emptyPlayer, emptyPlayer, emptyPlayer, emptyPlayer
        );
        assertEquals(expected, result);
    }

    @Test
    void shouldGetBestLineupWithWhiteListedPlayers() {
        List<Player> result = optimizer.bestLineupWithWhiteList();
        List<Player> expected = Arrays.asList(
                qb2, rb1, rb3, wr2, wr1, wr4, te1, rb4, dst2
        );
        assertEquals(expected, result);
    }

    @Test
    void shouldSelectCorrectPlayerToDowngrade() {
        List<Player> lineup = Arrays.asList(qb2, rb1, rb3, wr2, wr1, wr4, te1, rb6, dst2);
        List<Player> result = optimizer.downgradeLowestCostNonWhiteListPlayer(lineup);
        List<Player> expected = Arrays.asList(qb2, rb1, rb3, wr2, wr1, wr4, te2, rb6, dst2);
        assertEquals(expected, result);
    }

    @Test
    void shouldCalculateTotalSalaryOfLineup() {
        List<Player> lineup = Arrays.asList(qb2, rb1, rb3, wr2, wr1, wr4, te1, rb6, dst2);
        int result = optimizer.totalSalary(lineup);
        assertEquals(qb2.salary + rb1.salary + rb3.salary + wr2.salary + wr1.salary + wr4.salary + te1.salary
                + rb6.salary + dst2.salary, result);
    }

    @Test
    void shouldDowngradeLineupToWithinCap() {
        List<Player> lineup = Arrays.asList(qb2, rb1, rb3, wr2, wr1, wr4, te1, rb6, dst2);
        List<Player> result = optimizer.downgradePlayersUntilUnderCap(lineup);
        assertTrue(optimizer.totalSalary(result) <= salaryCap);
    }

    @Test
    void shouldBeUnableToDowngradeLineupToWithinCapIfCapTooLow() {
        List<Player> lineup = Arrays.asList(qb2, rb1, rb3, wr2, wr1, wr4, te1, rb6, dst2);
        List<Player> result = new Optimizer(playerList, whiteList, blackList, lineupMatrix, 40000)
                .downgradePlayersUntilUnderCap(lineup);
        assertEquals(new ArrayList<>(), result);
    }

    @Test
    void shouldGenerateOptimalLineupWithinCap() {
        List<Player> result = optimizer.optimize();
        assertTrue(result.contains(qb2));
        assertTrue(result.contains(wr2));
        assertTrue(!result.contains(rb2));
        assertEquals(result.size(), new HashSet<>(result).size());
        assertTrue(optimizer.totalSalary(result) <= salaryCap);
    }
}