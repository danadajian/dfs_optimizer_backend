package handler;

import optimize.*;

import java.util.*;

public class OptimizerHandler {
    Adjuster adjuster = new Adjuster();
    Optimizer optimizer = new Optimizer();
    LineupCompiler lineupCompiler = new LineupCompiler();

    @SuppressWarnings("unchecked")
    public List<Integer> handleRequest(Map<String, Object> input) {
        List<Player> lineup = new ArrayList<>();
        List<Player> playerPool = new ArrayList<>();
        List<Player> blackList = new ArrayList<>();
        ((List<Map<String, Object>>) input.get("lineup")).forEach(playerMap -> {
            int playerId = (int) playerMap.get("playerId");
            lineup.add(playerId == 0 ? new Player() : new Player(playerId, (String) playerMap.get("position"),
                    ((Number) playerMap.get("projection")).doubleValue(), (int) playerMap.get("salary")));
        });
        ((List<Map<String, Object>>) input.get("playerPool")).forEach(playerMap ->
                playerPool.add(new Player((int) playerMap.get("playerId"), (String) playerMap.get("position"),
                        ((Number) playerMap.get("projection")).doubleValue(), (int) playerMap.get("salary"))));
        ((List<Integer>) input.get("blackList")).forEach(playerId -> blackList.add(new Player(playerId)));
        List<String> lineupPositions = (List<String>) input.get("lineupPositions");
        int salaryCap = (int) input.get("salaryCap");
        List<Player> whiteList = adjuster.getWhiteList(lineup);
        List<Player> adjustedPlayerPool = adjuster.adjustPlayerPool(playerPool, whiteList, blackList);
        List<String> adjustedLineupPositions = adjuster.adjustLineupPositions(lineup, lineupPositions);
        int adjustedSalaryCap = adjuster.adjustSalaryCap(whiteList, salaryCap);
        LineupMatrix lineupMatrix = new LineupMatrix(adjustedLineupPositions, 3000000);
        List<Player> optimalPlayers = optimizer.generateOptimalPlayers(adjustedPlayerPool, lineupMatrix, adjustedSalaryCap);
        return lineupCompiler.outputLineup(lineup, optimalPlayers);
    }
}
