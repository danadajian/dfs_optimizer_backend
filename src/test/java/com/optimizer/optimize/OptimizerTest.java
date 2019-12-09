package com.optimizer.optimize;

import optimize.Optimizer;
import optimize.Player;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class OptimizerTest {
    private Player qb1 = new Player(828743, "QB", 20.81280187343307, 7700);
    private Player qb2 = new Player(691536, "QB", 20.542557435922102, 7700);
    private Player qb3 = new Player(822350, "QB", 18.88529138049285, 7100);
    private Player qb4 = new Player(216263, "QB", 20.194602653052648, 7600);
    private Player qb5 = new Player(839031, "QB", 20.918017657989424, 8400);
    private Player rb1 = new Player(747861, "RB", 15.293569534389379, 6400);
    private Player rb2 = new Player(543654, "RB", 16.711787177458103, 7300);
    private Player rb3 = new Player(592914, "RB", 13.734759944948792, 6000);
    private Player rb4 = new Player(556294, "RB", 13.540261647186695, 6000);
    private Player rb5 = new Player(750846, "RB", 17.06182853930281, 7600);
    private Player wr1 = new Player(877790, "WR", 16.983971848114212, 7100);
    private Player wr2 = new Player(821389, "WR", 12.678046108546337, 5800);
    private Player wr3 = new Player(653699, "WR", 18.478100634991, 8600);
    private Player wr4 = new Player(884013, "WR", 14.324187801863836, 6900);
    private Player wr5 = new Player(593587, "WR", 15.828094345164466, 7700);
    private Player te1 = new Player(600191, "TE", 12.987182375473749, 6200);
    private Player te2 = new Player(733672, "TE", 13.043278591888672, 6700);
    private Player te3 = new Player(448240, "TE", 13.588371113889947, 7100);
    private Player te4 = new Player(469472, "TE", 8.391181703019264, 4800);
    private Player te5 = new Player(739424, "TE", 11.261288939973811, 6600);
    private Player dst1 = new Player(323, "DST", 10.54, 3500);
    private Player dst2 = new Player(347, "DST", 11.63, 4800);
    private Player dst3 = new Player(356, "DST", 10.4, 4300);
    private Player dst4 = new Player(352, "DST", 9.55, 4100);
    private Player dst5 = new Player(335, "DST", 11.2, 5000);
    private Player goodPlayer = new Player(69, "TE", 6.9, 6900);
    private Player badPlayer = new Player(123, "QB", 0.2, 9000);
    private Player fillerPLayer1 = new Player(12345, "TE", 0.0, 6900);
    private Player fillerPLayer2 = new Player(123456, "QB", 0.0, 9000);

    private List<Player> playerList = Arrays.asList(
            qb1, qb2, qb3, qb4, qb5, rb1, rb2, rb3, rb4, rb5, wr1, wr2, wr3, wr4, wr5, te1, te2, te3, te4, te5,
            dst1, dst2, dst3, dst4, dst5, goodPlayer, badPlayer, fillerPLayer1, fillerPLayer2
    );

    private List<Player> whiteList = new ArrayList<>();
    private List<Player> blackList = Collections.singletonList(badPlayer);
    private List<String> lineupMatrix = new LinkedList<>(Arrays.asList("QB", "RB", "RB", "WR", "WR", "WR", "TE", "RB,WR,TE", "DST"));
    private int salaryCap = 60000;

    private Optimizer optimizer = new Optimizer(playerList, whiteList, blackList, lineupMatrix, salaryCap, 5);
    private Optimizer optimizerWithWhiteList = new Optimizer(playerList, Collections.singletonList(te1), blackList, lineupMatrix, salaryCap, 5);

    @Test
    void shouldReturnOptimalLineup() {
        List<Player> result = optimizer.getOptimalLineup();
        List<Player> expected = Arrays.asList(qb1, rb1, rb3, wr1, wr3, wr4, te1, rb5, dst1);
        Set resultSet = new HashSet<>(result);
        Set expectedSet = new HashSet<>(expected);
        assertEquals(expectedSet, resultSet);
    }

    @Test
    void shouldGetCheapestPlayersPerProjectionByPositionWithoutBlackList() {
        List<List<Player>> result = optimizer.getCheapestPlayersPerProjectionByPositionWithoutWhiteOrBlackList();
        List<List<Player>> expected = Arrays.asList(
                Arrays.asList(qb1, qb2, qb3, qb4, qb5),
                Arrays.asList(rb1, rb2, rb3, rb4, rb5),
                Arrays.asList(rb1, rb2, rb3, rb4, rb5),
                Arrays.asList(wr1, wr2, wr3, wr4, wr5),
                Arrays.asList(wr1, wr2, wr3, wr4, wr5),
                Arrays.asList(wr1, wr2, wr3, wr4, wr5),
                Arrays.asList(te1, te2, te3, te4, te5),
                Arrays.asList(wr1, rb1, rb2, rb3, rb4),
                Arrays.asList(dst1, dst2, dst3, dst4, dst5)
        );
        assertEquals(expected, result);
    }

    @Test
    void shouldGetLineupFromIntList() {
        List<List<Player>> playerPools = optimizer.getCheapestPlayersPerProjectionByPositionWithoutWhiteOrBlackList();
        List<Player> result = optimizer.getLineupFromIndexList(Arrays.asList(0, 0, 2, 1, 0, 4, 2, 2, 0), playerPools);
        assertEquals(Arrays.asList(qb1, rb1, rb3, wr2, wr1, wr5, te3, rb2, dst1), result);
    }

    @Test
    void shouldCheckForValidLineup() {
        boolean isValid = optimizer.isValidLineup(Arrays.asList(qb1, rb1, rb3, wr1, wr3, wr4, te1, rb5, dst1));
        assertTrue(isValid);
        boolean isHopefullyNotValid = optimizer.isValidLineup(Arrays.asList(qb1, rb1, rb3, wr1, wr3, wr4, te1, wr3, dst1));
        assertFalse(isHopefullyNotValid);
        Optimizer singleGameOptimizer = new Optimizer(playerList, whiteList, blackList,
                new LinkedList<>(Arrays.asList("any", "any", "any", "any", "any")), salaryCap, 5);
        boolean isValidSingleGameMatrix = singleGameOptimizer.isValidLineup(Arrays.asList(qb1, qb2, rb3, wr1, fillerPLayer2));
        assertTrue(isValidSingleGameMatrix);
    }

    @Test
    void shouldGetLineupWithWhiteListedPlayers() {
        Player emptyPlayer = new Player();
        List<Player> result = optimizerWithWhiteList.getLineupWithWhiteList();
        List<Player> expected = Arrays.asList(
                emptyPlayer, emptyPlayer, emptyPlayer, emptyPlayer, emptyPlayer, emptyPlayer, te1, emptyPlayer, emptyPlayer
        );
        assertEquals(expected, result);
    }

    @Test
    void shouldRemoveWhiteListedPositionsFromLineupMatrix() {
        lineupMatrix = optimizerWithWhiteList.removeWhiteListedPositionsFromLineupMatrix(lineupMatrix, optimizerWithWhiteList.getLineupWithWhiteList());
        assertEquals(Arrays.asList("QB", "RB", "RB", "WR", "WR", "WR", "RB,WR,TE", "DST"), lineupMatrix);
        assertEquals(8, optimizerWithWhiteList.getCheapestPlayersPerProjectionByPositionWithoutWhiteOrBlackList().size());
    }

    @Test
    void shouldReturnOptimalLineupWithWhiteList() {
        List<Player> result = optimizerWithWhiteList.getOptimalLineup();
        List<Player> expected = Arrays.asList(qb1, rb1, rb3, wr1, wr3, wr4, te1, rb5, dst1);
        Set resultSet = new HashSet<>(result);
        Set expectedSet = new HashSet<>(expected);
        assertEquals(expectedSet, resultSet);
        assertEquals("QB", result.get(0).position);
        assertEquals("DST", result.get(result.size() - 1).position);
    }

    @Test
    void shouldGetCheapestPlayersPerProjectionByPositionWithoutWhiteOrBlackList() {
        List<List<Player>> result = optimizerWithWhiteList.getCheapestPlayersPerProjectionByPositionWithoutWhiteOrBlackList();
        List<List<Player>> expected = Arrays.asList(
                Arrays.asList(qb1, qb2, qb3, qb4, qb5),
                Arrays.asList(rb1, rb2, rb3, rb4, rb5),
                Arrays.asList(rb1, rb2, rb3, rb4, rb5),
                Arrays.asList(wr1, wr2, wr3, wr4, wr5),
                Arrays.asList(wr1, wr2, wr3, wr4, wr5),
                Arrays.asList(wr1, wr2, wr3, wr4, wr5),
                Arrays.asList(te2, te3, te4, te5, goodPlayer),
                Arrays.asList(wr1, rb1, rb2, rb3, rb4),
                Arrays.asList(dst1, dst2, dst3, dst4, dst5)
        );
        assertEquals(expected, result);
    }

    @Test
    void shouldGetLineupFromIntListWithWhiteList() {
        lineupMatrix = optimizerWithWhiteList.removeWhiteListedPositionsFromLineupMatrix(lineupMatrix, optimizerWithWhiteList.getLineupWithWhiteList());
        List<List<Player>> playerPools = optimizerWithWhiteList.getCheapestPlayersPerProjectionByPositionWithoutWhiteOrBlackList();
        List<Player> result = optimizerWithWhiteList.getLineupFromIndexList(Arrays.asList(1, 0, 2, 1, 0, 4, 2, 0), playerPools);
        assertEquals(Arrays.asList(qb2, rb1, rb3, wr2, wr1, wr5, rb2, dst1), result);
    }

    @Test
    void shouldGenerateAllLineupPermutations() {
        optimizer.permuteLineups(IntStream.rangeClosed(0, 4).boxed().collect(Collectors.toList()), 9,
                Collections.singletonList(0), optimizer.getCheapestPlayersPerProjectionByPositionWithoutWhiteOrBlackList());
        assertEquals(Math.pow(5, 8), optimizer.getPermutationCounter());
    }

    @Test
    void shouldGenerateAllLineupPermutationsSingleGame() {
        optimizer.permuteSingleGameLineups(Arrays.asList(qb1, qb2, qb3, qb4, qb5), 2);
        assertEquals(CombinatoricsUtils.binomialCoefficient(5, 2), optimizer.getPermutationCounter());
    }

    @Test
    void shouldCombineOptimalPlayersWithWhiteList() {
        List<Player> lineupWithWhiteList = optimizerWithWhiteList.getLineupWithWhiteList();
        List<Player> optimalPlayers = Arrays.asList(qb1, rb1, rb2, wr1, wr2, wr3, rb3, dst1);
        List<Player> result = optimizerWithWhiteList.combineOptimalPlayersWithWhiteList(optimalPlayers, lineupWithWhiteList);
        assertEquals(Arrays.asList(qb1, rb1, rb2, wr1, wr2, wr3, te1, rb3, dst1), result);
    }
}