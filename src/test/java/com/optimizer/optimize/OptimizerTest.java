package com.optimizer.optimize;

import com.google.common.collect.Sets;
import optimize.Optimizer;
import optimize.Player;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

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
    private Player dst1 = new Player(15, "D", 6.0, 4600);
    private Player dst2 = new Player(16, "D", 9.0, 5300);
    private Player emptyPlayer = new Player();

    private List<Player> playerList = Arrays.asList(
            qb1, qb2, rb1, rb2, rb3, rb4, rb5, rb6, wr1, wr2, wr3, wr4, te1, te2, dst1, dst2
    );
    List<Player> whiteList = Arrays.asList(new Player(2), new Player(10));
    List<Player> blackList = Collections.singletonList(new Player(4));
    private List<String> lineupMatrix = new LinkedList<>(Arrays.asList(
            "QB", "RB", "RB", "WR", "WR", "WR", "TE", "RB,WR,TE", "D"
    ));
    int salaryCap = 60000;
    private Optimizer optimizer = new Optimizer(playerList, whiteList, blackList, lineupMatrix, salaryCap, 3000000);

    @Mock
    Optimizer mockOptimizer;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
        mockOptimizer.setLineupMatrix(Arrays.asList("RB", "WR", "WR"));
        mockOptimizer.setUniquePositions(Arrays.asList("RB", "WR"));
        mockOptimizer.setListToOptimize(Collections.emptyList());
        mockOptimizer.setPositionThresholds(Arrays.asList(4, 4));
        when(mockOptimizer.getTruncatedPlayerPoolsByPosition()).thenReturn(
                Arrays.asList(Arrays.asList(rb1, rb2, rb3, rb4), Arrays.asList(wr1, wr2, wr3, wr4))
        );
        when(mockOptimizer.permutePlayerPools(anyList())).thenReturn(Arrays.asList(
                new HashSet<>(Arrays.asList(Collections.singletonList(rb1), Collections.singletonList(rb2),
                        Collections.singletonList(rb3), Collections.singletonList(rb4))
                ),
                new HashSet<>(Arrays.asList(Arrays.asList(wr1, wr2), Arrays.asList(wr1, wr3),
                        Arrays.asList(wr1, wr4), Arrays.asList(wr2, wr3), Arrays.asList(wr2, wr4),
                        Arrays.asList(wr3, wr4))
                )
                )
        );
    }

    @Test
    void shouldReturnTruncatedPools() {
        List<List<Player>> result = mockOptimizer.getTruncatedPlayerPoolsByPosition();
        assertEquals(2, result.size());
        assertEquals(4, result.get(0).size());
        assertEquals(4, result.get(1).size());
    }

    @Test
    void shouldReturnEachSetOfCombinations() {
        List<List<Player>> playerPools = Arrays.asList(Arrays.asList(rb1, rb2, rb3, rb4),
                Arrays.asList(wr1, wr2, wr3, wr4));
        List<Set<List<Player>>> result = optimizer.permutePlayerPools(playerPools);
        assertEquals(2, result.size());
        assertEquals(CombinatoricsUtils.binomialCoefficient(4, 2), result.get(0).size());
        assertEquals(CombinatoricsUtils.binomialCoefficient(4, 2), result.get(1).size());
    }

    @Test
    void shouldGetCorrectCartesianProductSize() {
        List<Set<List<Player>>> mockPermutedPlayerPools = Arrays.asList(
                new HashSet<>(Arrays.asList(Collections.singletonList(rb1), Collections.singletonList(rb2),
                        Collections.singletonList(rb3), Collections.singletonList(rb4))
                ),
                new HashSet<>(Arrays.asList(Arrays.asList(wr1, wr2), Arrays.asList(wr1, wr3),
                        Arrays.asList(wr1, wr4), Arrays.asList(wr2, wr3), Arrays.asList(wr2, wr4),
                        Arrays.asList(wr3, wr4))
                )
        );
        Set<List<List<Player>>> result = Sets.cartesianProduct(mockPermutedPlayerPools);
        assertEquals(CombinatoricsUtils.binomialCoefficient(4, 1) * CombinatoricsUtils.binomialCoefficient(4, 2), result.size());
    }

    @Test
    void shouldGetLineupWithWhiteListedPlayers() {
        List<Player> result = optimizer.getLineupWithWhiteList(whiteList, lineupMatrix);
        List<Player> expected = Arrays.asList(
                qb2, emptyPlayer, emptyPlayer, wr2, emptyPlayer, emptyPlayer, emptyPlayer, emptyPlayer, emptyPlayer
        );
        assertEquals(expected, result);
    }

    @Test
    void shouldRemoveWhiteListedPositionsFromLineupMatrix() {
        List<Player> lineupWithWhiteList = Arrays.asList(
                qb2, emptyPlayer, emptyPlayer, wr2, emptyPlayer, emptyPlayer, emptyPlayer, emptyPlayer, emptyPlayer);
        List<String> result = optimizer.lineupMatrixWithoutWhiteListedPositions(lineupWithWhiteList, lineupMatrix);
        assertEquals(Arrays.asList("RB", "RB", "WR", "WR", "TE", "RB,WR,TE", "D"), result);
    }

    @Test
    void shouldCheckForValidLineup() {
        boolean result1 = optimizer.isValidLineup(Arrays.asList(rb1, rb2, wr2, wr3, te1, rb3, dst1));
        assertTrue(result1);
        boolean result2 = optimizer.isValidLineup(Arrays.asList(rb2, rb1, wr3));
        assertTrue(result2);
        boolean result3 = optimizer.isValidLineup(Arrays.asList(rb1, rb2, wr2, wr3, te1, rb2, dst1));
        assertFalse(result3);
    }

    @Test
    void shouldCombineOptimalPlayersWithWhiteList() {
        List<Player> lineupWithWhiteList = Arrays.asList(
                qb2, emptyPlayer, emptyPlayer, wr2, emptyPlayer, emptyPlayer, emptyPlayer, emptyPlayer, emptyPlayer
        );
        List<Player> optimalPlayers = Arrays.asList(rb1, rb2, wr1, wr4, te1, rb3, dst1);
        List<Player> result = optimizer.combineOptimalPlayersWithWhiteList(optimalPlayers, lineupWithWhiteList);
        assertEquals(Arrays.asList(qb2, rb1, rb2, wr2, wr1, wr4, te1, rb3, dst1), result);
    }

    @Test
    void shouldReturnPositionFrequencyMatrix() {
        List<Integer> result = optimizer.positionFrequencyMatrix();
        assertEquals(Arrays.asList(-1, 1, 1, 2, 2), result);
    }

    @Test
    void shouldReturnValidPositionThresholds() {
        List<Integer> result = optimizer.positionThresholds(3000000);
        assertEquals(Arrays.asList(14, 10, 10, 10, 10), result);
    }

    @Test
    void shouldReturnPositionThreshold() {
        int result2 = optimizer.getPositionThreshold("RB");
        assertEquals(10, result2);
        int result3 = optimizer.getPositionThreshold("WR");
        assertEquals(10, result3);
        int result4 = optimizer.getPositionThreshold("TE");
        assertEquals(10, result4);
        int result5 = optimizer.getPositionThreshold("RB,WR,TE");
        assertEquals(14, result5);
        int result6 = optimizer.getPositionThreshold("D");
        assertEquals(10, result6);
    }
}
