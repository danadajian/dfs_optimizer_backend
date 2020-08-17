package handler

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class KotlinHandlerKtTest {
    @Test
    fun test() {
        assertEquals("The result is: test", handleRequest(mapOf("input" to "test")))
    }
}
