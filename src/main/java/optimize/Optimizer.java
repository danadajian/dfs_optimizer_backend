package optimize;

import com.google.common.collect.Sets;
import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Optimizer {
    private List<Player> playerList, whiteList, blackList, lineupWithWhiteList, optimalLineup;
    private List<String> lineupMatrix, uniquePositions;
    private int salaryCap;
    private double maxPoints;

    public Optimizer(List<Player> playerList, List<Player> whiteList, List<Player> blackList,
                     List<String> lineupMatrix, int salaryCap) {
        this.playerList = playerList;
        this.whiteList = whiteList;
        this.blackList = blackList;
        this.salaryCap = salaryCap;
        this.maxPoints = 0;
        this.optimalLineup = new ArrayList<>();
        this.lineupWithWhiteList = getLineupWithWhiteList(whiteList, lineupMatrix);
        this.lineupMatrix = lineupMatrixWithoutWhiteListedPositions(lineupWithWhiteList, lineupMatrix);
        this.uniquePositions = lineupMatrix.stream().distinct().collect(Collectors.toList());
    }

    public List<Player> getOptimalLineup() {
        List<List<Player>> truncatedPlayerPools = getTruncatedPlayerPoolsByPosition();
        List<Set<List<Player>>> permutedPlayerPools = permutePlayerPools(truncatedPlayerPools);
        findBestLineupInCartesianProduct(permutedPlayerPools);
        if (lineupMatrix.stream().allMatch(position -> position.equals("any")))
            optimalLineup.sort((player1, player2) -> Double.compare(player2.projection, player1.projection));
        return combineOptimalPlayersWithWhiteList(optimalLineup, lineupWithWhiteList);
    }

    public List<List<Player>> getTruncatedPlayerPoolsByPosition() {
        List<List<Player>> truncatedPlayerPools = new ArrayList<>();
        for (String position : uniquePositions) {
            List<Player> playerPool = playerList.stream()
                    .filter(
                            player -> (position.equals("any") ||
                                    Arrays.asList(position.split(",")).contains(player.position))
                                    && player.projection > 0
                                    && !whiteList.contains(player)
                                    && !blackList.contains(player)
                    )
                    .sorted(Comparator.comparingDouble(player -> player.salary / player.projection))
                    .collect(Collectors.toList());
            List<Player> playerPoolSublist = playerPool
                    .subList(0, positionThreshold(position) == -1 ? playerPool.size() : positionThreshold(position));
            truncatedPlayerPools.add(playerPoolSublist);
        }
        return truncatedPlayerPools;
    }

    public List<Set<List<Player>>> permutePlayerPools(List<List<Player>> playerPools) {
        List<Set<List<Player>>> listOfPermutations = new ArrayList<>();
        for (int i = 0; i < playerPools.size(); i++) {
            Set<List<Player>> positionPermutations = new HashSet<>();
            String position = uniquePositions.get(i);
            Iterator<int[]> iterator = CombinatoricsUtils
                    .combinationsIterator(playerPools.get(i).size(), positionCount(position));
            while (iterator.hasNext()) {
                final int[] combination = iterator.next();
                List<Player> players = getLineupFromIndexArray(combination, playerPools.get(i));
                positionPermutations.add(players);
            }
            listOfPermutations.add(positionPermutations);
        }
        return listOfPermutations;
    }

    public void findBestLineupInCartesianProduct(List<Set<List<Player>>> playerPools) {
        Set<List<List<Player>>> cartesianProduct = Sets.cartesianProduct(playerPools);
        for (List<List<Player>> comboList : cartesianProduct) {
            List<Player> lineup = comboList.stream().flatMap(List::stream).collect(Collectors.toList());
            if (isValidLineup(lineup)) {
                saveLineupIfBetter(lineup);
                // permutationCounter += 1;
            }
        }
    }

    public boolean isValidLineup(List<Player> lineup) {
        int lineupSize = lineup.size();
        for (int i = 0; i < lineupSize - 1; i++) {
            for (int j = i + 1; j < lineupSize; j++) {
                if (lineupMatrix.get(j).contains(lineupMatrix.get(i)) && lineup.get(i).equals(lineup.get(j)))
                    return false;
            }
        }
        return true;
    }

    private void saveLineupIfBetter(List<Player> lineup) {
        double totalPoints = totalProjection(lineup);
        int totalSalary = totalSalary(lineup);
        if (totalSalary <= salaryCap && totalPoints > maxPoints) {
            maxPoints = totalPoints;
            optimalLineup = lineup;
        }
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

    public List<Player> getLineupFromIndexArray(int[] combination, List<Player> playerList) {
        List<Player> lineup = new ArrayList<>();
        for (int index : combination) {
            lineup.add(playerList.get(index));
        }
        return lineup;
    }

    public List<Player> getLineupWithWhiteList(List<Player> whiteList, List<String> lineupMatrix) {
        List<Player> lineupWithWhiteListedPlayers = new ArrayList<>();
        lineupMatrix.forEach(position -> lineupWithWhiteListedPlayers.add(new Player()));
        for (Player player : whiteList) {
            Player playerInPool = playerList.stream()
                    .filter(playerPoolPlayer -> playerPoolPlayer.equals(player))
                    .findFirst()
                    .orElse(new Player());
            int lineupIndex = getLineupIndex(playerInPool, lineupMatrix);
            if (lineupIndex > -1) {
                lineupWithWhiteListedPlayers.set(lineupIndex, playerInPool);
                salaryCap -= playerInPool.salary;
            }
        }
        return lineupWithWhiteListedPlayers;
    }

    public List<String> lineupMatrixWithoutWhiteListedPositions(List<Player> lineupWithWhiteList, List<String> lineupMatrix) {
        List<String> newLineupMatrix = new ArrayList<>(lineupMatrix);
        List<String> positionsToRemove = new ArrayList<>();
        for (Player player : lineupWithWhiteList) {
            if (player.playerId > 0) {
                int lineupIndex = getLineupIndex(player, lineupMatrix);
                positionsToRemove.add(newLineupMatrix.get(lineupIndex));
            }
        }
        positionsToRemove.forEach(newLineupMatrix::remove);
        return newLineupMatrix;
    }

    private double totalProjection(List<Player> lineup) {
        return lineup.stream().mapToDouble(player -> player.projection).sum();
    }

    private int totalSalary(List<Player> lineup) {
        return lineup.stream().mapToInt(player -> player.salary).sum();
    }

    private int getLineupIndex(Player player, List<String> lineupMatrix) {
        return IntStream.range(0, lineupMatrix.size())
                .filter(i -> lineupMatrix.get(i).equals("any") ||
                        Arrays.asList(lineupMatrix.get(i).split(",")).contains(player.position))
                .findFirst()
                .orElse(-1);
    }

    private int positionCount(String position) {
        return Collections.frequency(lineupMatrix, position);
    }

    public int positionThreshold(String position) {
        if (position.contains(","))
            return 10;
        int positionCount = positionCount(position);
        if (positionCount == lineupMatrix.size())
            return -1;
        switch (positionCount) {
            case 1:
                return 5;
            case 2:
                return 8;
            case 3:
                return 9;
            case 4:
                return 13;
            default:
                return -1;
        }
    }
}
