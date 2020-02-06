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

    public List<String> outputLineupPlayerNames(List<Player> lineup, List<Player> optimalPlayers, List<Player> playerPool) {
        List<Player> lineupWithoutNames = new ArrayList<>(lineup);
        for (Player player : optimalPlayers) {
            int emptySpotIndex = IntStream.range(0, lineupWithoutNames.size())
                    .filter(index -> lineupWithoutNames.get(index).playerId == 0)
                    .findFirst()
                    .orElse(-1);
            lineupWithoutNames.set(emptySpotIndex, player);
        }
        List<Player> lineupWithNames = new ArrayList<>();
        for (Player player : lineupWithoutNames) {
            Player playerInPlayerPool = playerPool
                    .stream()
                    .filter(playerInPool -> playerInPool.equals(player))
                    .findFirst()
                    .orElse(new Player());
            lineupWithNames.add(playerInPlayerPool);
        }
        return lineupWithNames.stream()
                .map(player -> "\n" + player.name + " " + player.team + " " + player.position)
                .collect(Collectors.toList());
    }
}
