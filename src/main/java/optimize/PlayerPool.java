package optimize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerPool {
    private List<Player> playerPool;

    @SuppressWarnings("unchecked")
    public PlayerPool(Map<String, Object> input) {
        this.playerPool = new ArrayList<>();
        ((List<Map<String, Object>>) input.get("playerPool")).forEach(playerMap ->
                playerPool.add(
                        new Player((int) playerMap.get("playerId"), (String) playerMap.get("name"),
                                (String) playerMap.get("position"), (String) playerMap.get("team"),
                                ((Number) playerMap.get("projection")).doubleValue(), (int) playerMap.get("salary"))));
    }

    public PlayerPool(List<Map<String, Object>> playerPoolArray) {
        this.playerPool = new ArrayList<>();
        playerPoolArray.forEach(playerMap ->
                playerPool.add(
                        new Player((int) playerMap.get("playerId"), (String) playerMap.get("name"),
                                (String) playerMap.get("position"), (String) playerMap.get("team"),
                                ((Number) playerMap.get("projection")).doubleValue(), (int) playerMap.get("salary"))));
    }

    public List<Player> getPlayerPool() {
        return playerPool;
    }
}
