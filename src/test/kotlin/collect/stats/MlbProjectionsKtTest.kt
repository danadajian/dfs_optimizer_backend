package collect.stats

import collect.misc.Odds
import collect.misc.Weather
import io.mockk.coEvery
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

class MlbProjectionsKtTest {
    private val fakeMlbEventsResponse: String = String(Files.readAllBytes(Paths.get("src/main/resources/mlbEventsResponse.json")))
    private val fakeMlbParticipantsResponse: String = String(Files.readAllBytes(Paths.get("src/main/resources/mlbParticipantsResponse.json")))
    private val fakeMlbOddsResponse: String = String(Files.readAllBytes(Paths.get("src/main/resources/mlbOddsResponse.json")))
    private val fakeWeatherResponse: String = String(Files.readAllBytes(Paths.get("src/main/resources/weatherResponse.json")))
    private val fakeMlbProjectionsResponse: String = String(Files.readAllBytes(Paths.get("src/main/resources/mlbProjectionsResponse.json")))

    private val mlbProjections = spyk<MlbProjections>()
    private val events = spyk<Events>()
    private val participants = spyk<Participants>()
    private val odds = spyk<Odds>()
    private val weather = spyk<Weather>()

    @BeforeEach
    fun setUp() {
        every { events.callEvents("mlb") } returns fakeMlbEventsResponse
        every { participants.callParticipants("mlb") } returns fakeMlbParticipantsResponse
        every { odds.callOdds("mlb") } returns fakeMlbOddsResponse
        every { weather.callWeather("mlb") } returns fakeWeatherResponse
        every { mlbProjections.getEventData() } returns events.getEventData("mlb")
        every { mlbProjections.getParticipantsData() } returns participants.getParticipantsData("mlb")
        every { mlbProjections.getOddsData() } returns odds.getOddsData("mlb")
        every { mlbProjections.getWeatherData() } returns weather.getWeatherData("mlb")
        every { mlbProjections.getProjectionsFromEvent(any()) } returns fakeMlbProjectionsResponse
    }

    @Test
    fun `should get MLB projections`() = runBlocking {
        val result: Map<Int, Map<String, Any?>> = mlbProjections.getMlbProjectionsData()
        assertEquals("Renato Nunez", result.getValue(596834)["name"])
        assertEquals("Bal", result.getValue(596834)["team"])
        assertEquals("v. Tor", result.getValue(596834)["opponent"])
        assertEquals("Wed 12:05PM EST", result.getValue(596834)["gameDate"])
        assertEquals("+1.5", result.getValue(596834)["spread"])
        assertEquals(9.5, result.getValue(596834)["overUnder"])
        assertEquals(mapOf("forecast" to "Mostly Sunny", "details" to "85°, 0% precip"), result.getValue(596834)["weather"])
        assertEquals(7.95609, result.getValue(596834)["DraftKingsProjection"])
        assertEquals(10.56437, result.getValue(596834)["FanduelProjection"])
    }
}