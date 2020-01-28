package collect.misc;

import api.ApiClient;
import collect.misc.Injuries;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InjuriesTest {

    private ApiClient mockApi = mock(ApiClient.class);
    private Injuries mlbInjuries = new Injuries(mockApi);
    private Injuries nflInjuries = new Injuries(mockApi);
    private Injuries nbaInjuries = new Injuries(mockApi);
    private Injuries nhlInjuries = new Injuries(mockApi);
    private String fakeMLBInjuriesResponse = new String(Files.readAllBytes(Paths.get("src/main/resources/mlbInjuryResponse.txt")));
    private String fakeNFLInjuriesResponse = new String(Files.readAllBytes(Paths.get("src/main/resources/nflInjuryResponse.txt")));
    private String fakeNBAInjuriesResponse = new String(Files.readAllBytes(Paths.get("src/main/resources/nbaInjuryResponse.txt")));
    private String fakeNHLInjuriesResponse = new String(Files.readAllBytes(Paths.get("src/main/resources/nhlInjuryResponse.txt")));

    InjuriesTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        when(mockApi.getInjuryData("mlb")).thenReturn(fakeMLBInjuriesResponse);
        when(mockApi.getInjuryData("nfl")).thenReturn(fakeNFLInjuriesResponse);
        when(mockApi.getInjuryData("nba")).thenReturn(fakeNBAInjuriesResponse);
        when(mockApi.getInjuryData("nhl")).thenReturn(fakeNHLInjuriesResponse);
    }

    @Test
    void shouldGetMLBInjuryData() {
        Map<String, String> result = mlbInjuries.getStandardInjuryData("mlb");
        assertEquals("Day-to-Day", result.get("Pedro Avila"));
        assertEquals("Suspension", result.get("Tim Beckham"));
    }

    @Test
    void shouldGetNFLInjuryData() {
        Map<String, String> result = nflInjuries.getNFLInjuryData();
        assertEquals("Questionable", result.get("Evan Engram"));
        assertEquals("Out", result.get("Hunter Renfrow"));
    }

    @Test
    void shouldGetNBAInjuryData() {
        Map<String, String> result = nbaInjuries.getStandardInjuryData("nba");
        assertEquals("Day-To-Day", result.get("De'Andre Hunter"));
        assertEquals("Out", result.get("Kyrie Irving"));
        assertEquals("Out", result.get("Chandler Hutchison"));
    }

    @Test
    void shouldGetNHLInjuryData() {
        Map<String, String> result = nhlInjuries.getStandardInjuryData("nhl");
        assertEquals("Day-To-Day", result.get("Carl Soderberg"));
        assertEquals("IR", result.get("Rasmus Dahlin"));
    }
}