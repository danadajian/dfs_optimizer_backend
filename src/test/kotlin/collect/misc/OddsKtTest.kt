package collect.misc

import api.DataCollector
import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

class OddsKtTest {
    private val fakeOddsResponse: String = String(Files.readAllBytes(Paths.get("src/main/resources/nflOddsResponse.json")))

    private val odds = spyk<Odds>()

    @BeforeEach
    fun setUp() {
        every { odds.callOdds("nfl") } returns fakeOddsResponse
    }

    @Test
    fun shouldGetOdds() {
        val result: Map<Int, Map<String, Number>> = odds.getOddsData("nfl")
        assertEquals(339, result[2142058]?.get("favoriteTeamId"))
        assertEquals(45.5, result[2142058]?.get("overUnder"))
        assertEquals(-10.0, result[2142058]?.get("spread"))
    }
}