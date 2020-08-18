package collect.stats

import collect.misc.getOddsData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.nio.file.Files.readAllBytes
import java.nio.file.Paths

class NbaProjectionsKtTest {
    private val fakeNBAEventsResponse: String = String(readAllBytes(Paths.get("src/main/resources/nbaEventsResponse.json")))
    private val fakeNBAParticipantsResponse: String = String(readAllBytes(Paths.get("src/main/resources/nbaParticipantsResponse.json")))
    private val fakeNBAOddsResponse: String = String(readAllBytes(Paths.get("src/main/resources/nbaOddsResponse.json")))
    private val fakeNBAProjectionsResponse: String = String(readAllBytes(Paths.get("src/main/resources/nbaProjectionsResponse.json")))

    @Test
    fun shouldGetNBAProjections() {
        val result: Map<Int, Map<String, Any?>> = getNbaProjectionsData(
                { getEventData("nba") {fakeNBAEventsResponse} },
                { getParticipantsData("nba") {fakeNBAParticipantsResponse} },
                { getOddsData("nba") {fakeNBAOddsResponse} },
                {_: String, _: Int -> fakeNBAProjectionsResponse }
        )
        assertEquals("Al Horford", result.getValue(280587)["name"])
        assertEquals("Phi", result.getValue(280587)["team"])
        assertEquals("v. Den", result.getValue(280587)["opponent"])
        assertEquals("Tue 8:00PM EST", result.getValue(280587)["gameDate"])
        assertEquals("-4.0", result.getValue(280587)["spread"])
        assertEquals(207.0, result.getValue(280587)["overUnder"])
        assertFalse(result.getValue(280587).containsKey("weather"))
        assertEquals(29.65199, result.getValue(280587)["DraftKingsProjection"])
        assertEquals(30.02133, result.getValue(280587)["FanduelProjection"])
        assertEquals("Paul Millsap", result.getValue(237675)["name"])
        assertEquals("Den", result.getValue(237675)["team"])
        assertEquals("@ Phi", result.getValue(237675)["opponent"])
        assertEquals("Tue 8:00PM EST", result.getValue(237675)["gameDate"])
        assertEquals("+4.0", result.getValue(237675)["spread"])
        assertEquals(207.0, result.getValue(237675)["overUnder"])
        assertEquals(0, result.getValue(237675)["DraftKingsProjection"])
        assertEquals(21.28275, result.getValue(237675)["FanduelProjection"])
    }
}