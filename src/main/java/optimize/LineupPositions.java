package optimize;

import java.util.List;
import java.util.Map;

public class LineupPositions {
    private List<String> lineupPositions;

    @SuppressWarnings("unchecked")
    public LineupPositions(Map<String, Object> input) {
        this.lineupPositions = (List<String>) input.get("lineupPositions");
    }

    public List<String> getLineupPositions() {
        return lineupPositions;
    }

}
