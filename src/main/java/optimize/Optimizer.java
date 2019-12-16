package optimize;

import com.google.common.collect.Sets;
import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Optimizer {
    private List<Player> playerList;
    private LineupMatrix lineupMatrix;
    private int salaryCap;

    public Optimizer(List<Player> playerList, LineupMatrix lineupMatrix, int salaryCap) {
        this.playerList = playerList;
        this.lineupMatrix = lineupMatrix;
        this.salaryCap = salaryCap;
    }

    public List<Player> optimize() {
        List<List<Player>> truncatedPlayerPools = truncatePlayerPoolsByPosition();
        List<Set<List<Player>>> permutedPlayerPools = permutePlayerPools(truncatedPlayerPools);
        return bestLineupInCartesianProduct(permutedPlayerPools);
    }

    public List<List<Player>> truncatePlayerPoolsByPosition() {
        List<List<Player>> truncatedPlayerPools = new ArrayList<>();
        for (String lineupPosition : lineupMatrix.uniquePositions()) {
            List<Player> playerPool = playerList.stream()
                    .filter(player -> lineupPosition.equals("any") ||
                                    Arrays.asList(lineupPosition.split(",")).contains(player.position) ||
                                    Arrays.asList(player.position.split("/")).contains(lineupPosition)
                    )
                    .sorted(Comparator.comparingDouble(player -> player.salary / player.projection))
                    .collect(Collectors.toList());
            List<Player> playerPoolSublist = playerPool.subList(0,
                    Math.min(lineupMatrix.positionThreshold(lineupPosition), playerPool.size()));
            truncatedPlayerPools.add(playerPoolSublist);
        }
        return truncatedPlayerPools;
    }

    public List<Set<List<Player>>> permutePlayerPools(List<List<Player>> playerPools) {
        List<Set<List<Player>>> listOfPermutations = new ArrayList<>();
        List<String> uniquePositions = lineupMatrix.uniquePositions();
        for (int i = 0; i < playerPools.size(); i++) {
            Set<List<Player>> positionPermutations = new HashSet<>();
            String position = uniquePositions.get(i);
            int n = playerPools.get(i).size();
            int k = Math.abs(lineupMatrix.positionFrequency(position));
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

    public List<Player> bestLineupInCartesianProduct(List<Set<List<Player>>> playerPools) {
        List<Player> optimalLineup = new ArrayList<>();
        double maxPoints = 0;
        Set<List<List<Player>>> cartesianProduct = Sets.cartesianProduct(playerPools);
        for (List<List<Player>> comboList : cartesianProduct) {
            List<Player> lineup = comboList.stream().flatMap(List::stream).collect(Collectors.toList());
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
