package optimize;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LineupCompiler {

    public List<Integer> outputLineupPlayerIds(List<Player> lineup, List<Player> optimalPlayers) {
        List<Player> lineupToOutput = new ArrayList<>(lineup);
        for (Player player : optimalPlayers) {
            int emptySpotIndex = IntStream.range(0, lineupToOutput.size())
                    .filter(index -> lineupToOutput.get(index).playerId == 0)
                    .findFirst()
                    .orElse(-1);
            lineupToOutput.set(emptySpotIndex, player);
        }
        return lineupToOutput.stream().map(player -> player.playerId).collect(Collectors.toList());
    }

    public List<Player> outputPlayersWithNames(List<Player> lineup, List<Player> optimalPlayers, List<Player> playerPool) {
        List<Player> playersWithoutNames = new ArrayList<>(lineup);
        for (Player player : optimalPlayers) {
            int emptySpotIndex = IntStream.range(0, playersWithoutNames.size())
                    .filter(index -> playersWithoutNames.get(index).playerId == 0)
                    .findFirst()
                    .orElse(-1);
            playersWithoutNames.set(emptySpotIndex, player);
        }
        List<Player> playersWithNames = new ArrayList<>();
        for (Player player : playersWithoutNames) {
            Player playerInPlayerPool = playerPool
                    .stream()
                    .filter(playerInPool -> playerInPool.equals(player))
                    .findFirst()
                    .orElse(new Player());
            playersWithNames.add(playerInPlayerPool);
        }
        return playersWithNames;
    }

    public List<String> outputPlayerNamesOnly(List<Player> players) {
        return players.stream()
                .map(player -> player.name)
                .collect(Collectors.toList());
    }
}
