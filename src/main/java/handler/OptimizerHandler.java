package handler;

import optimize.*;

import java.util.*;

public class OptimizerHandler {
    Adjuster adjuster = new Adjuster();
    Optimizer optimizer = new Optimizer();
    LineupCompiler lineupCompiler = new LineupCompiler();

    public List<Integer> handleRequest(Map<String, Object> input) {
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
        return lineupCompiler.outputLineup(lineup, optimalPlayers);
    }
}
