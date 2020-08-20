package collect.stats

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files.readAllBytes
import java.nio.file.Paths

class ParticipantsKtTest {
    private val fakeNHLParticipantsResponse: String = String(readAllBytes(Paths.get("src/main/resources/nhlParticipantsResponse.json")))
    private val fakeNBAParticipantsResponse: String = String(readAllBytes(Paths.get("src/main/resources/nbaParticipantsResponse.json")))

    private val participants = spyk<Participants>()

    @BeforeEach
    fun setUp() {
        every { participants.callParticipants("nhl") } returns fakeNHLParticipantsResponse
        every { participants.callParticipants("nba") } returns fakeNBAParticipantsResponse
    }

    @Test
    fun shouldGetNHLParticipantsData() {
        val result: Map<Int, Map<String, String>> = participants.getParticipantsData("nhl")
        assertEquals("Chad Ruhwedel", result.getValue(732552)["name"])
        assertEquals("Pit", result.getValue(732552)["team"])
        assertEquals("Christian Folin", result.getValue(824587)["name"])
        assertEquals("Mon", result.getValue(824587)["team"])
    }

    @Test
    fun shouldGetNBAParticipantsData() {
        val result: Map<Int, Map<String, String>> = participants.getParticipantsData("nba")
        assertEquals("Al Horford", result.getValue(280587)["name"])
        assertEquals("Phi", result.getValue(280587)["team"])
        assertEquals("Paul Millsap", result.getValue(237675)["name"])
        assertEquals("Den", result.getValue(237675)["team"])
    }
}