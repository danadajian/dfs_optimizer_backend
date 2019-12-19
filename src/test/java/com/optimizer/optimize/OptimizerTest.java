package com.optimizer.optimize;

import com.google.common.collect.Sets;
import optimize.LineupMatrix;
import optimize.Optimizer;
import optimize.Player;
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
    private Player rb1 = new Player(3, "RB", 15.0, 5000);
    private Player rb2 = new Player(4, "RB", 18.9, 8000);
    private Player rb3 = new Player(5, "RB", 14.0, 7500);
    private Player rb4 = new Player(6, "RB", 12.7, 7200);
    private Player wr1 = new Player(9, "WR", 22.9, 10000);
    private Player wr2 = new Player(10, "WR", 11.6, 7900);
    private Player wr3 = new Player(11, "WR", 5.5, 8400);
    private Player wr4 = new Player(12, "WR", 7.9, 3700);
    private Player te1 = new Player(13, "TE", 10.1, 6700);
    private Player dst1 = new Player(15, "D", 6.0, 4600);

    private List<Player> playerList = Arrays.asList(rb1, rb2, rb3, rb4, wr1, wr2, wr3, wr4);
    int salaryCap = 18000;
    private List<Set<List<Player>>> mockPermutedPlayerPools = Arrays.asList(
            new HashSet<>(Arrays.asList(Collections.singletonList(rb1), Collections.singletonList(rb2),
                    Collections.singletonList(rb3), Collections.singletonList(rb4))),
            new HashSet<>(Arrays.asList(Arrays.asList(wr1, wr2), Arrays.asList(wr1, wr3),
                    Arrays.asList(wr1, wr4), Arrays.asList(wr2, wr3), Arrays.asList(wr2, wr4),
                    Arrays.asList(wr3, wr4)))
    );

    @Mock
    LineupMatrix lineupMatrix;

    @InjectMocks
    Optimizer optimizer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(lineupMatrix.uniquePositions()).thenReturn(Arrays.asList("RB", "WR"));
        when(lineupMatrix.positionThreshold(anyString())).thenReturn(1);
        when(lineupMatrix.positionFrequency(anyString())).thenReturn(2);
    }

    @Test
    void shouldReturnTruncatedPools() {
        List<List<Player>> result = optimizer.truncatedPlayerPoolsByPosition(playerList, lineupMatrix);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).size());
        assertEquals(1, result.get(1).size());
    }

    @Test
    void shouldReturnEachSetOfCombinations() {
        List<List<Player>> playerPools = Arrays.asList(
                Arrays.asList(rb1, rb2, rb3, rb4),
                Arrays.asList(wr1, wr2, wr3, wr4)
        );
        List<Set<List<Player>>> result = optimizer.playerPoolCombinations(playerPools, lineupMatrix);
        assertEquals(2, result.size());
        assertEquals(CombinatoricsUtils.binomialCoefficient(4, 2), result.get(0).size());
        assertEquals(CombinatoricsUtils.binomialCoefficient(4, 2), result.get(1).size());
    }

    @Test
    void shouldGetCorrectCartesianProductSize() {
        Set<List<List<Player>>> result = Sets.cartesianProduct(mockPermutedPlayerPools);
        assertEquals(CombinatoricsUtils.binomialCoefficient(4, 1) *
                        CombinatoricsUtils.binomialCoefficient(4, 2), result.size());
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
        List<Player> result = optimizer.bestLineupInCartesianProduct(mockPermutedPlayerPools, salaryCap);
        assertEquals(Arrays.asList(rb1, wr2, wr4), result);
    }
}
