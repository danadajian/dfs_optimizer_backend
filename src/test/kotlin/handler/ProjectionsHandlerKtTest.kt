package handler

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ProjectionsHandlerKtTest {
    private val projectionsHandler = spyk<ProjectionsHandler>()
    private val mlbResult: Map<Int, Map<String, Any?>> = mapOf(0 to mapOf())

    @BeforeEach
    fun setUp() {
        coEvery { projectionsHandler.getMlbProjectionsData() } returns mlbResult
        coEvery { projectionsHandler.getNflProjectionsData() } returns mapOf()
        coEvery { projectionsHandler.getNhlProjectionsData() } returns mapOf()
        coEvery { projectionsHandler.getNbaProjectionsData() } returns mapOf()
        coJustRun { projectionsHandler.uploadToS3(any(), any()) }
    }

    @Test
    fun `should handle request with web invocation`() = runBlocking {
        val result = projectionsHandler.handleRequest(mapOf("sport" to "mlb"))
        coVerify(exactly = 0) { projectionsHandler.uploadToS3(any(), any()) }
        assertEquals(mapOf("body" to mlbResult), result)
    }

    @Test
    fun `should handle request with pipeline invocation`() = runBlocking {
        val result = projectionsHandler.handleRequest(mapOf("sport" to "mlb", "invocationType" to "pipeline"))
        coVerify { projectionsHandler.uploadToS3("mlbProjectionsData.json", mlbResult) }
        assertEquals(mapOf("sport" to "mlb"), result)
    }
}
