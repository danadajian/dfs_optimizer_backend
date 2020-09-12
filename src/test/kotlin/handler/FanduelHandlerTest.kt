package handler

import io.mockk.*
import kotlinx.coroutines.runBlocking
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
        coJustRun { fanduelHandler.uploadToS3(any(), any()) }
    }

    @Test
    fun `should handle request with web invocation`() = runBlocking {
        val result = fanduelHandler.handleRequest(mapOf("date" to date))
        coVerify(exactly = 0) { fanduelHandler.uploadToS3(any(), any()) }
        assertEquals(fanduelResult, result)
    }

    @Test
    fun `should handle request with pipeline invocation`() = runBlocking {
        val result = fanduelHandler.handleRequest(mapOf("date" to date, "invocationType" to "pipeline"))
        coVerify { fanduelHandler.uploadToS3("fanduelData.json", fanduelResult) }
        assertEquals(listOf<Map<String, Any>>(), result)
    }
}