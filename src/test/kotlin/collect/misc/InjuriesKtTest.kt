package collect.misc

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files.readAllBytes
import java.nio.file.Paths

class InjuriesKtTest {
    private val fakeMLBInjuriesResponse = String(readAllBytes(Paths.get("src/main/resources/mlbInjuryResponse.txt")))
    private val fakeNFLInjuriesResponse = String(readAllBytes(Paths.get("src/main/resources/nflInjuryResponse.txt")))
    private val fakeNBAInjuriesResponse = String(readAllBytes(Paths.get("src/main/resources/nbaInjuryResponse.txt")))
    private val fakeNHLInjuriesResponse = String(readAllBytes(Paths.get("src/main/resources/nhlInjuryResponse.txt")))

    private val injuries = spyk<Injuries>()

    @BeforeEach
    fun setUp() {
        every { injuries.getInjuryData("mlb") } returns fakeMLBInjuriesResponse
        every { injuries.getInjuryData("nfl") } returns fakeNFLInjuriesResponse
        every { injuries.getInjuryData("nba") } returns fakeNBAInjuriesResponse
        every { injuries.getInjuryData("nhl") } returns fakeNHLInjuriesResponse
    }

    @Test
    fun shouldGetMLBInjuryData() {
        val result: Map<String, String> = injuries.getStandardInjuryData("mlb")
        assertEquals("Day-to-Day", result["Pedro Avila"])
        assertEquals("Suspension", result["Tim Beckham"])
    }

    @Test
    fun shouldGetNFLInjuryData() {
        val result: Map<String, String> = injuries.getNFLInjuryData()
        assertEquals("Questionable", result["Evan Engram"])
        assertEquals("Out", result["Hunter Renfrow"])
    }

    @Test
    fun shouldGetNBAInjuryData() {
        val result: Map<String, String> = injuries.getStandardInjuryData("nba")
        assertEquals("Day-To-Day", result["De'Andre Hunter"])
        assertEquals("Out", result["Kyrie Irving"])
        assertEquals("Out", result["Chandler Hutchison"])
    }

    @Test
    fun shouldGetNHLInjuryData() {
        val result: Map<String, String> = injuries.getStandardInjuryData("nhl")
        assertEquals("Day-To-Day", result["Carl Soderberg"])
        assertEquals("IR", result["Rasmus Dahlin"])
    }
}