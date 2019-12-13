package optimize;

import com.google.common.collect.Sets;
import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Optimizer {
    private List<Player> playerList, listToOptimize, lineupWithWhiteList, optimalLineup;
    private List<String> lineupMatrix, uniquePositions;

    private List<Integer> positionThresholds;
    private int salaryCap;
    private double maxPoints;

    public Optimizer(List<Player> playerList, List<Player> whiteList, List<Player> blackList,
                     List<String> lineupMatrix, int salaryCap, long maxCombinations) {
        this.playerList = playerList;
        this.salaryCap = salaryCap;
        this.maxPoints = 0;
        this.optimalLineup = new ArrayList<>();
        this.listToOptimize = playerList
                .stream()
                .filter(player -> player.projection > 0 && !whiteList.contains(player) && !blackList.contains(player))
                .collect(Collectors.toList());
        this.lineupWithWhiteList = getLineupWithWhiteList(whiteList, lineupMatrix);
        this.lineupMatrix = lineupMatrixWithoutWhiteListedPositions(lineupWithWhiteList, lineupMatrix);
        this.uniquePositions = this.lineupMatrix.stream().distinct().collect(Collectors.toList());
        this.positionThresholds = positionThresholds(maxCombinations);
    }

    public List<Player> optimize() {
        List<List<Player>> truncatedPlayerPools = getTruncatedPlayerPoolsByPosition();
        List<Set<List<Player>>> permutedPlayerPools = permutePlayerPools(truncatedPlayerPools);
        findBestLineupInCartesianProduct(permutedPlayerPools);
        return combineOptimalPlayersWithWhiteList(optimalLineup, lineupWithWhiteList);
    }

    public List<List<Player>> getTruncatedPlayerPoolsByPosition() {
        List<List<Player>> truncatedPlayerPools = new ArrayList<>();
        for (String lineupPosition : uniquePositions) {
            List<Player> playerPool = listToOptimize.stream()
                    .filter(player -> lineupPosition.equals("any") ||
                                    Arrays.asList(lineupPosition.split(",")).contains(player.position) ||
                                    Arrays.asList(player.position.split("/")).contains(lineupPosition)
                    )
                    .sorted(Comparator.comparingDouble(player -> player.salary / player.projection))
                    .collect(Collectors.toList());
            List<Player> playerPoolSublist = playerPool.subList(0,
                    Math.min(getPositionThreshold(lineupPosition), playerPool.size()));
            truncatedPlayerPools.add(playerPoolSublist);
        }
        return truncatedPlayerPools;
    }

    public List<Set<List<Player>>> permutePlayerPools(List<List<Player>> playerPools) {
        List<Set<List<Player>>> listOfPermutations = new ArrayList<>();
        for (int i = 0; i < playerPools.size(); i++) {
            Set<List<Player>> positionPermutations = new HashSet<>();
            String position = uniquePositions.get(i);
            int n = playerPools.get(i).size();
            int k = Math.abs(positionCount(position));
            Iterator<int[]> iterator = CombinatoricsUtils.combinationsIterator(n, k);
            while (iterator.hasNext()) {
                final int[] combination = iterator.next();
                int poolsIndex = i;
                List<Player> players = Arrays.stream(combination)
                        .mapToObj(index -> playerPools.get(poolsIndex).get(index))
                        .sorted((player1, player2) -> Double.compare(player2.projection, player1.projection))
                        .collect(Collectors.toList());
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

    public void saveLineupIfBetter(List<Player> lineup) {
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

    public double totalProjection(List<Player> lineup) {
        return lineup.stream().mapToDouble(player -> player.projection).sum();
    }

    public int totalSalary(List<Player> lineup) {
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
        return position.contains(",") ? -1 : Collections.frequency(lineupMatrix, position);
    }

    public List<Integer> positionFrequencyMatrix() {
        return uniquePositions
                .stream()
                .map(this::positionCount)
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Integer> positionThresholds(long maxCombinations) {
        List<Integer> frequencyMatrix = positionFrequencyMatrix();
        List<Integer> positionThresholds = frequencyMatrix
                .stream()
                .map(threshold -> threshold == -1 ? 10 : 5)
                .collect(Collectors.toList());
        boolean continueIncrement = true;
        int positionIndex = positionThresholds.size() - 1;
        while (continueIncrement) {
            long product = 1;
            for (int i = 0; i < positionThresholds.size(); i++) {
                int n = positionThresholds.get(i);
                int k = Math.abs(frequencyMatrix.get(i));
                long combinations = CombinatoricsUtils.binomialCoefficient(Math.max(n, k), k);
                product *= combinations;
            }
            int currentThreshold = positionThresholds.get(positionIndex);
            if (product < maxCombinations) {
                positionThresholds.set(positionIndex, currentThreshold + 1);
            } else {
                int lastPositionIndex = (positionIndex + 1) % positionThresholds.size();
                positionThresholds.set(lastPositionIndex, positionThresholds.get(lastPositionIndex) - 1);
                continueIncrement = false;
            }
            positionIndex = (positionIndex == 0) ? positionThresholds.size() - 1 : positionIndex - 1;
        }
        return positionThresholds;
    }

    public int getPositionThreshold(String position) {
        List<Integer> frequencyMatrix = positionFrequencyMatrix();
        return IntStream.range(0, frequencyMatrix.size())
                .filter(index -> frequencyMatrix.get(index) == positionCount(position))
                .map(positionThresholds::get)
                .findFirst()
                .orElse(-1);
    }

    public List<String> getLineupMatrix() {
        return lineupMatrix;
    }

    public void setListToOptimize(List<Player> listToOptimize) {
        this.listToOptimize = listToOptimize;
    }

    public void setLineupMatrix(List<String> lineupMatrix) {
        this.lineupMatrix = lineupMatrix;
    }

    public void setUniquePositions(List<String> uniquePositions) {
        this.uniquePositions = uniquePositions;
    }

    public void setPositionThresholds(List<Integer> positionThresholds) {
        this.positionThresholds = positionThresholds;
    }

    public void setOptimalLineup(List<Player> optimalLineup) {
        this.optimalLineup = optimalLineup;
    }

    public void setMaxPoints(double maxPoints) {
        this.maxPoints = maxPoints;
    }

    public void setSalaryCap(int salaryCap) {
        this.salaryCap = salaryCap;
    }

    public List<Player> getOptimalLineup() {
        return this.optimalLineup;
    }
}
