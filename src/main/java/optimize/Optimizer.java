package optimize;

import com.google.common.collect.Sets;
import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Optimizer {

    public List<Player> generateOptimalPlayers(List<Player> playerList, LineupMatrix lineupMatrix, int salaryCap) {
        List<List<Player>> truncatedPlayerPools = truncatePlayerPoolsByPosition(playerList, lineupMatrix);
        List<Set<List<Player>>> permutedPlayerPools = getPlayerPoolCombinations(truncatedPlayerPools, lineupMatrix);
        return getBestLineupInCartesianProduct(permutedPlayerPools, salaryCap);
    }

    public List<List<Player>> truncatePlayerPoolsByPosition(List<Player> playerList, LineupMatrix lineupMatrix) {
        List<List<Player>> truncatedPlayerPools = new ArrayList<>();
        for (String lineupPosition : lineupMatrix.getUniquePositions()) {
            List<Player> playerPool = playerList.stream()
                    .filter(player -> lineupPosition.equals("any") ||
                                    Arrays.asList(lineupPosition.split(",")).contains(player.position) ||
                                    Arrays.asList(player.position.split("/")).contains(lineupPosition)
                    )
                    .sorted(Comparator.comparingDouble(player -> player.salary / player.projection))
                    .collect(Collectors.toList());
            List<Player> playerPoolSublist = playerPool.subList(0,
                    Math.min(lineupMatrix.getPositionThreshold(lineupPosition), playerPool.size()));
            truncatedPlayerPools.add(playerPoolSublist);
        }
        return truncatedPlayerPools;
    }

    public List<Set<List<Player>>> getPlayerPoolCombinations(List<List<Player>> playerPools, LineupMatrix lineupMatrix) {
        List<Set<List<Player>>> playerPoolCombinations = new ArrayList<>();
        List<String> uniquePositions = lineupMatrix.getUniquePositions();
        for (int i = 0; i < playerPools.size(); i++) {
            List<Player> playerPool = playerPools.get(i);
            String position = uniquePositions.get(i);
            int n = playerPools.get(i).size();
            int k = Math.abs(lineupMatrix.getPositionFrequency(position));
            Iterator<int[]> iterator = CombinatoricsUtils.combinationsIterator(n, k);
            Set<List<Player>> playerCombinations = new HashSet<>();
            while (iterator.hasNext()) {
                final int[] combination = iterator.next();
                List<Player> playerCombination = Arrays.stream(combination)
                        .mapToObj(playerPool::get)
                        .sorted((player1, player2) -> Double.compare(player2.projection, player1.projection))
                        .collect(Collectors.toList());
                playerCombinations.add(playerCombination);
            }
            playerPoolCombinations.add(playerCombinations);
        }
        return playerPoolCombinations;
    }

    public List<Player> getBestLineupInCartesianProduct(List<Set<List<Player>>> playerPools, int salaryCap) {
        List<Player> optimalLineup = new ArrayList<>();
        double maxPoints = 0;
        Set<List<List<Player>>> cartesianProduct = Sets.cartesianProduct(playerPools);
        for (List<List<Player>> tuple : cartesianProduct) {
            List<Player> lineup = tuple.stream().flatMap(List::stream).collect(Collectors.toList());
            if (areNoDuplicates(lineup) && lineupIsBetter(lineup, salaryCap, maxPoints)) {
                maxPoints = totalProjection(lineup);
                optimalLineup = lineup;
            }
        }
        return optimalLineup;
    }

    public boolean areNoDuplicates(List<Player> lineup) {
        return lineup.size() == lineup.stream().distinct().count();
    }

    public boolean lineupIsBetter(List<Player> lineup, int salaryCap, double maxPoints) {
        return totalSalary(lineup) <= salaryCap && totalProjection(lineup) > maxPoints;
    }

    private double totalProjection(List<Player> lineup) {
        return lineup.stream().mapToDouble(player -> player.projection).sum();
    }

    private double totalSalary(List<Player> lineup) {
        return lineup.stream().mapToInt(player -> player.salary).sum();
    }
}
