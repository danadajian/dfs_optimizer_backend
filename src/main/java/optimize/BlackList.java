package optimize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BlackList {
    private List<Player> blackList;

    @SuppressWarnings("unchecked")
    public BlackList(Map<String, Object> input) {
        this.blackList = new ArrayList<>();
        ((List<Integer>) input.get("blackList")).forEach(playerId -> blackList.add(new Player(playerId)));
    }

    public List<Player> getBlackList() {
        return blackList;
    }

}
