package com.optimizer.optimize;

import optimize.ParameterAdjuster;
import optimize.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

public class ParameterAdjusterTest {
    private Player qb1 = new Player(1, "QB", 6.9, 6900);
    private Player qb2 = new Player(2, "QB", 17.0, 6100);
    private Player rb1 = new Player(3, "RB", 15.0, 5000);
    private Player rb2 = new Player(4, "RB", 18.9, 8000);
    private Player rb3 = new Player(5, "RB", 14.0, 7500);
    private Player rb4 = new Player(6, "RB", 12.7, 7200);
    private Player rb5 = new Player(7, "RB", 7.7, 5600);
    private Player rb6 = new Player(8, "RB", 9.1, 4900);
    private Player wr1 = new Player(9, "WR", 22.9, 10000);
    private Player wr2 = new Player(10, "WR", 11.6, 7900);
    private Player wr3 = new Player(11, "WR", 5.5, 8400);
    private Player wr4 = new Player(12, "WR", 7.9, 3700);
    private Player te1 = new Player(13, "TE", 10.1, 6700);
    private Player te2 = new Player(14, "TE", 8.1, 5200);
    private Player dst1 = new Player(15, "D", 6.0, 4600);
    private Player dst2 = new Player(16, "D", 9.0, 5300);
    private Player zeroPlayer = new Player(17, "TE", 0.0, 5200);
    private Player emptyPlayer = new Player(0);

    private List<Player> lineup = Arrays.asList(
            emptyPlayer,
            rb2,
            emptyPlayer,
            rb3,
            emptyPlayer
    );

    private List<Player> playerList = Arrays.asList(
            qb1, qb2, rb1, rb2, rb3, rb4, rb5, rb6, wr1, wr2, wr3, wr4, te1, te2, dst1, dst2, zeroPlayer
    );

    private List<Player> blackList = Arrays.asList(rb4, rb5);

    private List<String> startingPositions = Arrays.asList("QB", "RB", "WR", "TE", "D");

    ParameterAdjuster parameterAdjuster = new ParameterAdjuster(lineup, playerList, blackList, startingPositions, 60000);

    @Test
    void shouldAdjustPlayerList() {
        List<Player> result = parameterAdjuster.adjustPlayerList();
        assertEquals(Arrays.asList(qb1, qb2, rb1, rb6, wr1, wr2, wr3, wr4, te1, te2, dst1, dst2), result);
    }

    @Test
    void shouldAdjustLineupPositions() {
        List<String> result = parameterAdjuster.adjustLineupPositions();
        assertEquals(Arrays.asList("QB", "WR", "D"), result);
    }

    @Test
    void shouldAdjustSalaryCap() {
        int result = parameterAdjuster.adjustSalaryCap();
        assertEquals(60000 - rb2.salary - rb3.salary, result);

    }
}
