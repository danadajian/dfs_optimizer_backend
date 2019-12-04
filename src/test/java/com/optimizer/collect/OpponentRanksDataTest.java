package com.optimizer.collect;

import api.ApiClient;
import collect.OpponentRanksData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OpponentRanksDataTest implements MockResponses {
    private ApiClient mockApi = mock(ApiClient.class);
    private OpponentRanksData opponentRanksData = new OpponentRanksData(mockApi);

    @BeforeEach
    void setUp() {
        when(mockApi.getOpponentRanksData()).thenReturn(fakeFantasyProsData);
    }

    @Test
    void shouldGetNFLOpponentRanks() {
        Map<String, Map<String, Integer>> result = opponentRanksData.getNFLOpponentRanks();
        assertEquals(32, result.get("Arizona Cardinals").get("QB"));
        assertEquals(23, result.get("Arizona Cardinals").get("RB"));
        assertEquals(17, result.get("Miami Dolphins").get("TE"));
        assertEquals(30, result.get("New York Giants").get("WR"));
        assertEquals(15, result.get("Detroit Lions").get("D/ST"));
    }
}