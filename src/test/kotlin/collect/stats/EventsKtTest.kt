package collect.stats

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.file.Files.readAllBytes
import java.nio.file.Paths

class EventsKtTest {
    private val fakeNFLEventsResponse: String = String(readAllBytes(Paths.get("src/main/resources/nflEventsResponse.json")))
    private val fakeNHLEventsResponse: String = String(readAllBytes(Paths.get("src/main/resources/nhlEventsResponse.json")))
    private val fakeNBAEventsResponse: String = String(readAllBytes(Paths.get("src/main/resources/nbaEventsResponse.json")))

    @Test
    fun shouldGetHomeOrAwayMapFromNFLEvents() {
        val result: Map<Int, Map<*, Any>> = getEventData("nfl") { fakeNFLEventsResponse }
        assertEquals("v. NYJ", result[2142041]?.get(366))
        assertEquals("@ Bal", result[2142041]?.get(352))
    }

    @Test
    fun shouldGetNHLEventDataFromThisWeek() {
        val result: Map<Int, Map<*, Any>> = getEventData("nhl") { fakeNHLEventsResponse }
        assertEquals("Tue 7:00PM EST", result.getValue(2154769)["gameDate"])
        assertEquals("v. Mon", result.getValue(2154769)[4969])
        assertEquals("@ Pit", result.getValue(2154769)[4963])
    }

    @Test
    fun shouldGetNBAEventDataFromThisWeek() {
        val result: Map<Int, Map<*, Any>> = getEventData("nba") { fakeNBAEventsResponse }
        assertEquals("Tue 8:00PM EST", result.getValue(2177081)["gameDate"])
        assertEquals("v. Den", result.getValue(2177081)[20])
        assertEquals("@ Phi", result.getValue(2177081)[7])
    }
}