package optimize;

import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerSelector {
    private static final int NUMBER_OF_PLAYERS_TO_ADD = 3;

    public List<List<Player>> truncatePlayerPoolsByPosition(List<Player> playerPool, LineupMatrix lineupMatrix) {
        List<List<Player>> truncatedPlayerPools = new ArrayList<>();
        for (String lineupPosition : lineupMatrix.getUniquePositions()) {
            List<Player> validPlayerPoolSortedByPricePerPoint = playerPool
                    .stream()
                    .filter(player -> playerHasValidPosition(player, lineupPosition))
                    .sorted(Comparator.comparingDouble(player -> player.salary / player.projection))
                    .collect(Collectors.toList());
            List<Player> truncatedPlayerPool = validPlayerPoolSortedByPricePerPoint
                    .subList(0, Math.min(
                            lineupMatrix.getPositionThreshold(lineupPosition),
                            validPlayerPoolSortedByPricePerPoint.size()
                    ));
            List<Player> finalTruncatedPool = addTopProjectedPlayersToTruncatedPool(truncatedPlayerPool, playerPool,
                    lineupPosition);
            System.out.println("Opty will include " + truncatedPlayerPool.size() +
                    " + " + (finalTruncatedPool.size() - truncatedPlayerPool.size()) + " = "
                    + finalTruncatedPool.size() + " players for position: " + lineupPosition);
            truncatedPlayerPools.add(finalTruncatedPool);
        }
        return truncatedPlayerPools;
    }

    public List<Player> addTopProjectedPlayersToTruncatedPool(List<Player> truncatedPlayerPool,
                                                              List<Player> playerPool, String lineupPosition) {
        List<Player> validPlayerPoolSortedByDescendingProjection = playerPool
                .stream()
                .filter(player -> playerHasValidPosition(player, lineupPosition))
                .sorted((player1, player2) -> Double.compare(player2.projection, player1.projection))
                .collect(Collectors.toList());
        List<Player> playerPoolWithoutTruncatedPool = validPlayerPoolSortedByDescendingProjection
                .stream()
                .filter(player -> !truncatedPlayerPool.contains(player))
                .collect(Collectors.toList());
        List<Player> topProjectedPlayersToAdd = playerPoolWithoutTruncatedPool
                .subList(0, Math.min(NUMBER_OF_PLAYERS_TO_ADD, playerPoolWithoutTruncatedPool.size()));
        Set<Player> pricePerPointSet = new LinkedHashSet<>(truncatedPlayerPool);
        pricePerPointSet.addAll(topProjectedPlayersToAdd);
        return new ArrayList<>(pricePerPointSet);
    }

    public List<Set<List<Player>>> getPlayerPoolCombinations(List<List<Player>> playerPools, LineupMatrix lineupMatrix) {
        List<Set<List<Player>>> playerPoolCombinations = new ArrayList<>();
        List<String> uniquePositions = lineupMatrix.getUniquePositions();
        for (int i = 0; i < playerPools.size(); i++) {
            List<Player> playerPool = playerPools.get(i);
            String position = uniquePositions.get(i);
            int n = playerPools.get(i).size();
            int k = lineupMatrix.getPositionFrequency(position);
            Iterator<int[]> iterator = CombinatoricsUtils.combinationsIterator(n, k);
            Set<List<Player>> playerCombinations = new HashSet<>();
            while (iterator.hasNext()) {
                final int[] combination = iterator.next();
                List<Player> playerCombination = Arrays
                        .stream(combination)
                        .mapToObj(playerPool::get)
                        .sorted((player1, player2) -> Double.compare(player2.projection, player1.projection))
                        .collect(Collectors.toList());
                playerCombinations.add(playerCombination);
            }
            playerPoolCombinations.add(playerCombinations);
        }
        return playerPoolCombinations;
    }

    public boolean playerHasValidPosition(Player player, String lineupPosition) {
        return lineupPosition.equals("any") ||
                Arrays.asList(lineupPosition.split(",")).contains(player.position) ||
                Arrays.asList(player.position.split("/")).contains(lineupPosition);
    }
}
