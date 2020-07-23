package optimize;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Optimizer {
    List<Set<List<Player>>> playerPools;
    int salaryCap;
    LineupRestrictions lineupRestrictions;
    LineupValidator lineupValidator = new LineupValidator();
    double maxPoints;
    List<Player> optimalLineup;

    public List<Player> generateOptimalLineup(List<Set<List<Player>>> playerPools, int salaryCap,
                                              LineupRestrictions lineupRestrictions) {
        this.playerPools = playerPools;
        this.salaryCap = salaryCap;
        this.maxPoints = 0;
        this.lineupRestrictions = lineupRestrictions;
        optimize(Collections.emptyList(), -1);
        return optimalLineup;
    }

    public void optimize(List<Player> lineup, int poolsIndex) {
        boolean lineupIsFull = ++poolsIndex == playerPools.size();
        if (!lineupIsFull)
            recursivelyCheckLineups(lineup, poolsIndex);
        else if (lineupIsBetter(lineup) && lineupValidator.lineupSatisfiesDistinctTeamsRequired(lineup, lineupRestrictions)) {
            maxPoints = totalProjection(lineup);
            optimalLineup = lineup;
        }
    }

    public void recursivelyCheckLineups(List<Player> lineup, int poolsIndex) {
        Set<List<Player>> positionCombos = playerPools.get(poolsIndex);
        for (List<Player> players : positionCombos) {
            List<Player> lineupFragment = Stream.of(lineup, players)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            boolean shouldContinueBuildingLineup =
                    lineupValidator.lineupContainsNoDuplicates(lineupFragment) &&
                    lineupValidator.lineupSatisfiesMaxPlayersPerTeam(lineupFragment, lineupRestrictions) &&
                    canFindABetterLineup(lineupFragment, playerPools);
            if (shouldContinueBuildingLineup)
                optimize(lineupFragment, poolsIndex);
        }
    }

    public boolean canFindABetterLineup(List<Player> currentLineup, List<Set<List<Player>>> playerPools) {
        int numberOfPositionsFilled = getDistinctNumberOfPositionTypesFilled(currentLineup);
        List<Set<List<Player>>> remainingPlayerPools = playerPools.subList(numberOfPositionsFilled, playerPools.size());
        double maxPointsToAdd = getMaxPointsToAdd(remainingPlayerPools);
        int minSalaryToAdd = getMinSalaryToAdd(remainingPlayerPools);
        return totalProjection(currentLineup) + maxPointsToAdd > maxPoints &&
                totalSalary(currentLineup) + minSalaryToAdd <= salaryCap;
    }

    public int getDistinctNumberOfPositionTypesFilled(List<Player> currentLineup) {
        return IntStream.range(0, currentLineup.size())
                .map(i -> {
                    String playerPosition = currentLineup.get(i).position;
                    String precedingPlayerPosition = i == 0 ? "N/A" : currentLineup.get(i - 1).position;
                    return !playerPosition.equals(precedingPlayerPosition) ? 1 : 0;
                })
                .sum();
    }

    public double getMaxPointsToAdd(List<Set<List<Player>>> remainingPlayerPools) {
        return remainingPlayerPools.stream()
                .mapToDouble(playerPool -> playerPool.stream()
                        .mapToDouble(this::totalProjection)
                        .max()
                        .orElse(0)
                )
                .sum();
    }

    public int getMinSalaryToAdd(List<Set<List<Player>>> remainingPlayerPools) {
        return remainingPlayerPools.stream()
                .mapToInt(playerPool -> playerPool.stream()
                        .mapToInt(this::totalSalary)
                        .min()
                        .orElse(0)
                )
                .sum();
    }

    public boolean lineupIsBetter(List<Player> lineup) {
        return totalProjection(lineup) > maxPoints && totalSalary(lineup) <= salaryCap;
    }

    public double totalProjection(List<Player> lineup) {
        return lineup.stream().mapToDouble(player -> player.projection).sum();
    }

    public int totalSalary(List<Player> lineup) {
        return lineup.stream().mapToInt(player -> player.salary).sum();
    }

    public void setSalaryCap(int salaryCap) {
        this.salaryCap = salaryCap;
    }

    public void setMaxPoints(double maxPoints) {
        this.maxPoints = maxPoints;
    }

    public void setPlayerPools(List<Set<List<Player>>> playerPools) {
        this.playerPools = playerPools;
    }

    public void setLineupValidator(LineupValidator lineupValidator) {
        this.lineupValidator = lineupValidator;
    }

    public double getMaxPoints() {
        return maxPoints;
    }

    public List<Player> getOptimalLineup() {
        return optimalLineup;
    }
}
