package optimize;

import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LineupMatrix {
    private List<String> lineupPositions;
    private long maxCombinations;
    private List<String> uniquePositions;
    private List<Integer> positionThresholds;

    public LineupMatrix(List<String> lineupPositions, long maxCombinations) {
        this.lineupPositions = lineupPositions;
        this.maxCombinations = maxCombinations;
        this.uniquePositions = lineupPositions.stream().distinct().collect(Collectors.toList());
        this.positionThresholds = positionThresholds();
    }

    public List<String> uniquePositions() {
        return uniquePositions;
    }

    public int positionFrequency(String position) {
        return Collections.frequency(lineupPositions, position);
    }

    public int positionThreshold(String position) {
        int positionIndex = uniquePositions.indexOf(position);
        return positionThresholds.get(positionIndex);
    }

    public List<Integer> positionThresholds() {
        List<Integer> positionThresholds = uniquePositions
                .stream()
                .map(position -> position.contains(",") ? 10 : Math.max(5, positionFrequency(position)))
                .collect(Collectors.toList());
        int positionCount = positionThresholds.size();
        if (positionCount > 0) {
            boolean continueIncrement = true;
            int positionIndex = 0;
            while (continueIncrement) {
                if (totalCombinations(positionThresholds) < maxCombinations)
                    positionThresholds.set(positionIndex, positionThresholds.get(positionIndex) + 1);
                else {
                    int lastPositionIndex = (positionIndex + positionCount - 1) % positionCount;
                    positionThresholds.set(lastPositionIndex, positionThresholds.get(lastPositionIndex) - 1);
                    continueIncrement = false;
                }
                positionIndex = (positionIndex + 1) % positionCount;
            }
        }
        return positionThresholds;
    }

    public long totalCombinations(List<Integer> positionThresholds) {
        return uniquePositions
                .stream()
                .mapToLong(position -> {
                    int n = positionThresholds.get(uniquePositions.indexOf(position));
                    int k = positionFrequency(position);
                    return CombinatoricsUtils.binomialCoefficient(n, k);
                })
                .reduce(1, (a, b) -> a * b);
    }
}
