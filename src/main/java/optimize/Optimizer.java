package optimize;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Optimizer {
    private List<Player> playerList;
    private List<Player> whiteList;
    private List<Player> blackList;
    private List<String> lineupMatrix;
    private List<List<Player>> playerPools;

    public Optimizer(List<Player> playerList, List<Player> whiteList, List<Player> blackList,
                     List<String> lineupMatrix) {
        this.playerList = playerList;
        this.whiteList = whiteList;
        this.blackList = blackList;
        this.lineupMatrix = lineupMatrix;
        this.playerPools = getPlayerPools();
    }

    public Map<Integer, String> optimize() {
        return new HashMap<>();
    }

    public Player selectPlayerToDowngrade(List<Player> lineup) {
        Player playerWithLowestDownGradeCost = new Player();
        double lowestDowngradeCost = 0;
        for (int i = 0; i < lineup.size(); i++) {
            Player player = lineup.get(i);
            List<Player> downgradePool = playerPools.get(i);
            int playerIndex = downgradePool.indexOf(player);
            Player nextPlayer = downgradePool.get(playerIndex + 1);
            double valueRatio = player.projection / player.salary;
            double nextPlayerValueRatio = nextPlayer.projection / nextPlayer.salary;
            double downgradeCost = valueRatio - nextPlayerValueRatio;
            if (lowestDowngradeCost == 0 || downgradeCost < lowestDowngradeCost) {
                playerWithLowestDownGradeCost = player;
                lowestDowngradeCost = downgradeCost;
            }
        }
        return playerWithLowestDownGradeCost;
    }

    public List<Player> getBestLineupWithWhiteListedPlayers() {
        List<Player> initialLineup = getLineupWithWhiteListedPlayers();
        for (int i = 0; i < lineupMatrix.size(); i++) {
            if (initialLineup.get(i).playerId == 0) {
                Player bestPlayer = playerPools.get(i).stream()
                        .filter(player -> !initialLineup.contains(player))
                        .findFirst()
                        .orElse(new Player());
                initialLineup.set(i, bestPlayer);
            }
        }
        return initialLineup;
    }

    public List<Player> getLineupWithWhiteListedPlayers() {
        List<Player> lineupWithWhiteList = new ArrayList<>();
        lineupMatrix.forEach(player -> lineupWithWhiteList.add(new Player()));
        for (Player player : whiteList) {
            int index = IntStream.range(0, lineupMatrix.size())
                    .filter(i -> Arrays.asList(lineupMatrix.get(i).split(",")).contains(player.position))
                    .findFirst()
                    .orElse(-1);
            lineupWithWhiteList.set(index, player);
        }
        return lineupWithWhiteList;
    }

    public List<List<Player>> getPlayerPools() {
        List<List<Player>> playerPools = new ArrayList<>();
        for (String position : lineupMatrix) {
            List<Player> playerPool = playerList.stream()
                    .filter(
                            player -> Arrays.asList(position.split(",")).contains(player.position)
                                    && !blackList.contains(player)
                    )
                    .sorted((player1, player2) -> Double.compare(player2.projection, player1.projection))
                    .collect(Collectors.toList());
            playerPools.add(playerPool);
        }
        return playerPools;
    }
}
