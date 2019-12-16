package com.optimizer.collect;

import api.ApiClient;
import collect.InjuryData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InjuryDataTest {

    private ApiClient mockApi = mock(ApiClient.class);
    private InjuryData mlbInjuryData = new InjuryData(mockApi);
    private InjuryData nflInjuryData = new InjuryData(mockApi);
    private InjuryData nbaInjuryData = new InjuryData(mockApi);
    private InjuryData nhlInjuryData = new InjuryData(mockApi);
    private String fakeMLBInjuriesResponse = new String(Files.readAllBytes(Paths.get("src/main/resources/mlbInjuryResponse.txt")));
    private String fakeNFLInjuriesResponse = new String(Files.readAllBytes(Paths.get("src/main/resources/nflInjuryResponse.txt")));
    private String fakeNBAInjuriesResponse = new String(Files.readAllBytes(Paths.get("src/main/resources/nbaInjuryResponse.txt")));
    private String fakeNHLInjuriesResponse = new String(Files.readAllBytes(Paths.get("src/main/resources/nhlInjuryResponse.txt")));

    InjuryDataTest() throws IOException {
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
        Map<String, String> result = mlbInjuryData.getStandardInjuryData("mlb");
        assertEquals("Day-to-Day", result.get("Pedro Avila"));
        assertEquals("Suspension", result.get("Tim Beckham"));
    }

    @Test
    void shouldGetNFLInjuryData() {
        Map<String, String> result = nflInjuryData.getNFLInjuryData();
        assertEquals("Questionable", result.get("Evan Engram"));
        assertEquals("Out", result.get("Hunter Renfrow"));
    }

    @Test
    void shouldGetNBAInjuryData() {
        Map<String, String> result = nbaInjuryData.getStandardInjuryData("nba");
        assertEquals("Day-To-Day", result.get("De'Andre Hunter"));
        assertEquals("Out", result.get("Kyrie Irving"));
        assertEquals("Out", result.get("Chandler Hutchison"));
    }

    @Test
    void shouldGetNHLInjuryData() {
        Map<String, String> result = nhlInjuryData.getStandardInjuryData("nhl");
        assertEquals("Day-To-Day", result.get("Carl Soderberg"));
        assertEquals("IR", result.get("Rasmus Dahlin"));
    }
}