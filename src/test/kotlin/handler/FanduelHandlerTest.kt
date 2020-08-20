package handler

import io.mockk.every
import io.mockk.justRun
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FanduelHandlerTest {
    private val date = "a date"
    private val fanduelHandler = spyk<FanduelHandler>()
    private val fanduelResult: List<Map<String, Any>> = listOf()

    @BeforeEach
    fun setUp() {
        every { fanduelHandler.getFanduelContestData(date) } returns fanduelResult
        justRun { fanduelHandler.uploadToS3(any(), any()) }
    }

    @Test
    fun `should handle request with web invocation`() {
        val result = fanduelHandler.handleRequest(mapOf("date" to date))
        verify(exactly = 0) { fanduelHandler.uploadToS3(any(), any()) }
        assertEquals(fanduelResult, result)
    }

    @Test
    fun `should handle request with pipeline invocation`() {
        val result = fanduelHandler.handleRequest(mapOf("date" to date, "invocationType" to "pipeline"))
        verify { fanduelHandler.uploadToS3("fanduelData.json", fanduelResult) }
        assertEquals(listOf<Map<String, Any>>(), result)
    }
}