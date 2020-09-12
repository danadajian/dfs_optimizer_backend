package collect.stats

import collect.misc.Odds
import collect.misc.Weather
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

class NflProjectionsKtTest {
    private val fakeNflEventsResponse: String = String(Files.readAllBytes(Paths.get("src/main/resources/nflEventsResponse.json")))
    private val fakeNflOddsResponse: String = String(Files.readAllBytes(Paths.get("src/main/resources/nflOddsResponse.json")))
    private val fakeWeatherResponse = String(Files.readAllBytes(Paths.get("src/main/resources/weatherResponse.json")))
    private val fakeNflProjectionsResponse: String = String(Files.readAllBytes(Paths.get("src/main/resources/nflProjectionsResponse.json")))

    private val nflProjections = spyk<NflProjections>()
    private val events = spyk<Events>()
    private val odds = spyk<Odds>()
    private val weather = spyk<Weather>()

    @BeforeEach
    fun setUp() {
        every { events.callEvents("nfl") } returns fakeNflEventsResponse
        every { odds.callOdds("nfl") } returns fakeNflOddsResponse
        every { weather.callWeather("nfl") } returns fakeWeatherResponse
        every { nflProjections.getEventData() } returns events.getEventData("nfl")
        every { nflProjections.getOddsData() } returns odds.getOddsData("nfl")
        every { nflProjections.getWeatherData() } returns weather.getWeatherData("nfl")
        every { nflProjections.getProjectionsFromThisWeek() } returns fakeNflProjectionsResponse
    }

    @Test
    fun `should get NFL projections from this week`() = runBlocking {
        val result: Map<Int, Map<String, Any?>> = nflProjections.getNflProjectionsData()
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