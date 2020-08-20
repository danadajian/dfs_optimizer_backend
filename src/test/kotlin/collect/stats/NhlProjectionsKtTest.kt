package collect.stats

import collect.misc.Odds
import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import java.nio.file.Files.readAllBytes
import java.nio.file.Paths

class NhlProjectionsKtTest {
    private val fakeNhlEventsResponse: String = String(readAllBytes(Paths.get("src/main/resources/nhlEventsResponse.json")))
    private val fakeNhlParticipantsResponse: String = String(readAllBytes(Paths.get("src/main/resources/nhlParticipantsResponse.json")))
    private val fakeNhlOddsResponse: String = String(readAllBytes(Paths.get("src/main/resources/nhlOddsResponse.json")))
    private val fakeNhlProjectionsResponse: String = String(readAllBytes(Paths.get("src/main/resources/nhlProjectionsResponse.json")))

    private val nhlProjections = spyk<NhlProjections>()
    private val events = spyk<Events>()
    private val participants = spyk<Participants>()
    private val odds = spyk<Odds>()

    @BeforeEach
    fun setUp() {
        every { events.callEvents("nhl") } returns fakeNhlEventsResponse
        every { participants.callParticipants("nhl") } returns fakeNhlParticipantsResponse
        every { odds.callOdds("nhl") } returns fakeNhlOddsResponse
        every { nhlProjections.getEventData() } returns events.getEventData("nhl")
        every { nhlProjections.getParticipantsData() } returns participants.getParticipantsData("nhl")
        every { nhlProjections.getOddsData() } returns odds.getOddsData("nhl")
        every { nhlProjections.getProjectionsFromEvent(any()) } returns fakeNhlProjectionsResponse
    }

    @Test
    fun shouldGetNhlProjections() {
        val result: Map<Int, Map<String, Any?>> = nhlProjections.getNhlProjectionsData()
        assertEquals("Chad Ruhwedel", result.getValue(732552)["name"])
        assertEquals("Pit", result.getValue(732552)["team"])
        assertEquals("v. Mon", result.getValue(732552)["opponent"])
        assertEquals("Tue 7:00PM EST", result.getValue(732552)["gameDate"])
        assertEquals("-1.5", result.getValue(732552)["spread"])
        assertEquals(6.5, result.getValue(732552)["overUnder"])
        assertFalse(result.getValue(732552).containsKey("weather"))
        assertEquals(1.65178, result.getValue(732552)["DraftKingsProjection"])
        assertEquals(5.73402, result.getValue(732552)["FanduelProjection"])
        assertEquals("Christian Folin", result.getValue(824587)["name"])
        assertEquals("Mon", result.getValue(824587)["team"])
        assertEquals("@ Pit", result.getValue(824587)["opponent"])
        assertEquals("Tue 7:00PM EST", result.getValue(824587)["gameDate"])
        assertEquals("+1.5", result.getValue(824587)["spread"])
        assertEquals(6.5, result.getValue(824587)["overUnder"])
        assertEquals(0.0, result.getValue(824587)["DraftKingsProjection"])
        assertEquals(0.0, result.getValue(824587)["FanduelProjection"])
    }
}