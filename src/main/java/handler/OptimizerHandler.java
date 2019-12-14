package handler;

import optimize.*;

import java.util.*;
import java.util.stream.Collectors;

public class OptimizerHandler {
    public List<Integer> handleRequest(Map<String, Object> input) {
        List<Player> lineup = new ArrayList<>();
        List<Player> playerList = new ArrayList<>();
        List<Player> blackList = new ArrayList<>();
        List<String> lineupPositions = new ArrayList<>();
        for (Object object : (List) input.get("lineup")) {
            Map playerMap = (Map) object;
            int playerId = (int) playerMap.get("playerId");
            if (playerId > 0) {
                String position = (String) playerMap.get("position");
                double projection = ((Number) playerMap.get("projection")).doubleValue();
                int salary = (int) playerMap.get("salary");
                lineup.add(new Player(playerId, position, projection, salary));
            } else
                lineup.add(new Player(playerId));
        }
        for (Object object : (List) input.get("players")) {
            Map playerMap = (Map) object;
            int playerId = (int) playerMap.get("playerId");
            String position = (String) playerMap.get("position");
            double projection = ((Number) playerMap.get("projection")).doubleValue();
            int salary = (int) playerMap.get("salary");
            playerList.add(new Player(playerId, position, projection, salary));
        }
        for (Object object : (List) input.get("lineupPositions")) {
            String position = (String) object;
            lineupPositions.add(position);
        }
        for (Object object : (List) input.get("blackList")) {
            int playerId = (int) object;
            blackList.add(new Player(playerId));
        }
        int salaryCap = (int) input.get("salaryCap");
        ParameterAdjuster parameterAdjuster = new ParameterAdjuster(lineup, playerList, blackList, lineupPositions, salaryCap);
        List<Player> newPlayerList = parameterAdjuster.adjustPlayerList();
        List<String> newLineupPositions = parameterAdjuster.adjustLineupPositions();
        int newSalaryCap = parameterAdjuster.adjustSalaryCap();
        LineupMatrix lineupMatrix = new LineupMatrix(newLineupPositions, 3000000);
        Optimizer optimizer = new Optimizer(newPlayerList, lineupMatrix, newSalaryCap);
        List<Player> optimalPlayers = optimizer.optimize();
        return new LineupCompiler(lineup, optimalPlayers).outputLineup();
    }
}
