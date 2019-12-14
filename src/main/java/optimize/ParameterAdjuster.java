package optimize;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParameterAdjuster {
    List<Player> lineup;
    List<Player> playerList;
    List<Player> whiteList;
    List<Player> blackList;
    List<String> startingPositions;
    int startingSalaryCap;

    public ParameterAdjuster(List<Player> lineup, List<Player> playerList, List<Player> blackList,
                             List<String> startingPositions, int startingSalaryCap) {
        this.lineup = lineup;
        this.playerList = playerList;
        this.whiteList = lineup.stream().filter(player -> player.playerId > 0).collect(Collectors.toList());
        this.blackList = blackList;
        this.startingPositions = startingPositions;
        this.startingSalaryCap = startingSalaryCap;
    }

    public List<Player> adjustPlayerList() {
        return playerList
                .stream()
                .filter(player -> player.projection > 0 && !whiteList.contains(player) && !blackList.contains(player))
                .collect(Collectors.toList());
    }

    public List<String> adjustLineupPositions() {
        List<String> newLineupMatrix = new ArrayList<>();
        for (int i = 0; i < startingPositions.size(); i++) {
            if (lineup.get(i).playerId == 0) {
                newLineupMatrix.add(startingPositions.get(i));
            }
        }
        return newLineupMatrix;
    }

    public int adjustSalaryCap() {
        return startingSalaryCap - whiteList.stream().mapToInt(player -> player.salary).sum();
    }

}
