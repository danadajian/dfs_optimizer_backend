package handler

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DraftKingsHandlerTest {
    private val sport = "a sport"
    private val draftKingsHandler = spyk<DraftKingsHandler>()
    private val draftKingsResult: List<Map<String, Any>> = listOf()

    @BeforeEach
    fun setUp() {
        every { draftKingsHandler.getDraftKingsContestData(sport) } returns draftKingsResult
    }

    @Test
    fun `should handle request`() {
        val result = draftKingsHandler.handleRequest(mapOf("sport" to sport))
        assertEquals(draftKingsResult, result)
    }
}