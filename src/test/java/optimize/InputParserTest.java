package optimize;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InputParserTest {

    InputParserTest() throws IOException {
    }

    String fakeOptimizerBody = new String(Files.readAllBytes(Paths.get("src/main/resources/optimizerHandlerBody.json")));
    String fakeOptimizerWhiteAndBlackListBody = new String(Files.readAllBytes(Paths.get("src/main/resources/optimizerHandlerWhiteAndBlackListBody.json")));
    Map<String, Object> optimizerInput = new ObjectMapper().readValue(fakeOptimizerBody, new TypeReference<Map<String, Object>>(){});
    Map<String, Object> optimizerWithWhiteAndBlackListInput = new ObjectMapper().readValue(fakeOptimizerWhiteAndBlackListBody, new TypeReference<Map<String, Object>>(){});

    InputParser inputParser;

    @BeforeEach
    void setUp() {
        inputParser = new InputParser();
    }

    @Test
    void shouldGetLineupFromInput() {
        Player player = new Player();
        List<Player> expectedLineup = Arrays.asList(player, player, player, player, player, player, player, player, player);
        List<Player> result = inputParser.getLineup(optimizerInput);
        assertEquals(expectedLineup, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldGetPlayerPoolFromInput() {
        List<Player> expectedPlayerPool = Arrays.asList(
                new Player(830517),
                new Player(877745),
                new Player(653699)
        );
        List<Player> result = inputParser.getPlayerPool((List<Map<String, Object>>) optimizerInput.get("playerPool"));
        assertEquals(expectedPlayerPool, result);
    }

    @Test
    void shouldGetEmptyBlacklistFromInput() {
        List<Player> expectedBlacklist = Collections.emptyList();
        List<Player> result = inputParser.getBlackList(optimizerInput);
        assertEquals(expectedBlacklist, result);
    }

    @Test
    void shouldGetBlacklistFromInput() {
        List<Player> expectedBlacklist = Collections.singletonList(new Player(868199));
        List<Player> result = inputParser.getBlackList(optimizerWithWhiteAndBlackListInput);
        assertEquals(expectedBlacklist, result);
    }

    @Test
    void shouldGetLineupPositions() {
        List<String> expectedLineupPositions = Arrays.asList(
                "QB",
                "RB",
                "RB",
                "WR",
                "WR",
                "WR",
                "TE",
                "RB,WR,TE",
                "D"
        );
        List<String> result = inputParser.getLineupPositions(optimizerInput);
        assertEquals(expectedLineupPositions, result);
    }

    @Test
    void shouldGetLineupRestrictionsFromInput() {
        LineupRestrictions expectedLineupRestrictions = new LineupRestrictions(optimizerInput);
        LineupRestrictions result = inputParser.getLineupRestrictions(optimizerInput);
        assertEquals(expectedLineupRestrictions, result);
    }
}