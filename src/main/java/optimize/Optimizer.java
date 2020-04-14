package optimize;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Optimizer {
    List<Set<List<Player>>> playerPools;
    List<Player> optimalLineup;
    double maxPoints;
    int salaryCap;
    LineupRestrictions lineupRestrictions;

    public List<Player> generateOptimalLineup(List<Set<List<Player>>> playerPools, int salaryCap, LineupRestrictions lineupRestrictions) {
        this.optimalLineup = Collections.emptyList();
        this.maxPoints = 0;
        this.playerPools = playerPools;
        this.salaryCap = salaryCap;
        this.lineupRestrictions = lineupRestrictions;
        optimize(Collections.emptyList(), -1);
        return optimalLineup;
    }

    private void optimize(List<Player> lineup, int poolsIndex) {
        boolean isCompleteLineup = ++poolsIndex == playerPools.size();
        if (isCompleteLineup) {
            checkIfLineupIsBestYet(lineup);
        } else {
            Set<List<Player>> nextPlayerList = playerPools.get(poolsIndex);
            for (List<Player> players : nextPlayerList) {
                List<Player> concatenatedLineup = Stream.of(lineup, players)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                if (canFindABetterLineup(concatenatedLineup, playerPools, maxPoints, salaryCap) &&
                        satisfiesMaxPlayersPerTeam(concatenatedLineup, lineupRestrictions)) {
                    optimize(concatenatedLineup, poolsIndex);
                }
            }
        }
    }

    private void checkIfLineupIsBestYet(List<Player> lineup) {
        if (areNoDuplicates(lineup) &&
                lineupIsBetter(lineup, salaryCap, maxPoints) &&
                satisfiesDistinctTeamsRequired(lineup, lineupRestrictions) &&
                satisfiesMaxPlayersPerTeam(lineup, lineupRestrictions)) {
            maxPoints = totalProjection(lineup);
            optimalLineup = lineup;
        }
    }

    public boolean canFindABetterLineup(List<Player> currentLineup, List<Set<List<Player>>> playerPools,
                                        double maxPoints, int salaryCap) {
        int numberOfPositionsFilled = getDistinctNumberOfPositionTypesFilled(currentLineup);
        List<Set<List<Player>>> remainingPlayerPools = playerPools.subList(numberOfPositionsFilled, playerPools.size());
        double maxPointsToAdd = getMaxPointsToAdd(remainingPlayerPools);
        int minSalaryToAdd = getMinSalaryToAdd(remainingPlayerPools);
        return totalProjection(currentLineup) + maxPointsToAdd > maxPoints &&
                totalSalary(currentLineup) + minSalaryToAdd <= salaryCap;
    }

    public int getDistinctNumberOfPositionTypesFilled(List<Player> currentLineup) {
        int numberOfPositionsFilled = 1;
        for (int i = 1; i < currentLineup.size(); i++) {
            if (!currentLineup.get(i - 1).position.equals(currentLineup.get(i).position))
                numberOfPositionsFilled++;
        }
        return numberOfPositionsFilled;
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

    public boolean areNoDuplicates(List<Player> lineup) {
        return lineup.size() == lineup.stream().distinct().count();
    }

    public boolean satisfiesDistinctTeamsRequired(List<Player> lineup, LineupRestrictions lineupRestrictions) {
        return Stream
                .concat(lineup.stream().map(player -> player.team), lineupRestrictions.getWhiteListedTeams().stream())
                .distinct()
                .count() >= lineupRestrictions.getDistinctTeamsRequired();
    }

    public boolean satisfiesMaxPlayersPerTeam(List<Player> lineup, LineupRestrictions lineupRestrictions) {
        Stream<String> teamsInLineup = lineup
                .stream()
                .filter(player -> !player.position.equals(lineupRestrictions.getTeamAgnosticPosition()))
                .map(player -> player.team);
        List<String> teamsList = Stream
                .concat(teamsInLineup, lineupRestrictions.getWhiteListedTeams().stream())
                .collect(Collectors.toList());
        return lineup
                .stream()
                .map(player -> player.team)
                .distinct()
                .allMatch(team -> Collections.frequency(teamsList, team) <= lineupRestrictions.getMaxPlayersPerTeam());
    }

    public boolean lineupIsBetter(List<Player> lineup, int salaryCap, double maxPoints) {
        return totalSalary(lineup) <= salaryCap && totalProjection(lineup) > maxPoints;
    }

    public double totalProjection(List<Player> lineup) {
        return lineup.stream().mapToDouble(player -> player.projection).sum();
    }

    public int totalSalary(List<Player> lineup) {
        return lineup.stream().mapToInt(player -> player.salary).sum();
    }
}
