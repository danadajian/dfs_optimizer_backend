package collect.misc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.file.Files.readAllBytes
import java.nio.file.Paths

class OpponentRanksKtTest {
    private val fakeFantasyProsData: String = String(readAllBytes(Paths.get("src/main/resources/opponentRanksResponse.txt")))

    @Test
    fun shouldGetNFLOpponentRanks() {
        val result: Map<String, Map<String, Int>> = getOpponentRanks("nfl") { fakeFantasyProsData }
        assertEquals(32, result.getValue("Arizona Cardinals")["QB"])
        assertEquals(23, result.getValue("Arizona Cardinals")["RB"])
        assertEquals(17, result.getValue("Miami Dolphins")["TE"])
        assertEquals(30, result.getValue("New York Giants")["WR"])
        assertEquals(15, result.getValue("Detroit Lions")["D/ST"])
    }
}