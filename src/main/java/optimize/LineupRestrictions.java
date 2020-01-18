package optimize;

import java.util.Map;

public class LineupRestrictions {
    private int distinctTeamsRequired;
    private int maxPlayersPerTeam;
    private String teamAgnosticPosition;

    @SuppressWarnings("unchecked")
    public LineupRestrictions(Map<String, Object> input) {
        Map<String, Object> lineupRestrictionsMap = (Map<String, Object>) input.get("lineupRestrictions");
        this.distinctTeamsRequired = (int) lineupRestrictionsMap.get("distinctTeamsRequired");
        this.maxPlayersPerTeam = (int) lineupRestrictionsMap.get("maxPlayersPerTeam");
        this.teamAgnosticPosition = (String) lineupRestrictionsMap.get("teamAgnosticPosition");
    }

    public int getDistinctTeamsRequired() {
        return distinctTeamsRequired;
    }

    public int getMaxPlayersPerTeam() {
        return maxPlayersPerTeam;
    }

    public String getTeamAgnosticPosition() {
        return teamAgnosticPosition;
    }
}
