package optimize;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Adjuster {

    public List<Player> getWhiteList(List<Player> lineup) {
        return lineup.stream().filter(player -> player.playerId > 0).collect(Collectors.toList());
    }

    public List<Player> adjustPlayerList(List<Player> playerList, List<Player> whiteList, List<Player> blackList) {
        return playerList
                .stream()
                .filter(player -> player.projection > 0 && !whiteList.contains(player) && !blackList.contains(player))
                .collect(Collectors.toList());
    }

    public List<String> adjustLineupPositions(List<Player> lineup, List<String> startingPositions) {
        List<String> newLineupMatrix = new ArrayList<>();
        for (int i = 0; i < startingPositions.size(); i++) {
            if (lineup.get(i).playerId == 0) {
                newLineupMatrix.add(startingPositions.get(i));
            }
        }
        return newLineupMatrix;
    }

    public int adjustSalaryCap(List<Player> whiteList, int startingSalaryCap) {
        return startingSalaryCap - whiteList.stream().mapToInt(player -> player.salary).sum();
    }
}
