package optimize;

import java.util.HashMap;
import java.util.Map;

public class Player {
    public int playerId, salary;
    public String position, team;
    public double projection;

    public Player() {
    }

    public Player(int playerId) {
        this.playerId = playerId;
    }

    public Player(int playerId, String position) {
        this.playerId = playerId;
        this.position = position;
    }

    public Player(int playerId, String position, String team, double projection, int salary) {
        this.playerId = playerId;
        this.position = position;
        this.team = team;
        this.projection = projection;
        this.salary = salary;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Player && this.playerId == ((Player) object).playerId;
    }

    @Override
    public String toString() {
        Map<String, Object> playerMap = new HashMap<>();
        playerMap.put("playerId", playerId);
        playerMap.put("position", position);
        playerMap.put("team", team);
        playerMap.put("projection", projection);
        playerMap.put("salary", salary);
        return playerMap.toString();
    }
}
