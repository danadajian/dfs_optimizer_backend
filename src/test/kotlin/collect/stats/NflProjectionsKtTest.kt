package collect.stats

import collect.misc.getOddsData
import collect.misc.getWeatherData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

class NflProjectionsKtTest {
    private val fakeNflEventsResponse: String = String(Files.readAllBytes(Paths.get("src/main/resources/nflEventsResponse.json")))
    private val fakeOddsResponse: String = String(Files.readAllBytes(Paths.get("src/main/resources/nflOddsResponse.json")))
    private val fakeWeatherResponse = String(Files.readAllBytes(Paths.get("src/main/resources/weatherResponse.json")))
    private val fakeNflProjectionsResponse: String = String(Files.readAllBytes(Paths.get("src/main/resources/nflProjectionsResponse.json")))

    @Test
    fun shouldGetNflProjectionsFromThisWeek() {
        val result: Map<Int, Map<String, Any?>> = getNflProjectionsData(
                { getEventData("nfl") { fakeNflEventsResponse } },
                { getOddsData("nfl") { fakeOddsResponse } },
                { getWeatherData("nfl") { fakeWeatherResponse } },
                { fakeNflProjectionsResponse }
        )
        assertEquals("Lamar Jackson", result.getValue(877745)["name"])
        assertEquals("Bal", result.getValue(877745)["team"])
        assertEquals("v. NYJ", result.getValue(877745)["opponent"])
        assertEquals("Thu 8:20PM EST", result.getValue(366)["gameDate"])
        assertEquals("-15.0", result.getValue(366)["spread"])
        assertEquals(44.5, result.getValue(366)["overUnder"])
        assertEquals("Sunny", (result.getValue(877745)["weather"] as Map<*, *>?)!!["forecast"])
        assertEquals("85°, 4% precip", (result.getValue(877745)["weather"] as Map<*, *>?)!!["details"])
        assertEquals(25.4156364974765640884448627852073774696, result.getValue(877745)["DraftKingsProjection"])
        assertEquals(24.1805531418601748497370736365420855337, result.getValue(877745)["FanduelProjection"])
        assertEquals("Jets D/ST", result.getValue(352)["name"])
        assertEquals("NYJ", result.getValue(352)["team"])
        assertEquals("@ Bal", result.getValue(352)["opponent"])
        assertEquals("Thu 8:20PM EST", result.getValue(352)["gameDate"])
        assertEquals("+15.0", result.getValue(352)["spread"])
        assertEquals(44.5, result.getValue(352)["overUnder"])
        assertEquals(3.41, result.getValue(352)["DraftKingsProjection"])
        assertEquals(3.41, result.getValue(352)["FanduelProjection"])
    }
}