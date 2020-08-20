package handler

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ProjectionsHandlerKtTest {
    private val projectionsHandler = spyk<ProjectionsHandler>()
    private val mlbResult: Map<Int, Map<String, Any?>> = mapOf(0 to mapOf())

    @BeforeEach
    fun setUp() {
        every { projectionsHandler.getMlbProjectionsData() } returns mlbResult
        every { projectionsHandler.getNflProjectionsData() } returns mapOf()
        every { projectionsHandler.getNhlProjectionsData() } returns mapOf()
        every { projectionsHandler.getNbaProjectionsData() } returns mapOf()
        justRun { projectionsHandler.uploadToS3(any(), any()) }
    }

    @Test
    fun `should handle request with web invocation`() {
        val result = projectionsHandler.handleRequest(mapOf("sport" to "mlb"))
        verify(exactly = 0) { projectionsHandler.uploadToS3(any(), any()) }
        assertEquals(mapOf("body" to mlbResult), result)
    }

    @Test
    fun `should handle request with pipeline invocation`() {
        val result = projectionsHandler.handleRequest(mapOf("sport" to "mlb", "invocationType" to "pipeline"))
        verify { projectionsHandler.uploadToS3("mlbProjectionsData.json", mlbResult) }
        assertEquals(mapOf("sport" to "mlb"), result)
    }
}
