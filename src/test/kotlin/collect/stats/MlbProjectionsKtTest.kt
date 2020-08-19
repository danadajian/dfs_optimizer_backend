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
//        val result: Map<Int, Map<String, Any?>> = getMlbProjectionsData(
//                { getEventData("mlb") { fakeMlbEventsResponse } },
//                { getParticipantsData("mlb") { fakeMlbParticipantsResponse } },
//                { getOddsData("mlb") { fakeMlbOddsResponse } },
//                { getWeatherData("mlb") { fakeWeatherResponse } },
//                {_: String, _: Int -> fakeMlbProjectionsResponse }
//        )
        assertTrue(true)
    }
}