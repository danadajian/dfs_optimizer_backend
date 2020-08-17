package handler

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class KotlinHandlerKtTest {
    @Test
    fun test() {
        assertEquals("The result is: test", KotlinHandler().handleRequest(mapOf("input" to "test")))
    }
}
