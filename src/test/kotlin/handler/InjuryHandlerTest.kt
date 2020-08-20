package handler

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class InjuryHandlerTest {
    private val injuryHandler = spyk<InjuryHandler>()
    private val sport = "not nfl"
    private val nflResult: Map<String, String> = mapOf("nfl" to "result")
    private val standardResult: Map<String, String> = mapOf("standard" to "result")

    @BeforeEach
    fun setUp() {
        every { injuryHandler.getNFLInjuryData() } returns nflResult
        every { injuryHandler.getStandardInjuryData(sport) } returns standardResult
    }

    @Test
    fun `should handle request for nfl injury data`() {
        val result = injuryHandler.handleRequest(mapOf("sport" to "nfl"))
        assertEquals(nflResult, result)
    }

    @Test
    fun `should handle request with pipeline invocation`() {
        val result = injuryHandler.handleRequest(mapOf("sport" to sport))
        assertEquals(standardResult, result)
    }
}