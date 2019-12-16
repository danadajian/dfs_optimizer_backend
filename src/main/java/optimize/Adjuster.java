package optimize;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Adjuster {
    private List<Player> lineup;
    private List<Player> playerList;
    private List<Player> whiteList;
    private List<Player> blackList;
    private List<String> startingPositions;
    private int startingSalaryCap;

    public Adjuster(List<Player> lineup, List<Player> playerList, List<Player> blackList,
                    List<String> startingPositions, int startingSalaryCap) {
        this.lineup = lineup;
        this.playerList = playerList;
        this.whiteList = lineup.stream().filter(player -> player.playerId > 0).collect(Collectors.toList());
        this.blackList = blackList;
        this.startingPositions = startingPositions;
        this.startingSalaryCap = startingSalaryCap;
    }

    public List<Player> adjustedPlayerList() {
        return playerList
                .stream()
                .filter(player -> player.projection > 0 && !whiteList.contains(player) && !blackList.contains(player))
                .collect(Collectors.toList());
    }

    public List<String> adjustedLineupPositions() {
        List<String> newLineupMatrix = new ArrayList<>();
        for (int i = 0; i < startingPositions.size(); i++) {
            if (lineup.get(i).playerId == 0) {
                newLineupMatrix.add(startingPositions.get(i));
            }
        }
        return newLineupMatrix;
    }

    public int adjustedSalaryCap() {
        return startingSalaryCap - whiteList.stream().mapToInt(player -> player.salary).sum();
    }
}
