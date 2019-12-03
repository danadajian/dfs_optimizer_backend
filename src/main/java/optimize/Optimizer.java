package optimize;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Optimizer {
    private List<Player> playerList, whiteList, blackList;
    private List<String> lineupMatrix;
    private int salaryCap;
    private List<List<Player>> playerPools;

    public Optimizer(List<Player> playerList, List<Player> whiteList, List<Player> blackList,
                     List<String> lineupMatrix, int salaryCap) {
        this.playerList = playerList;
        this.whiteList = whiteList;
        this.blackList = blackList;
        this.lineupMatrix = lineupMatrix;
        this.salaryCap = salaryCap;
        this.playerPools = getSortedPlayerPoolsWithoutBlackList();
    }

    public List<Player> optimize() {
        return downgradePlayersUntilUnderCap(bestLineupWithWhiteList());
    }

    public List<Player> bestLineupWithWhiteList() {
        List<Player> bestLineup = lineupWithWhiteList();
        for (int i = 0; i < lineupMatrix.size(); i++) {
            if (bestLineup.get(i).playerId == 0) {
                Player bestPlayer = playerPools.get(i).stream()
                        .filter(player -> !bestLineup.contains(player))
                        .findFirst()
                        .orElse(new Player());
                bestLineup.set(i, bestPlayer);
            }
        }
        return bestLineup;
    }

    public List<Player> downgradePlayersUntilUnderCap(List<Player> lineup) {
        List<Player> lineupUnderCap = new ArrayList<>(lineup);
        while (totalSalary(lineupUnderCap) > salaryCap) {
            lineupUnderCap = downgradeLowestCostNonWhiteListPlayer(lineupUnderCap);
        }
        return lineupUnderCap;
    }

    public List<Player> downgradeLowestCostNonWhiteListPlayer(List<Player> lineup) {
        List<Player> newLineup = new ArrayList<>(lineup);
        double lowestDowngradeCost = 0;
        int downgradeIndex = -1;
        Player playerToSubIn = new Player();
        for (int i = 0; i < lineup.size(); i++) {
            Player player = lineup.get(i);
            if (whiteList.contains(player))
                continue;
            List<Player> downgradePool = playerPools.get(i);
            int playerIndex = downgradePool.indexOf(player);
            Player nextPlayer = findNextPlayer(playerIndex, lineup, downgradePool);
            if (nextPlayer.playerId == 0)
                continue;
            double playerValueRatio = player.projection / player.salary;
            double nextPlayerValueRatio = nextPlayer.projection / nextPlayer.salary;
            double downgradeCost = playerValueRatio - nextPlayerValueRatio;
            if (downgradeCost < lowestDowngradeCost || lowestDowngradeCost == 0) {
                downgradeIndex = i;
                playerToSubIn = nextPlayer;
                lowestDowngradeCost = downgradeCost;
            }
        }
        if (downgradeIndex > -1)
            newLineup.set(downgradeIndex, playerToSubIn);
        return lowestDowngradeCost == 0 ? new ArrayList<>() : newLineup;
    }

    public List<Player> lineupWithWhiteList() {
        List<Player> lineupWithWhiteListedPlayers = new ArrayList<>();
        lineupMatrix.forEach(position -> lineupWithWhiteListedPlayers.add(new Player()));
        for (Player player : whiteList) {
            Player playerInPool = playerList.stream()
                    .filter(playerPoolPlayer -> playerPoolPlayer.equals(player))
                    .findFirst()
                    .orElse(new Player());
            int lineupIndex = IntStream.range(0, lineupMatrix.size())
                    .filter(i -> lineupMatrix.get(i).equals("any") ||
                            Arrays.asList(lineupMatrix.get(i).split(",")).contains(playerInPool.position))
                    .findFirst()
                    .orElse(-1);
            lineupWithWhiteListedPlayers.set(lineupIndex, playerInPool);
        }
        return lineupWithWhiteListedPlayers;
    }

    private Player findNextPlayer(int playerIndex, List<Player> lineup, List<Player> downgradePool) {
        return IntStream.range(0, downgradePool.size())
                .filter(index -> index > playerIndex && !lineup.contains(downgradePool.get(index)))
                .mapToObj(downgradePool::get)
                .findFirst()
                .orElse(new Player());
    }

    public int totalSalary(List<Player> lineup) {
        return lineup.stream().mapToInt(player -> player.salary).sum();
    }

    public List<List<Player>> getSortedPlayerPoolsWithoutBlackList() {
        List<List<Player>> playerPools = new ArrayList<>();
        for (String position : lineupMatrix) {
            List<Player> playerPool = playerList.stream()
                    .filter(
                            player -> (position.equals("any") ||
                                    Arrays.asList(position.split(",")).contains(player.position))
                                    && !blackList.contains(player)
                    )
                    .sorted((player1, player2) -> Double.compare(player2.projection, player1.projection))
                    .collect(Collectors.toList());
            playerPools.add(playerPool);
        }
        return playerPools;
    }
}
