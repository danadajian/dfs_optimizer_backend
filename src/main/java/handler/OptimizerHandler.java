package handler;

import optimize.*;
import util.AWSClient;

import java.util.*;

public class OptimizerHandler {
    Adjuster adjuster = new Adjuster();
    Optimizer optimizer = new Optimizer();
    LineupCompiler lineupCompiler = new LineupCompiler();
    private AWSClient AWSClient = new AWSClient();

    public List<Integer> handleRequest(Map<String, Object> input) {
        String invocationType = (String) input.getOrDefault("invocationType", "web");
        List<Player> lineup = new Lineup(input).getLineup();
        List<Player> playerPool = new PlayerPool(input).getPlayerPool();
        List<Player> blackList = new BlackList(input).getBlackList();
        List<String> lineupPositions = new LineupPositions(input).getLineupPositions();
        LineupRestrictions lineupRestrictions = new LineupRestrictions(input);
        int salaryCap = (int) input.get("salaryCap");
        int maxCombinations = (int) input.get("maxCombinations");

        List<Player> whiteList = adjuster.getWhiteList(lineup);
        List<Player> adjustedPlayerPool = adjuster.adjustPlayerPool(playerPool, whiteList, blackList);
        List<String> adjustedLineupPositions = adjuster.adjustLineupPositions(lineup, lineupPositions);
        LineupMatrix lineupMatrix = new LineupMatrix(adjustedLineupPositions, maxCombinations);
        int adjustedSalaryCap = adjuster.adjustSalaryCap(whiteList, salaryCap);
        List<Player> optimalPlayers = optimizer.generateOptimalPlayers(adjustedPlayerPool, lineupMatrix,
                adjustedSalaryCap, lineupRestrictions);

        if (invocationType.equals("pipeline")) {
            List<String> namesInLineup = lineupCompiler.outputLineupPlayerNames(lineup, optimalPlayers, playerPool);
            AWSClient.uploadToS3("optimalLineup.json", namesInLineup);
            AWSClient.sendTextMessage(namesInLineup);
            return new ArrayList<>();
        } else
            return lineupCompiler.outputLineupPlayerIds(lineup, optimalPlayers);
    }
}
