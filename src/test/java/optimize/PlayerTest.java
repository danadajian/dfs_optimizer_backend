package optimize;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void shouldConsiderTwoPlayerObjectsWithSamePlayerIdEqual() {
        Player player1 = new Player(69);
        Player player2 = new Player(69);
        assertEquals(player1, player2);
        Player player3 = new Player(69, "RB", "teamDan", 69, 6900);
        assertEquals(player1, player2);
        assertEquals(player1, player3);
    }
}