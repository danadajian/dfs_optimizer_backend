package handler

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class OpponentRanksHandlerTest {
    private val sport = "a sport"
    private val opponentRanksHandler = spyk<OpponentRanksHandler>()
    private val opponentRanksResult: Map<String, Map<String, Int>> = mapOf()

    @BeforeEach
    fun setUp() {
        every { opponentRanksHandler.getOpponentRanksData(sport) } returns opponentRanksResult
    }

    @Test
    fun `should handle request`() {
        val result = opponentRanksHandler.handleRequest(mapOf("sport" to sport))
        assertEquals(opponentRanksResult, result)
    }
}