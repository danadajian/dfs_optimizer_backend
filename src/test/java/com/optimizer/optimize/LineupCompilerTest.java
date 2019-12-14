package com.optimizer.optimize;

import optimize.LineupCompiler;
import optimize.Player;
import org.junit.jupiter.api.Test;

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
    private List<Player> optimalPlayers = Arrays.asList(
            new Player(1),
            new Player(2),
            new Player(3)
    );

    LineupCompiler lineupCompiler = new LineupCompiler(lineup, optimalPlayers);

    @Test
    void shouldCombineOptimalPlayersWithWhiteList() {
        List<Integer> result = lineupCompiler.outputLineup();
        assertEquals(Arrays.asList(1, 4, 2, 5, 3), result);
    }
}
