package com.optimizer.collect;

import api.ApiClient;
import collect.misc.OpponentRanks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OpponentRanksTest implements MockResponses {
    private ApiClient mockApi = mock(ApiClient.class);
    private OpponentRanks opponentRanks = new OpponentRanks(mockApi);

    @BeforeEach
    void setUp() {
        when(mockApi.getOpponentRanksData("nfl")).thenReturn(fakeFantasyProsData);
    }

    @Test
    void shouldGetNFLOpponentRanks() {
        Map<String, Map<String, Integer>> result = opponentRanks.getOpponentRanks("nfl");
        assertEquals(32, result.get("Arizona Cardinals").get("QB"));
        assertEquals(23, result.get("Arizona Cardinals").get("RB"));
        assertEquals(17, result.get("Miami Dolphins").get("TE"));
        assertEquals(30, result.get("New York Giants").get("WR"));
        assertEquals(15, result.get("Detroit Lions").get("D/ST"));
    }
}