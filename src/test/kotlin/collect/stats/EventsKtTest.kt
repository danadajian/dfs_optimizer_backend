package collect.stats

import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files.readAllBytes
import java.nio.file.Paths

internal class EventsKtTest {
    private val fakeMlbEventsResponse: String = String(readAllBytes(Paths.get("src/main/resources/mlbEventsResponse.json")))
    private val fakeNFLEventsResponse: String = String(readAllBytes(Paths.get("src/main/resources/nflEventsResponse.json")))
    private val fakeNHLEventsResponse: String = String(readAllBytes(Paths.get("src/main/resources/nhlEventsResponse.json")))
    private val fakeNBAEventsResponse: String = String(readAllBytes(Paths.get("src/main/resources/nbaEventsResponse.json")))

    private val events = spyk<Events>()

    @BeforeEach
    fun setUp() {
        every { events.callEvents("mlb") } returns fakeMlbEventsResponse
        every { events.callEvents("nfl") } returns fakeNFLEventsResponse
        every { events.callEvents("nhl") } returns fakeNHLEventsResponse
        every { events.callEvents("nba") } returns fakeNBAEventsResponse
    }

    @Test
    fun shouldGetHomeOrAwayMapFromNFLEvents() {
        val result: Map<Int, Map<*, Any>> = events.getEventData("nfl")
        println(result)
        assertEquals("v. NYJ", result[2142041]?.get(366))
        assertEquals("@ Bal", result[2142041]?.get(352))
    }

    @Test
    fun shouldGetNHLEventDataFromThisWeek() {
        val result: Map<Int, Map<*, Any>> = events.getEventData("nhl")
        assertEquals("Tue 7:00PM EST", result.getValue(2154769)["gameDate"])
        assertEquals("v. Mon", result.getValue(2154769)[4969])
        assertEquals("@ Pit", result.getValue(2154769)[4963])
    }

    @Test
    fun shouldGetNBAEventDataFromThisWeek() {
        val result: Map<Int, Map<*, Any>> = events.getEventData("nba")
        assertEquals("Tue 8:00PM EST", result.getValue(2177081)["gameDate"])
        assertEquals("v. Den", result.getValue(2177081)[20])
        assertEquals("@ Phi", result.getValue(2177081)[7])
    }

    @Test
    fun shouldGetMLBEventDataFromThisWeek() {
        val result: Map<Int, Map<*, Any>> = events.getEventData("mlb")
        assertEquals("Wed 12:35PM EST", result.getValue(2246843)["gameDate"])
        assertEquals("v. Phi", result.getValue(2246843)[226])
        assertEquals("@ Bos", result.getValue(2246843)[246])
    }
}