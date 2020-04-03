package optimize;

import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.*;
import java.util.stream.Collectors;

public class LineupMatrix {
    private List<String> lineupPositions;
    private List<String> uniquePositions;
    private List<Integer> positionThresholds;

    public LineupMatrix(List<String> lineupPositions, List<Player> playerPool, long maxCombinations) {
        this.lineupPositions = lineupPositions;
        this.uniquePositions = getUniquePositions();
        List<Integer> positionCounts = getPositionCounts(playerPool);
        this.positionThresholds = getPositionThresholds(positionCounts, maxCombinations);
    }

    public List<String> getUniquePositions() {
        return lineupPositions.stream().distinct().collect(Collectors.toList());
    }

    public List<Integer> getPositionCounts(List<Player> playerPool) {
        List<Integer> positionCounts = new ArrayList<>();
        Map<String, List<Player>> playerPoolGroupedByPosition = playerPool
                .stream()
                .collect(Collectors.groupingBy(player -> player.position));
        uniquePositions.forEach(position -> {
            if (position.contains(",")) {
                int positionCountSum = Arrays.stream(position.split(","))
                        .mapToInt(subPosition -> playerPoolGroupedByPosition
                                .getOrDefault(subPosition, Collections.emptyList())
                                .size())
                        .sum();
                positionCounts.add(positionCountSum);
            } else if (position.equals("any"))
                positionCounts.add(lineupPositions.size());
            else
                positionCounts.add(playerPoolGroupedByPosition
                        .getOrDefault(position, Collections.emptyList())
                        .size());
        });
        return positionCounts;
    }

    public List<Integer> getPositionThresholds(List<Integer> positionCounts, long maxCombinations) {
        List<Integer> positionThresholds = new ArrayList<>(positionCounts);
        int positionCount = positionThresholds.size();
        if (positionCount > 0) {
            int positionIndex, highestThreshold;
            while (getAllCombinations(positionThresholds) > maxCombinations) {
                highestThreshold = positionThresholds.stream().max(Integer::compare).orElse(0);
                positionIndex = positionThresholds.indexOf(highestThreshold);
                positionThresholds.set(positionIndex, positionThresholds.get(positionIndex) - 1);
            }
        }
        return positionThresholds;
    }

    public long getAllCombinations(List<Integer> positionThresholds) {
        return uniquePositions
                .stream()
                .mapToLong(position -> {
                    int n = positionThresholds.get(uniquePositions.indexOf(position));
                    int k = getPositionFrequency(position);
                    return CombinatoricsUtils.binomialCoefficient(n, k);
                })
                .reduce(1, (a, b) -> a * b);
    }

    public int getPositionFrequency(String position) {
        return Collections.frequency(lineupPositions, position);
    }

    public int getPositionThreshold(String position) {
        int positionIndex = uniquePositions.indexOf(position);
        return positionThresholds.get(positionIndex);
    }
}
