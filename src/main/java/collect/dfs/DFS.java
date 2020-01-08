package collect.dfs;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public abstract class DFS {
    public abstract List<Map<String, Object>> getAllContestData(String input);
    public abstract List<JSONObject> getValidContests(String input);
}
