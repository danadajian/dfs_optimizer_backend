@file:Suppress("UNCHECKED_CAST")

package collect.dfs

import io.mockk.every
import io.mockk.spyk
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files.readAllBytes
import java.nio.file.Paths

class FanduelContestsKtTest {
    private val fakeFanduelData: String = String(readAllBytes(Paths.get("src/main/resources/fanduelDataResponse.txt")))

    private val date = "testDateString"
    private val fanduelContests = spyk<FanduelContests>()

    @BeforeEach
    fun setUp() {
        every { fanduelContests.getFanduelData(date) } returns fakeFanduelData
    }

    @Test
    fun shouldGetValidContests() {
        val result: List<JSONObject> = fanduelContests.getValidFanduelContests(date)
        assertEquals(3, result.size)
    }

    @Test
    fun shouldGetAllContestData() {
        val result: List<Map<String, Any>> = fanduelContests.getFanduelContestData(date)
        assertEquals("NFL", result[0]["sport"])
        assertEquals("PIT @ CLE", result[0]["contest"])
        val players = result[0]["players"] as List<Map<String, Any>>
        val playerInfo1 = players[0]
        assertEquals(748070, playerInfo1["playerId"])
        assertEquals("Baker Mayfield", playerInfo1["name"])
        assertEquals("CLE", playerInfo1["team"])
        assertEquals("QB", playerInfo1["position"])
        assertEquals(15500, playerInfo1["salary"])
        val playerInfo2 = players[1]
        assertNull(playerInfo2["playerId"])
        assertEquals("James Conner", playerInfo2["name"])
        assertEquals("PIT", playerInfo2["team"])
        assertEquals("RB", playerInfo2["position"])
        assertEquals(14500, playerInfo2["salary"])
        assertEquals(44, players.size)
    }
}