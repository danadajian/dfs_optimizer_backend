package optimize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Lineup {
    private List<Player> lineup;

    @SuppressWarnings("unchecked")
    public Lineup(Map<String, Object> input) {
        this.lineup = new ArrayList<>();
        ((List<Map<String, Object>>) input.get("lineup")).forEach(playerMap -> {
            int playerId = (int) playerMap.get("playerId");
            lineup.add(playerId == 0 ? new Player() :
                    new Player(playerId, (String) playerMap.get("position"), (String) playerMap.get("team"),
                            ((Number) playerMap.get("projection")).doubleValue(), (int) playerMap.get("salary")));
        });
    }

    public List<Player> getLineup() {
        return lineup;
    }
}
