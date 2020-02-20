package optimize;

import org.junit.jupiter.api.Test;

import java.util.*;

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
            new Player(1, "name1", "RB","team1", 0.0, 10),
            new Player(2, "name2", "RB","team2", 0.0, 20),
            new Player(3, "name3", "RB","team3", 0.0, 50),
            new Player(4, "name4", "RB","team4", 0.0, 30),
            new Player(5, "name5", "RB","team5", 0.0, 40)
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
    void shouldReturnPlayersWithNames() {
        List<Player> result = lineupCompiler.outputPlayersWithNames(fullLineup, new ArrayList<>(), playerPool);
        assertEquals(Arrays.asList(
                new Player(1, "name1", "RB","team1", 1.0, 10),
                new Player(2, "name2", "RB","team2", 2.0, 20),
                new Player(3, "name3", "RB","team3", 3.0, 50),
                new Player(4, "name4", "RB","team4", 4.0, 30),
                new Player(5, "name5", "RB","team5", 5.0, 40)
        ), result);
    }

    @Test
    void shouldGenerateCorrectFileOutput() {
        List<Player> playersWithNames = Arrays.asList(
                new Player(1, "name1", "RB","team1", 1.0, 10),
                new Player(2, "name2", "RB","team2", 2.0, 20),
                new Player(3, "name3", "RB","team3", 3.0, 50)
        );
        Map<String, String> playerMap1 = new HashMap<>();
        playerMap1.put("name", "name1");
        playerMap1.put("team", "team1");
        playerMap1.put("position", "RB");
        Map<String, String> playerMap2 = new HashMap<>();
        playerMap2.put("name", "name2");
        playerMap2.put("team", "team2");
        playerMap2.put("position", "RB");
        Map<String, String> playerMap3 = new HashMap<>();
        playerMap3.put("name", "name3");
        playerMap3.put("team", "team3");
        playerMap3.put("position", "RB");
        List<Map<String, String>> expectedListOfPlayerMaps = Arrays.asList(playerMap1, playerMap2, playerMap3);
        Map<String, Object> result = lineupCompiler.generateFileOutput(playersWithNames);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("lineup", expectedListOfPlayerMaps);
        resultMap.put("totalProjection", 6.0);
        resultMap.put("totalSalary", 80);
        assertEquals(resultMap, result);
    }
}
