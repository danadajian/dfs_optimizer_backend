package collect.misc

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files.readAllBytes
import java.nio.file.Paths

class OpponentRanksKtTest {
    private val fakeFantasyProsData: String = String(readAllBytes(Paths.get("src/main/resources/opponentRanksResponse.txt")))

    private val opponentRanks = spyk<OpponentRanks>()

    @BeforeEach
    fun setUp() {
        every { opponentRanks.getOpponentRanksData("nfl") } returns fakeFantasyProsData
    }

    @Test
    fun shouldGetNFLOpponentRanks() {
        val result: Map<String, Map<String, Int>> = opponentRanks.getOpponentRanks("nfl")
        assertEquals(32, result.getValue("Arizona Cardinals")["QB"])
        assertEquals(23, result.getValue("Arizona Cardinals")["RB"])
        assertEquals(17, result.getValue("Miami Dolphins")["TE"])
        assertEquals(30, result.getValue("New York Giants")["WR"])
        assertEquals(15, result.getValue("Detroit Lions")["D/ST"])
    }
}