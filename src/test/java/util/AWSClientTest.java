package util;

import optimize.Player;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AWSClientTest {

    AWSClient awsClient = new AWSClient();

    private List<Player> playersWithNames = Arrays.asList(
            new Player(1, "name1", "RB","team1", 1.0, 10),
            new Player(2, "name2", "RB","team2", 2.0, 20),
            new Player(3, "name3", "RB","team3", 3.0, 50),
            new Player(4, "name4", "RB","team4", 4.0, 30),
            new Player(5, "name5", "RB","team5", 5.0, 40)
    );

    @Test
    void shouldReturnCorrectOutputForText() {
        String result = awsClient.convertToTextOutput(playersWithNames);
        assertEquals("\nname1 team1 RB, " +
                "\nname2 team2 RB, " +
                "\nname3 team3 RB, " +
                "\nname4 team4 RB, " +
                "\nname5 team5 RB, " +
                "\nTotal projected points: 15.0, " +
                "\nTotal salary: $150"
        , result);
    }
}