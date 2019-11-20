package handler;

import optimize.Optimizer;
import optimize.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OptimizerHandler {
    public Map<Integer, String> handleRequest(Map<String, List<Object>> input) {
        List<Player> playerList = new ArrayList<>();
        List<Player> whiteList = new ArrayList<>();
        List<Player> blackList = new ArrayList<>();
        List<String> lineupMatrix = new ArrayList<>();
        for (Object object : input.get("players")) {
            Map playerMap = (Map) object;
            int playerId = (int) playerMap.get("playerId");
            String position = (String) playerMap.get("position");
            double projection = (double) playerMap.get("projection");
            int salary = (int) playerMap.get("salary");
            playerList.add(new Player(playerId, position, projection, salary));
        }
        for (Object object : input.get("whiteList")) {
            Map playerMap = (Map) object;
            int playerId = (int) playerMap.get("playerId");
            whiteList.add(new Player(playerId));
        }
        for (Object object : input.get("blackList")) {
            Map playerMap = (Map) object;
            int playerId = (int) playerMap.get("playerId");
            blackList.add(new Player(playerId));
        }
        for (Object object : input.get("lineupMatrix")) {
            String position = (String) object;
            lineupMatrix.add(position);
        }
        return new Optimizer(playerList, whiteList, blackList, lineupMatrix).optimize();
    }
}
