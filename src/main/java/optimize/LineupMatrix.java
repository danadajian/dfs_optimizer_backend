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
        this.positionThresholds = getPositionThresholds();
    }

    public List<String> getUniquePositions() {
        return uniquePositions;
    }

    public int getPositionFrequency(String position) {
        return Collections.frequency(lineupPositions, position);
    }

    public int getPositionThreshold(String position) {
        int positionIndex = uniquePositions.indexOf(position);
        return positionThresholds.get(positionIndex);
    }

    public List<Integer> getPositionThresholds() {
        List<Integer> positionThresholds = uniquePositions
                .stream()
                .map(position -> position.contains(",") ? 10 : Math.max(5, getPositionFrequency(position)))
                .collect(Collectors.toList());
        int positionCount = positionThresholds.size();
        if (positionCount > 0) {
            boolean continueIncrement = true;
            int positionIndex = 0;
            while (continueIncrement) {
                if (getAllCombinations(positionThresholds) < maxCombinations)
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
}
