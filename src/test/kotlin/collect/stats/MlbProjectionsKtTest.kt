package collect.stats

import collect.misc.getOddsData
import collect.misc.getWeatherData
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.nio.file.Files
import java.nio.file.Paths

class MlbProjectionsKtTest {
    private val fakeMlbEventsResponse: String = String(Files.readAllBytes(Paths.get("src/main/resources/mlbEventsResponse.json")))
    private val fakeMlbParticipantsResponse: String = String(Files.readAllBytes(Paths.get("src/main/resources/mlbParticipantsResponse.json")))
    private val fakeMlbOddsResponse: String = String(Files.readAllBytes(Paths.get("src/main/resources/mlbOddsResponse.json")))
    private val fakeWeatherResponse = String(Files.readAllBytes(Paths.get("src/main/resources/weatherResponse.json")))
    private val fakeMlbProjectionsResponse: String = String(Files.readAllBytes(Paths.get("src/main/resources/mlbProjectionsResponse.json")))

    @Test
    fun shouldGetMlbProjections() {
        val result: Map<Int, Map<String, Any?>> = getMlbProjectionsData(
                { getEventData("mlb") { fakeMlbEventsResponse } },
                { getParticipantsData("mlb") { fakeMlbParticipantsResponse } },
                { getOddsData("mlb") { fakeMlbOddsResponse } },
                { getWeatherData("mlb") { fakeWeatherResponse } },
                {_: String, _: Int -> fakeMlbProjectionsResponse }
        )
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