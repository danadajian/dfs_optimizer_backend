package com.optimizer.optimize;

import optimize.Optimizer;
import optimize.Player;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OptimizerTestWithDKData {
    private Player qb1 = new Player(822350, "QB", 21.2881974884152, 5700);
    private Player qb2 = new Player(691536, "QB", 22.610172212559654, 6400);
    private Player qb3 = new Player(25347, "QB", 20.806415956007175, 6100);
    private Player qb4 = new Player(216263, "QB", 22.378894124869483, 6600);
    private Player qb5 = new Player(828743, "QB", 21.779414172463024, 6500);
    private Player rb1 = new Player(747861, "RB", 18.52400778119, 5300);
    private Player rb2 = new Player(556294, "RB", 17.80204220100545, 5500);
    private Player rb3 = new Player(750846, "RB", 21.722453017797186, 7000);
    private Player rb4 = new Player(592914, "RB", 16.37929748706943, 5400);
    private Player rb5 = new Player(835814, "RB", 12.983900720727094, 4500);
    private Player wr1 = new Player(821389, "WR", 15.83504234964124, 4900);
    private Player wr2 = new Player(877790, "WR", 22.22516429933301, 7000);
    private Player wr3 = new Player(653699, "WR", 24.66948502316909, 8300);
    private Player wr4 = new Player(556955, "WR", 11.78882814138826, 4000);
    private Player wr5 = new Player(865801, "WR", 15.0689786209298, 5200);
    private Player te1 = new Player(600191, "TE", 17.3673754686699, 5800);
    private Player te2 = new Player(733672, "TE", 17.612800977616033, 5900);
    private Player te3 = new Player(448240, "TE", 17.7702766125721, 6200);
    private Player te4 = new Player(477386, "TE", 12.596249019857991, 4600);
    private Player te5 = new Player(923911, "TE", 9.125309715350358, 3400);
    private Player dst1 = new Player(323, "DST", 10.54, 3000);
    private Player dst2 = new Player(338, "DST", 8.2, 2400);
    private Player dst3 = new Player(327, "DST", 6.6, 2100);
    private Player dst4 = new Player(347, "DST", 11.63, 3800);
    private Player dst5 = new Player(352, "DST", 9.55, 3200);

    private List<Player> playerList = Arrays.asList(
            qb1, qb2, qb3, qb4, qb5, rb1, rb2, rb3, rb4, rb5, wr1, wr2, wr3, wr4, wr5, te1, te2, te3, te4, te5,
            dst1, dst2, dst3, dst4, dst5
    );

    private List<Player> whiteList = new ArrayList<>();

    private List<Player> blackList = new ArrayList<>();

    private List<String> lineupMatrix = Arrays.asList("QB", "RB", "RB", "WR", "WR", "WR", "TE", "RB,WR,TE", "DST");

    private int salaryCap = 50000;

    private Optimizer optimizer = new Optimizer(playerList, whiteList, blackList, lineupMatrix, salaryCap);

    @Test
    void shouldReturnOptimalLineup() {
        List<Player> result = optimizer.optimize();
        List<Player> expected = Arrays.asList(qb1, rb1, rb2, wr1, wr2, wr3, te1, rb5, dst1);
        Set resultSet = new HashSet<>(result);
        Set expectedSet = new HashSet<>(expected);
        System.out.println("Expected projection: " + optimizer.totalProjection(expected));
        System.out.println("Expected salary: " + optimizer.totalSalary(expected));

        System.out.println("Actual projection: " + optimizer.totalProjection(result));
        System.out.println("Actual salary: " + optimizer.totalSalary(result));
        assertEquals(expectedSet, resultSet);
    }
}