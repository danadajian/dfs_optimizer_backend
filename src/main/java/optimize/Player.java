package optimize;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Player {
    public int playerId, salary;
    public String position;
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

    public Player(int playerId, String position, double projection, int salary) {
        this.playerId = playerId;
        this.position = position;
        this.projection = projection;
        this.salary = salary;
    }

    public Player(String playerString) {
        Map<String, Object> map = Arrays.stream(playerString.replace("{", "")
                .replace("}", "").replace(" ", "").split(","))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(s -> s[0], s -> s[1]));
        this.playerId = Integer.parseInt((String) map.get("playerId"));
        this.position = (String) map.get("position");
        this.projection = Double.parseDouble((String) map.get("projection"));
        this.salary = Integer.parseInt((String) map.get("salary"));
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
        playerMap.put("projection", projection);
        playerMap.put("salary", salary);
        return playerMap.toString();
    }
}
