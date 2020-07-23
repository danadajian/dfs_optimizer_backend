package optimize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InputParser {
    @SuppressWarnings("unchecked")
    public List<Player> getLineup(Map<String, Object> input) {
        List<Player> lineup = new ArrayList<>();
        ((List<Map<String, Object>>) input.get("lineup")).forEach(playerMap -> {
            int playerId = (int) playerMap.get("playerId");
            lineup.add(playerId == 0 ? new Player() :
                    new Player(playerId, (String) playerMap.get("position"), (String) playerMap.get("team"),
                            ((Number) playerMap.get("projection")).doubleValue(), (int) playerMap.get("salary")));
        });
        return lineup;
    }

    public List<Player> getPlayerPool(List<Map<String, Object>> playerPoolArray) {
        List<Player> playerPool = new ArrayList<>();
        playerPoolArray.forEach(playerMap ->
                playerPool.add(
                        new Player((int) playerMap.get("playerId"), (String) playerMap.get("name"),
                                (String) playerMap.get("position"), (String) playerMap.get("team"),
                                ((Number) playerMap.get("projection")).doubleValue(), (int) playerMap.get("salary"))));
        return playerPool;
    }

    @SuppressWarnings("unchecked")
    public List<Player> getBlackList(Map<String, Object> input) {
        List<Player> blackList = new ArrayList<>();
        ((List<Integer>) input.get("blackList")).forEach(playerId -> blackList.add(new Player(playerId)));
        return blackList;
    }

    @SuppressWarnings("unchecked")
    public List<String> getLineupPositions(Map<String, Object> input) {
        return (List<String>) input.get("lineupPositions");
    }

    public LineupRestrictions getLineupRestrictions(Map<String, Object> input) {
        return new LineupRestrictions(input);
    }
}
