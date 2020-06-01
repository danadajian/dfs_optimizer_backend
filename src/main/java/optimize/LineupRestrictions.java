package optimize;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LineupRestrictions {
    int distinctTeamsRequired;
    int maxPlayersPerTeam;
    String teamAgnosticPosition;
    List<String> whiteListedTeams;

    @SuppressWarnings("unchecked")
    public LineupRestrictions(Map<String, Object> input) {
        Map<String, Object> lineupRestrictionsMap = (Map<String, Object>) input.get("lineupRestrictions");
        this.distinctTeamsRequired = (int) lineupRestrictionsMap.get("distinctTeamsRequired");
        this.maxPlayersPerTeam = (int) lineupRestrictionsMap.get("maxPlayersPerTeam");
        this.teamAgnosticPosition = (String) lineupRestrictionsMap.get("teamAgnosticPosition");
        this.whiteListedTeams = Collections.emptyList();
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

    public List<String> getWhiteListedTeams() {
        return whiteListedTeams;
    }

    public void setWhiteListedTeams(List<String> whiteListedTeams) {
        this.whiteListedTeams = whiteListedTeams;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof LineupRestrictions) {
            return ((LineupRestrictions) object).getDistinctTeamsRequired() == this.distinctTeamsRequired &&
                    ((LineupRestrictions) object).getMaxPlayersPerTeam() == this.maxPlayersPerTeam &&
                    ((LineupRestrictions) object).getTeamAgnosticPosition().equals(this.teamAgnosticPosition) &&
                    ((LineupRestrictions) object).getWhiteListedTeams() == this.whiteListedTeams;
        } else
            return false;
    }
}
