package handler;

import optimize.Optimizer;
import optimize.Player;

import java.util.*;
import java.util.stream.Collectors;

public class OptimizerHandler {
    public List<Integer> handleRequest(Map<String, Object> input) {
        List<Player> playerList = new ArrayList<>();
        List<Player> whiteList = new ArrayList<>();
        List<Player> blackList = new ArrayList<>();
        List<String> lineupMatrix = new ArrayList<>();
        for (Object object : (List) input.get("players")) {
            Map playerMap = (Map) object;
            int playerId = (int) playerMap.get("playerId");
            String position = (String) playerMap.get("position");
            double projection = ((Number) playerMap.get("projection")).doubleValue();
            int salary = (int) playerMap.get("salary");
            playerList.add(new Player(playerId, position, projection, salary));
        }
        for (Object object : (List) input.get("whiteList")) {
            int playerId = (int) object;
            whiteList.add(new Player(playerId));
        }
        for (Object object : (List) input.get("blackList")) {
            int playerId = (int) object;
            blackList.add(new Player(playerId));
        }
        for (Object object : (List) input.get("lineupMatrix")) {
            String position = (String) object;
            lineupMatrix.add(position);
        }
        int salaryCap = (int) input.get("salaryCap");
        Optimizer optimizer = new Optimizer(playerList, whiteList, blackList, lineupMatrix, salaryCap, 3000000);
        List<Player> optimalLineup = optimizer.getOptimalLineup();
        return optimalLineup.stream().map(player -> player.playerId).collect(Collectors.toList());
    }
}
