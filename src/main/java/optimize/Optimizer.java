package optimize;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Optimizer {
    private List<Player> playerList, whiteList, blackList;
    private List<String> lineupMatrix;
    private List<List<Player>> playerPools;
    private int salaryCap;
    private double maxPoints;
    private List<Player> optimalLineup;

    public Optimizer(List<Player> playerList, List<Player> whiteList, List<Player> blackList,
                     List<String> lineupMatrix, int salaryCap) {
        this.playerList = playerList;
        this.whiteList = whiteList;
        this.blackList = blackList;
        this.lineupMatrix = lineupMatrix;
        this.salaryCap = salaryCap;
        this.maxPoints = 0;
        this.optimalLineup = new ArrayList<>();
    }

    public List<Player> optimize() {
        List<Player> lineupWithWhiteList = lineupWithWhiteList();
        removeWhiteListedPositionsFromLineupMatrix(lineupWithWhiteList);
        playerPools = getCheapestPlayersPerProjectionByPositionWithoutWhiteOrBlackList();
        int size = lineupMatrix.size();
        List<Integer> intList = IntStream.rangeClosed(0, 4).boxed().collect(Collectors.toList());
        for (int i = 0; i < 5; i++) {
            permuteLineups(intList, size, Collections.singletonList(i));
        }
        System.out.println(totalProjection(optimalLineup));
        System.out.println(totalSalary(optimalLineup));
        List<Player> optimalLineupWithWhiteList = new ArrayList<>(lineupWithWhiteList);
        for (Player player : optimalLineup) {
            int emptySpotIndex = IntStream.range(0, optimalLineupWithWhiteList.size())
                    .filter(index -> optimalLineupWithWhiteList.get(index).playerId == 0)
                    .findFirst()
                    .orElse(-1);
            optimalLineupWithWhiteList.set(emptySpotIndex, player);
        }
        return optimalLineupWithWhiteList;
    }

    public void removeWhiteListedPositionsFromLineupMatrix(List<Player> lineupWithWhiteList) {
        List<String> positionsToRemove = new ArrayList<>();
        for (Player player : lineupWithWhiteList) {
            int lineupIndex = IntStream.range(0, lineupMatrix.size())
                    .filter(i -> lineupMatrix.get(i).equals("any") ||
                            Arrays.asList(lineupMatrix.get(i).split(",")).contains(player.position))
                    .findFirst()
                    .orElse(-1);
            if (lineupIndex > -1)
                positionsToRemove.add(lineupMatrix.get(lineupIndex));
        }
        lineupMatrix.removeAll(positionsToRemove);
    }

    public void permuteLineups(List<Integer> intList, int n, List<Integer> start){
        if(start.size() >= n){
            List<Player> lineup = getLineupFromIntList(start, playerPools);
            if (isValidLineup(lineup)) {
                double totalPoints = totalProjection(lineup);
                int totalSalary = totalSalary(lineup);
                if (totalSalary <= salaryCap && totalPoints > maxPoints) {
                    maxPoints = totalPoints;
                    optimalLineup = lineup;
                }
            }
        } else {
            for (int x : intList) {
                List<Integer> listCopy = new ArrayList<>(start);
                listCopy.add(x);
                permuteLineups(intList, n, listCopy);
            }
        }
    }

    public boolean isValidLineup(List<Player> lineup) {
        List<Boolean> validLineupRequirements = new ArrayList<>();
        int lineupSize = lineupMatrix.size();
        for (int i = 0; i < lineupSize - 1; i++) {
            for (int j = i + 1; j < lineupSize; j++) {
                if (lineupMatrix.get(j).contains(lineupMatrix.get(i))) {
                    validLineupRequirements.add(!lineup.get(i).equals(lineup.get(j)));
                }
            }
        }
        for (boolean statement : validLineupRequirements) {
            if (!statement)
                return false;
        }
        return true;
    }

    public List<Player> getLineupFromIntList(List<Integer> intList, List<List<Player>> playerPools) {
        List<Player> lineup = new ArrayList<>();
        for (int i = 0; i < intList.size(); i++) {
            lineup.add(playerPools.get(i).get(intList.get(i)));
        }
        return lineup;
    }

    public List<Player> lineupWithWhiteList() {
        List<Player> lineupWithWhiteListedPlayers = new ArrayList<>();
        lineupMatrix.forEach(position -> lineupWithWhiteListedPlayers.add(new Player()));
        for (Player player : whiteList) {
            Player playerInPool = playerList.stream()
                    .filter(playerPoolPlayer -> playerPoolPlayer.equals(player))
                    .findFirst()
                    .orElse(new Player());
            int lineupIndex = IntStream.range(0, lineupMatrix.size())
                    .filter(i -> lineupMatrix.get(i).equals("any") ||
                            Arrays.asList(lineupMatrix.get(i).split(",")).contains(playerInPool.position))
                    .findFirst()
                    .orElse(-1);
            if (lineupIndex > -1) {
                lineupWithWhiteListedPlayers.set(lineupIndex, playerInPool);
                salaryCap -= playerInPool.salary;
            }
        }
        return lineupWithWhiteListedPlayers;
    }

    public double totalProjection(List<Player> lineup) {
        return lineup.stream().mapToDouble(player -> player.projection).sum();
    }

    public int totalSalary(List<Player> lineup) {
        return lineup.stream().mapToInt(player -> player.salary).sum();
    }

    public List<List<Player>> getCheapestPlayersPerProjectionByPositionWithoutWhiteOrBlackList() {
        List<List<Player>> playerPools = new ArrayList<>();
        for (String position : lineupMatrix) {
            List<Player> playerPool = playerList.stream()
                    .filter(
                            player -> (position.equals("any") ||
                                    Arrays.asList(position.split(",")).contains(player.position))
                                    && !whiteList.contains(player)
                                    && !blackList.contains(player)
                    )
                    .sorted(Comparator.comparingDouble(player -> player.salary / player.projection))
                    .collect(Collectors.toList())
                    .subList(0, 5);
            playerPools.add(playerPool);
        }
        return playerPools;
    }
}
