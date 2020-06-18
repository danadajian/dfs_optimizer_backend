package optimize;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LineupValidator {

    public boolean lineupContainsNoDuplicates(List<Player> lineup) {
        return lineup.size() == lineup.stream().distinct().count();
    }

    public boolean lineupSatisfiesMaxPlayersPerTeam(List<Player> lineup, LineupRestrictions lineupRestrictions) {
        Stream<String> teamsInLineup = lineup
                .stream()
                .filter(player -> !player.position.equals(lineupRestrictions.getTeamAgnosticPosition()))
                .map(player -> player.team);
        List<String> teamsList = Stream
                .concat(teamsInLineup, lineupRestrictions.getWhiteListedTeams().stream())
                .collect(Collectors.toList());
        return lineup
                .stream()
                .map(player -> player.team)
                .distinct()
                .allMatch(team -> Collections.frequency(teamsList, team) <= lineupRestrictions.getMaxPlayersPerTeam());
    }

    public boolean lineupSatisfiesDistinctTeamsRequired(List<Player> lineup, LineupRestrictions lineupRestrictions) {
        return Stream
                .concat(lineup.stream().map(player -> player.team), lineupRestrictions.getWhiteListedTeams().stream())
                .distinct()
                .count() >= lineupRestrictions.getDistinctTeamsRequired();
    }
}
