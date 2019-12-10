package com.optimizer.optimize;

import optimize.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void shouldConsiderTwoPlayerObjectsWithSamePlayerIdEqual() {
        Player player1 = new Player(69);
        Player player2 = new Player(69);
        assertEquals(player1, player2);
        Player player3 = new Player(69, "RB", 69, 6900);
        assertEquals(player1, player2);
        assertEquals(player1, player3);
    }

    @Test
    void shouldConstructPlayerFromMapString() {
        String playerString = "{position=RB, projection=24.45617861968624, salary=11000, playerId=830517}";
        Player testPlayer = new Player(playerString);
        assertEquals(830517, testPlayer.playerId);
        assertEquals("RB", testPlayer.position);
        assertEquals(24.45617861968624, testPlayer.projection);
        assertEquals(11000, testPlayer.salary);
    }
}