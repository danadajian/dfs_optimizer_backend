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
        String invocationType = (String) input.getOrDefault("invocationType", "web");
        String sport = (String) input.get("sport");
        List<Player> lineup = new Lineup(input).getLineup();
        List<Player> playerPool = invocationType.equals("pipeline") ?
                new PlayerPool(awsClient.downloadFromS3(sport + "PlayerPool.json")).getPlayerPool() :
                new PlayerPool(input).getPlayerPool();
        List<Player> blackList = new BlackList(input).getBlackList();
        List<String> lineupPositions = new LineupPositions(input).getLineupPositions();
        LineupRestrictions lineupRestrictions = new LineupRestrictions(input);
        int salaryCap = (int) input.get("salaryCap");
        Object maxCombinationsObject = input.get("maxCombinations");
        long maxCombinations = maxCombinationsObject instanceof Integer ? (int) maxCombinationsObject : (long) maxCombinationsObject;

        List<Player> whiteList = adjuster.getWhiteList(lineup);
        List<Player> adjustedPlayerPool = adjuster.adjustPlayerPool(playerPool, whiteList, blackList);
        List<String> adjustedLineupPositions = adjuster.adjustLineupPositions(lineup, lineupPositions);
        LineupMatrix lineupMatrix = new LineupMatrix(adjustedLineupPositions, maxCombinations);
        int adjustedSalaryCap = adjuster.adjustSalaryCap(whiteList, salaryCap);
        List<List<Player>> truncatedPlayerPools = playerSelector.truncatePlayerPoolsByPosition(adjustedPlayerPool, lineupMatrix);
        List<Set<List<Player>>> permutedPlayerPools = playerSelector.getPlayerPoolCombinations(truncatedPlayerPools, lineupMatrix);
        List<Player> optimalPlayers = optimizer.generateOptimalLineup(permutedPlayerPools, adjustedSalaryCap, lineupRestrictions);

        if (invocationType.equals("pipeline")) {
            List<Player> playersWithNames = lineupCompiler.outputPlayersWithNames(lineup, optimalPlayers, playerPool);
            awsClient.uploadToS3(sport + "OptimalLineup.json",
                    lineupCompiler.generateFileOutput(playersWithNames));
            return Collections.emptyList();
        } else {
            return lineupCompiler.outputLineupPlayerIds(lineup, optimalPlayers);
        }
    }
}
