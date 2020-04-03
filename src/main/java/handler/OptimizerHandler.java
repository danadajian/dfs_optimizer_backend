package handler;

import optimize.*;
import util.AWSClient;

import java.util.*;

public class OptimizerHandler {
    Adjuster adjuster = new Adjuster();
    PlayerSelector playerSelector = new PlayerSelector();
    Optimizer optimizer = new Optimizer();
    LineupCompiler lineupCompiler = new LineupCompiler();
    private AWSClient awsClient = new AWSClient();

    public List<Integer> handleRequest(Map<String, Object> input) {
        boolean isPipelineInvocation = (input.getOrDefault("invocationType", "web")).equals("pipeline");
        String sport = (String) input.get("sport");
        List<Player> lineup = new Lineup(input).getLineup();
        List<Player> playerPool = isPipelineInvocation ?
                new PlayerPool(awsClient.downloadFromS3(sport + "PlayerPool.json")).getPlayerPool() :
                new PlayerPool(input).getPlayerPool();
        List<Player> blackList = new BlackList(input).getBlackList();
        List<String> lineupPositions = new LineupPositions(input).getLineupPositions();
        LineupRestrictions lineupRestrictions = new LineupRestrictions(input);
        int salaryCap = (int) input.get("salaryCap");
        long maxCombinationsReductionFactor = input.containsKey("previouslyTimedOut") ? 10 : 1;
        long maxCombinations = ((Number) input.get("maxCombinations")).longValue() / maxCombinationsReductionFactor;

        List<Player> whiteList = adjuster.getWhiteList(lineup);
        List<Player> adjustedPlayerPool = adjuster.adjustPlayerPool(playerPool, whiteList, blackList);
        List<String> adjustedLineupPositions = adjuster.adjustLineupPositions(lineup, lineupPositions);
        LineupRestrictions adjustedLineupRestrictions = adjuster.adjustLineupRestrictions(lineupRestrictions, whiteList);
        LineupMatrix lineupMatrix = new LineupMatrix(adjustedLineupPositions, adjustedPlayerPool, maxCombinations);
        int adjustedSalaryCap = adjuster.adjustSalaryCap(whiteList, salaryCap);
        List<List<Player>> truncatedPlayerPools = playerSelector.truncatePlayerPoolsByPosition(adjustedPlayerPool, lineupMatrix);
        List<Set<List<Player>>> permutedPlayerPools = playerSelector.getPlayerPoolCombinations(truncatedPlayerPools, lineupMatrix);
        List<Player> optimalPlayers = optimizer.generateOptimalLineup(permutedPlayerPools, adjustedSalaryCap, adjustedLineupRestrictions);

        if (isPipelineInvocation) {
            List<Player> playersWithNames = lineupCompiler.outputPlayersWithNames(lineup, optimalPlayers, playerPool);
            awsClient.uploadToS3(sport + "OptimalLineup.json",
                    lineupCompiler.generateFileOutput(playersWithNames));
            return Collections.emptyList();
        } else {
            return lineupCompiler.outputLineupPlayerIds(lineup, optimalPlayers);
        }
    }
}
