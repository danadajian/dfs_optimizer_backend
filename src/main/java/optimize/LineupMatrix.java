package optimize;

import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LineupMatrix {
    private List<String> lineupPositions;
    private long maxCombinations;
    private List<Integer> positionFrequencies;
    private List<Integer> positionThresholds;

    public LineupMatrix(List<String> lineupPositions, long maxCombinations) {
        this.lineupPositions = lineupPositions;
        this.maxCombinations = maxCombinations;
        this.positionFrequencies = positionFrequencies();
        this.positionThresholds = positionThresholds();
    }

    public List<Integer> positionFrequencies() {
        return lineupPositions
                .stream()
                .distinct()
                .map(this::positionFrequency)
                .sorted()
                .collect(Collectors.toList());
    }

    public int positionFrequency(String position) {
        return position.contains(",") ? -1 : Collections.frequency(lineupPositions, position);
    }

    public int positionThreshold(String position) {
        List<Integer> frequencyMatrix = positionFrequencies();
        return IntStream.range(0, frequencyMatrix.size())
                .filter(index -> frequencyMatrix.get(index) == positionFrequency(position))
                .map(positionThresholds::get)
                .findFirst()
                .orElse(-1);
    }

    public List<Integer> positionThresholds() {
        List<Integer> positionThresholds = positionFrequencies
                .stream()
                .map(threshold -> threshold == -1 ? 10 : 5)
                .collect(Collectors.toList());
        boolean continueIncrement = true;
        int positionIndex = positionThresholds.size() - 1;
        while (continueIncrement) {
            long product = 1;
            for (int i = 0; i < positionThresholds.size(); i++) {
                int n = positionThresholds.get(i);
                int k = Math.abs(positionFrequencies.get(i));
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

    public List<String> uniquePositions() {
        return lineupPositions.stream().distinct().collect(Collectors.toList());
    }
}
