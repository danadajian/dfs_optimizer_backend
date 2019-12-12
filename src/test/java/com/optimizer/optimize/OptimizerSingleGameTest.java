package com.optimizer.optimize;

import com.google.common.collect.Sets;
import optimize.Optimizer;
import optimize.Player;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class OptimizerSingleGameTest {
    private List<Player> singleGamePlayerList = new ArrayList<>();
    private Optimizer singleGameOptimizer;
    private Optimizer singleGameOptimizerWithWhiteList;

    private List<Player> singleGameWhiteList = Collections.singletonList(new Player(456613));
    private List<Player> singleGameBlackList = Arrays.asList(new Player(868199), new Player(406186));

    OptimizerSingleGameTest() {
        String singleGameString = "[{position=QB, projection=22.26028086187252, salary=17500, playerId=877745}, {position=QB, projection=17.28165351688948, salary=15500, playerId=868199}, {position=K, projection=10.723627723482894, salary=10000, playerId=448132}, {position=WR, projection=10.91630368761338, salary=11000, playerId=473742}, {position=WR, projection=8.321990597071242, salary=9000, playerId=976220}, {position=RB, projection=12.525118633183299, salary=14000, playerId=456613}, {position=K, projection=8.030606512889833, salary=9000, playerId=406186}, {position=WR, projection=9.883871079071676, salary=11500, playerId=460830}, {position=TE, projection=9.885905225180252, salary=12000, playerId=820699}, {position=RB, projection=9.982349180661368, salary=13000, playerId=916466}, {position=TE, projection=3.812997273938738, salary=5000, playerId=884083}, {position=TE, projection=3.9283748645810426, salary=6000, playerId=608753}, {position=WR, projection=4.051741488449973, salary=6500, playerId=591586}, {position=TE, projection=3.413350403417888, salary=5500, playerId=881956}, {position=WR, projection=3.7949487083171505, salary=7000, playerId=823040}, {position=RB, projection=4.2287049685487785, salary=8000, playerId=157341}, {position=RB, projection=3.889535076579936, salary=7500, playerId=739799}, {position=WR, projection=2.9496643508454916, salary=6500, playerId=750838}, {position=WR, projection=2.440822263196591, salary=6000, playerId=704254}, {position=RB, projection=1.6055371462607257, salary=5000, playerId=923109}, {position=WR, projection=1.9111163537812932, salary=6000, playerId=884553}, {position=TE, projection=1.4064319525163602, salary=5000, playerId=592268}, {position=WR, projection=0.9127037277849662, salary=5000, playerId=329031}, {position=WR, projection=0.820356268293934, salary=5000, playerId=606551}, {position=TE, projection=0.5853383066262842, salary=5000, playerId=334184}, {position=RB, projection=0.32639236755195605, salary=5000, playerId=553705}, {position=WR, projection=0.2093432111779464, salary=5000, playerId=607847}]";
        List<String> singleGameStringList = Arrays.asList(singleGameString.replace("[", "")
                .replace("]", "").split("}, "));
        singleGameStringList.forEach(playerString -> singleGamePlayerList.add(new Player(playerString)));
        List<Player> whiteList = new ArrayList<>();
        List<Player> blackList = new ArrayList<>();
        List<String> lineupMatrix = new LinkedList<>(Arrays.asList("any", "any", "any", "any", "any"));
        int salaryCap = 60000;
        singleGameOptimizer = new Optimizer(singleGamePlayerList, whiteList, blackList, lineupMatrix, salaryCap);
        singleGameOptimizerWithWhiteList = new Optimizer(singleGamePlayerList, singleGameWhiteList, singleGameBlackList, lineupMatrix, salaryCap);
    }

    @Test
    void shouldReturnOptimalLineupSingleGame() {
        List<Player> result = singleGameOptimizer.getOptimalLineup();
        List<Player> expected = Arrays.asList(
                new Player(877745),
                new Player(868199),
                new Player(473742),
                new Player(448132),
                new Player(608753)
        );
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnPositionThresholdSingleGame() {
        int result = singleGameOptimizer.positionThreshold("any");
        assertEquals(-1, result);
    }

    @Test
    void shouldReturnTruncatedPoolsSingleGame() {
        List<List<Player>> result = singleGameOptimizer.getTruncatedPlayerPoolsByPosition();
        assertEquals(1, result.size());
        assertEquals(singleGamePlayerList.size(), result.get(0).size());
    }

    @Test
    void shouldReturnEachSetOfCombinationsSingleGame() {
        List<Set<List<Player>>> result = singleGameOptimizer.permutePlayerPools(singleGameOptimizer.getTruncatedPlayerPoolsByPosition());
        assertEquals(1, result.size());
        assertEquals(CombinatoricsUtils.binomialCoefficient(
                singleGamePlayerList.size(),
                singleGameOptimizer.getLineupMatrix().size()), result.get(0).size());
    }

    @Test
    void shouldGetCorrectCartesianProductSizeSingleGame() {
        List<Set<List<Player>>> playerPools = singleGameOptimizer.permutePlayerPools(singleGameOptimizer.getTruncatedPlayerPoolsByPosition());
        Set<List<List<Player>>> result = Sets.cartesianProduct(playerPools);
        assertEquals(CombinatoricsUtils.binomialCoefficient(
                singleGamePlayerList.size(),
                singleGameOptimizer.getLineupMatrix().size()), result.size());
    }

    @Test
    void shouldReturnOptimalLineupSingleGameWithWhiteList() {
        List<Player> result = singleGameOptimizerWithWhiteList.getOptimalLineup();
        List<Player> expected = Arrays.asList(
                new Player(456613),
                new Player(877745),
                new Player(473742),
                new Player(448132),
                new Player(591586)
        );
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnTruncatedPoolsSingleGameWithWhiteList() {
        List<List<Player>> result = singleGameOptimizerWithWhiteList.getTruncatedPlayerPoolsByPosition();
        assertEquals(1, result.size());
        assertEquals(singleGamePlayerList.size() - singleGameWhiteList.size() - singleGameBlackList.size(),
                result.get(0).size());
    }

    @Test
    void shouldReturnEachSetOfCombinationsSingleGameWithWhiteList() {
        List<Set<List<Player>>> result = singleGameOptimizerWithWhiteList.permutePlayerPools(singleGameOptimizerWithWhiteList.getTruncatedPlayerPoolsByPosition());
        assertEquals(1, result.size());
        assertEquals(CombinatoricsUtils.binomialCoefficient(
                singleGamePlayerList.size() - singleGameWhiteList.size() - singleGameBlackList.size(),
                singleGameOptimizerWithWhiteList.getLineupMatrix().size()), result.get(0).size());
    }

    @Test
    void shouldGetCorrectCartesianProductSizeSingleGameWithWhiteList() {
        List<Set<List<Player>>> playerPools = singleGameOptimizerWithWhiteList.permutePlayerPools(singleGameOptimizerWithWhiteList.getTruncatedPlayerPoolsByPosition());
        Set<List<List<Player>>> result = Sets.cartesianProduct(playerPools);
        assertEquals(CombinatoricsUtils.binomialCoefficient(
                singleGamePlayerList.size() - singleGameWhiteList.size() - singleGameBlackList.size(),
                singleGameOptimizerWithWhiteList.getLineupMatrix().size()), result.size());
    }

    @Test
    void shouldCheckForValidLineup() {
        boolean result1 = singleGameOptimizer.isValidLineup(Arrays.asList(
                new Player(828743),
                new Player(747861),
                new Player(750846),
                new Player(877790),
                new Player(653699)
        ));
        assertTrue(result1);
        boolean result2 = singleGameOptimizer.isValidLineup(Arrays.asList(
                new Player(828743),
                new Player(828743),
                new Player(750846),
                new Player(877790)
        ));
        assertFalse(result2);
    }
}
