package handler;

import optimize.*;

import java.util.*;

public class OptimizerHandler {
    @SuppressWarnings("unchecked")
    public List<Integer> handleRequest(Map<String, Object> input) {
        List<Map<String, Object>> lineupInput = (List<Map<String, Object>>) input.get("lineup");
        List<Map<String, Object>> playersInput = (List<Map<String, Object>>) input.get("players");
        List<Integer> blackListInput = (List<Integer>) input.get("blackList");
        List<String> lineupPositionsInput = (List<String>) input.get("lineupPositions");
        List<Player> lineup = new ArrayList<>();
        List<Player> playerList = new ArrayList<>();
        List<Player> blackList = new ArrayList<>();
        for (Map<String, Object> playerMap : lineupInput) {
            int playerId = (int) playerMap.get("playerId");
            if (playerId == 0)
                lineup.add(new Player());
            else
                lineup.add(
                        new Player(
                                playerId,
                                (String) playerMap.get("position"),
                                ((Number) playerMap.get("projection")).doubleValue(),
                                (int) playerMap.get("salary")
                        )
                );
        }
        for (Map<String, Object> playerMap : playersInput)
            playerList.add(
                    new Player(
                            (int) playerMap.get("playerId"),
                            (String) playerMap.get("position"),
                            ((Number) playerMap.get("projection")).doubleValue(),
                            (int) playerMap.get("salary")
                    )
            );
        for (int playerId : blackListInput)
            blackList.add(new Player(playerId));
        int salaryCap = (int) input.get("salaryCap");
        Adjuster adjuster = new Adjuster(lineup, playerList, blackList, lineupPositionsInput, salaryCap);
        List<Player> adjustedPlayerList = adjuster.adjustedPlayerList();
        List<String> adjustedLineupPositions = adjuster.adjustedLineupPositions();
        int adjustedSalaryCap = adjuster.adjustedSalaryCap();
        LineupMatrix lineupMatrix = new LineupMatrix(adjustedLineupPositions, 3000000);
        Optimizer optimizer = new Optimizer(adjustedPlayerList, lineupMatrix, adjustedSalaryCap);
        List<Player> optimalPlayers = optimizer.optimize();
        return new LineupCompiler(lineup, optimalPlayers).outputLineup();
    }
}
