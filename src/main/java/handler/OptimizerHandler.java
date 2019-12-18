package handler;

import optimize.*;

import java.util.*;

public class OptimizerHandler {
    @SuppressWarnings("unchecked")
    public List<Integer> handleRequest(Map<String, Object> input) {
        List<Player> lineup = new ArrayList<>();
        List<Player> playerList = new ArrayList<>();
        List<Player> blackList = new ArrayList<>();
        ((List<Map<String, Object>>) input.get("lineup")).forEach(playerMap -> {
            int playerId = (int) playerMap.get("playerId");
            lineup.add(playerId == 0 ? new Player() : new Player(playerId, (String) playerMap.get("position"),
                    ((Number) playerMap.get("projection")).doubleValue(), (int) playerMap.get("salary")));
        });
        ((List<Map<String, Object>>) input.get("players")).forEach(playerMap ->
                playerList.add(new Player((int) playerMap.get("playerId"), (String) playerMap.get("position"),
                        ((Number) playerMap.get("projection")).doubleValue(), (int) playerMap.get("salary"))));
        ((List<Integer>) input.get("blackList")).forEach(playerId -> blackList.add(new Player(playerId)));
        List<String> lineupPositionsInput = (List<String>) input.get("lineupPositions");
        int salaryCap = (int) input.get("salaryCap");
        Adjuster adjuster = new Adjuster(lineup, playerList, blackList, lineupPositionsInput, salaryCap);
        List<Player> adjustedPlayerList = adjuster.adjustedPlayerList();
        List<String> adjustedLineupPositions = adjuster.adjustedLineupPositions();
        int adjustedSalaryCap = adjuster.adjustedSalaryCap();
        LineupMatrix lineupMatrix = new LineupMatrix(adjustedLineupPositions, 3000000);
        Optimizer optimizer = new Optimizer(adjustedPlayerList, lineupMatrix, adjustedSalaryCap);
        List<Player> optimalPlayers = optimizer.generateOptimalPlayers();
        return new LineupCompiler(lineup, optimalPlayers).outputLineup();
    }
}
