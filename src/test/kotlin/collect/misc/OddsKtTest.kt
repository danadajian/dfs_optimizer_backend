package collect.misc

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

class OddsKtTest {
    private val fakeOddsResponse: String = String(Files.readAllBytes(Paths.get("src/main/resources/nflOddsResponse.json")))

    @Test
    fun shouldGetOdds() {
        val result: Map<Int, Map<String, Number>> = getOddsData("nfl") { fakeOddsResponse }
        assertEquals(339, result[2142058]?.get("favoriteTeamId"))
        assertEquals(45.5, result[2142058]?.get("overUnder"))
        assertEquals(-10.0, result[2142058]?.get("spread"))
    }
}