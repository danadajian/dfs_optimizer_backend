package optimize;

import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Optimizer {
    private List<Player> playerList, whiteList, blackList, optimalLineup;
    private List<String> lineupMatrix;
    private int salaryCap, poolThreshold;
    private double maxPoints;
    private long permutationCounter;

    public Optimizer(List<Player> playerList, List<Player> whiteList, List<Player> blackList,
                     List<String> lineupMatrix, int salaryCap, int poolThreshold) {
        this.playerList = playerList;
        this.whiteList = whiteList;
        this.blackList = blackList;
        this.lineupMatrix = lineupMatrix;
        this.salaryCap = salaryCap;
        this.poolThreshold = poolThreshold;
        this.maxPoints = 0;
        this.optimalLineup = new ArrayList<>();
        this.permutationCounter = 0;
    }

    public List<Player> getOptimalLineup() {
        List<Player> lineupWithWhiteList = getLineupWithWhiteList();
        if (whiteList.size() > 0)
            lineupMatrix = removeWhiteListedPositionsFromLineupMatrix(lineupMatrix, lineupWithWhiteList);
        List<List<Player>> playerPools = getCheapestPlayersPerProjectionByPositionWithoutWhiteOrBlackList();
        List<Integer> startingIndices = IntStream.rangeClosed(0, 4).boxed().collect(Collectors.toList());
        if (lineupMatrix.stream().allMatch(position -> position.equals("any")))
            permuteSingleGameLineups(getPlayerListWithoutWhiteOrBlackList(), lineupMatrix.size());
        else {
            for (int i = 0; i < poolThreshold; i++)
                permuteLineups(startingIndices, lineupMatrix.size(), Collections.singletonList(i), playerPools);
        }
        if (lineupMatrix.stream().allMatch(position -> position.equals("any")))
            optimalLineup.sort((player1, player2) -> Double.compare(player2.projection, player1.projection));
        return combineOptimalPlayersWithWhiteList(optimalLineup, lineupWithWhiteList);
    }

    public List<Player> getLineupWithWhiteList() {
        List<Player> lineupWithWhiteListedPlayers = new ArrayList<>();
        lineupMatrix.forEach(position -> lineupWithWhiteListedPlayers.add(new Player()));
        for (Player player : whiteList) {
            Player playerInPool = playerList.stream()
                    .filter(playerPoolPlayer -> playerPoolPlayer.equals(player))
                    .findFirst()
                    .orElse(new Player());
            int lineupIndex = getLineupIndex(playerInPool);
            if (lineupIndex > -1) {
                lineupWithWhiteListedPlayers.set(lineupIndex, playerInPool);
                salaryCap -= playerInPool.salary;
            }
        }
        return lineupWithWhiteListedPlayers;
    }

    public List<String> removeWhiteListedPositionsFromLineupMatrix(List<String> lineupMatrixToChange,
                                                                   List<Player> lineupWithWhiteList) {
        List<String> positionsToRemove = new ArrayList<>();
        for (Player player : lineupWithWhiteList) {
            if (player.playerId > 0) {
                int lineupIndex = getLineupIndex(player);
                positionsToRemove.add(lineupMatrixToChange.get(lineupIndex));
            }
        }
        positionsToRemove.forEach(lineupMatrixToChange::remove);
        return lineupMatrixToChange;
    }

    public List<List<Player>> getCheapestPlayersPerProjectionByPositionWithoutWhiteOrBlackList() {
        List<List<Player>> playerPools = new ArrayList<>();
        for (String position : lineupMatrix) {
            List<Player> playerPool = playerList.stream()
                    .filter(
                            player -> (position.equals("any") ||
                                    Arrays.asList(position.split(",")).contains(player.position))
                                    && !whiteList.contains(player)
                                    && !blackList.contains(player)
                    )
                    .sorted(Comparator.comparingDouble(player -> player.salary / player.projection))
                    .collect(Collectors.toList())
                    .subList(0, poolThreshold);
            playerPools.add(playerPool);
        }
        return playerPools;
    }

    private List<Player> getPlayerListWithoutWhiteOrBlackList() {
        return playerList.stream().filter(player -> !whiteList.contains(player) && !blackList.contains(player)).collect(Collectors.toList());
    }

    public void permuteLineups(List<Integer> indexList, int lineupSize, List<Integer> baseList,
                               List<List<Player>> playerPools) {
        if (baseList.size() < lineupSize) {
            for (int index : indexList) {
                List<Integer> listCopy = new ArrayList<>(baseList);
                listCopy.add(index);
                permuteLineups(indexList, lineupSize, listCopy, playerPools);
            }
        } else {
            permutationCounter += 1;
            List<Player> lineup = getLineupFromIndexList(baseList, playerPools);
            if (isValidLineup(lineup))
                saveLineupIfBetter(lineup);
        }
    }

    public void permuteSingleGameLineups(List<Player> playerList, int lineupSize) {
        Iterator<int[]> iterator = CombinatoricsUtils.combinationsIterator(playerList.size(), lineupSize);
        while (iterator.hasNext()) {
            final int[] combination = iterator.next();
            permutationCounter += 1;
            List<Player> lineup = getLineupFromIndexList(combination, playerList);
            saveLineupIfBetter(lineup);
        }
    }

    private void saveLineupIfBetter(List<Player> lineup) {
        double totalPoints = totalProjection(lineup);
        int totalSalary = totalSalary(lineup);
        if (totalSalary <= salaryCap && totalPoints > maxPoints) {
            maxPoints = totalPoints;
            optimalLineup = lineup;
        }
    }

    public List<Player> getLineupFromIndexList(int[] combination, List<Player> playerList) {
        List<Player> lineup = new ArrayList<>();
        for (int index : combination) {
            lineup.add(playerList.get(index));
        }
        return lineup;
    }

    public List<Player> combineOptimalPlayersWithWhiteList(List<Player> optimalPlayers, List<Player> lineupWithWhiteList) {
        List<Player> optimalLineupWithWhiteList = new ArrayList<>(lineupWithWhiteList);
        for (Player player : optimalPlayers) {
            int emptySpotIndex = IntStream.range(0, optimalLineupWithWhiteList.size())
                    .filter(index -> optimalLineupWithWhiteList.get(index).playerId == 0)
                    .findFirst()
                    .orElse(-1);
            optimalLineupWithWhiteList.set(emptySpotIndex, player);
        }
        return optimalLineupWithWhiteList;
    }

    public boolean isValidLineup(List<Player> lineup) {
        int lineupSize = lineupMatrix.size();
        for (int i = 0; i < lineupSize - 1; i++) {
            for (int j = i + 1; j < lineupSize; j++) {
                if (lineupMatrix.get(j).contains(lineupMatrix.get(i)) && lineup.get(i).equals(lineup.get(j)))
                    return false;
                }
            }
        return true;
    }

    public List<Player> getLineupFromIndexList(List<Integer> indexList, List<List<Player>> playerPools) {
        List<Player> lineup = new ArrayList<>();
        for (int i = 0; i < indexList.size(); i++) {
            lineup.add(playerPools.get(i).get(indexList.get(i)));
        }
        return lineup;
    }

    public double totalProjection(List<Player> lineup) {
        return lineup.stream().mapToDouble(player -> player.projection).sum();
    }

    public int totalSalary(List<Player> lineup) {
        return lineup.stream().mapToInt(player -> player.salary).sum();
    }

    private int getLineupIndex(Player player) {
        return IntStream.range(0, lineupMatrix.size())
                .filter(i -> lineupMatrix.get(i).equals("any") ||
                        Arrays.asList(lineupMatrix.get(i).split(",")).contains(player.position))
                .findFirst()
                .orElse(-1);
    }

    public long getPermutationCounter() {
        return permutationCounter;
    }
}
