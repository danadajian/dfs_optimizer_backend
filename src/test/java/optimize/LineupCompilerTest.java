package optimize;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LineupCompilerTest {

    private List<Player> lineup = Arrays.asList(
            new Player(0),
            new Player(4),
            new Player(0),
            new Player(5),
            new Player(0)
    );
    private List<Player> fullLineup = Arrays.asList(
            new Player(1),
            new Player(2),
            new Player(3),
            new Player(4),
            new Player(5)
    );
    private List<Player> optimalPlayers = Arrays.asList(
            new Player(1),
            new Player(2),
            new Player(3)
    );

    private List<Player> playerPool = Arrays.asList(
            new Player(1, "name1", "RB","team1", 0.0, 0),
            new Player(2, "name2", "RB","team2", 0.0, 0),
            new Player(3, "name3", "RB","team3", 0.0, 0),
            new Player(4, "name4", "RB","team4", 0.0, 0),
            new Player(5, "name5", "RB","team5", 0.0, 0)
    );

    LineupCompiler lineupCompiler = new LineupCompiler();

    @Test
    void shouldCombineOptimalPlayerIdsWithWhiteList() {
        List<Integer> result = lineupCompiler.outputLineupPlayerIds(lineup, optimalPlayers);
        assertEquals(Arrays.asList(1, 4, 2, 5, 3), result);
    }

    @Test
    void shouldReturnFullWhiteListedLineupPlayerIds() {
        List<Integer> result = lineupCompiler.outputLineupPlayerIds(fullLineup, new ArrayList<>());
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result);
    }

    @Test
    void shouldReturnFullWhiteListedLineupNames() {
        List<String> result = lineupCompiler.outputLineupPlayerNames(fullLineup, new ArrayList<>(), playerPool);
        assertEquals(Arrays.asList(
                "\nname1 team1 RB",
                "\nname2 team2 RB",
                "\nname3 team3 RB",
                "\nname4 team4 RB",
                "\nname5 team5 RB"
        ), result);
    }
}
