package optimize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Optimizer {
    private List<Object> playerList;
    private List<Object> whiteList;
    private List<Object> blackList;

    public Optimizer(Map<String, List<Object>> input) {
        this.playerList = input.get("players");
        this.whiteList = input.get("whiteList");
        this.blackList = input.get("blackList");
    }

    public Map<Integer, String> optimize() {
        return new HashMap<>();
    }
}
