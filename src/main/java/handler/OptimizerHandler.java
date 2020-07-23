package handler;

import optimize.*;
import util.AWSClient;

import java.util.*;

public class OptimizerHandler {
    InputParser inputParser = new InputParser();
    Adjuster adjuster = new Adjuster();
    PlayerSelector playerSelector = new PlayerSelector();
    Optimizer optimizer = new Optimizer();
    LineupCompiler lineupCompiler = new LineupCompiler();
    AWSClient awsClient = new AWSClient();

    @SuppressWarnings("unchecked")
    public List<Integer> handleRequest(Map<String, Object> input) {
        System.out.println(input);
        boolean isPipelineInvocation = (input.getOrDefault("invocationType", "web")).equals("pipeline");
        String sport = (String) input.get("sport");
        List<Player> lineup = inputParser.getLineup(input);
        List<Player> playerPool = isPipelineInvocation ?
                inputParser.getPlayerPool(awsClient.downloadFromS3(sport + "PlayerPool.json")) :
                inputParser.getPlayerPool((List<Map<String, Object>>) input.get("playerPool"));
        List<Player> blackList = inputParser.getBlackList(input);
        List<String> lineupPositions = inputParser.getLineupPositions(input);
        LineupRestrictions lineupRestrictions = inputParser.getLineupRestrictions(input);
        long maxCombinationsReductionFactor = input.containsKey("previouslyTimedOut") ? 10 : 1;
        long maxCombinations = ((Number) input.get("maxCombinations")).longValue() / maxCombinationsReductionFactor;
        int salaryCap = (int) input.get("salaryCap");

        List<Player> whiteList = adjuster.getWhiteList(lineup);
        List<Player> adjustedPlayerPool = adjuster.adjustPlayerPool(playerPool, whiteList, blackList);
        List<String> adjustedLineupPositions = adjuster.adjustLineupPositions(lineup, lineupPositions);
        LineupRestrictions adjustedLineupRestrictions = adjuster.adjustLineupRestrictions(lineupRestrictions, whiteList);
        int adjustedSalaryCap = adjuster.adjustSalaryCap(whiteList, salaryCap);
        LineupMatrix lineupMatrix = playerSelector.getLineupMatrix(adjustedLineupPositions, adjustedPlayerPool, maxCombinations);
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
