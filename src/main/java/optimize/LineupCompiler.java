package optimize;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LineupCompiler {

    private final List<Player> lineup;
    private final List<Player> optimalPlayers;

    public LineupCompiler(List<Player> lineup, List<Player> optimalPlayers) {
        this.lineup = lineup;
        this.optimalPlayers = optimalPlayers;
    }

    public List<Integer> outputLineup() {
        for (Player player : optimalPlayers) {
            int emptySpotIndex = IntStream.range(0, lineup.size())
                    .filter(index -> lineup.get(index).playerId == 0)
                    .findFirst()
                    .orElse(-1);
            lineup.set(emptySpotIndex, player);
        }
        return lineup.stream().map(player -> player.playerId).collect(Collectors.toList());
    }
}
