@file:Suppress("UNCHECKED_CAST")

package collect

import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.file.Files.readAllBytes
import java.nio.file.Paths

class DraftKingsContestsKtTest {
    private val fakeDraftKingsData: String = String(readAllBytes(Paths.get("src/main/resources/draftKingsDataResponse.json")))

    @Test
    fun shouldGetFixtureLists() {
        val result: List<JSONObject> = getValidDraftKingsContests("nfl") { fakeDraftKingsData }
        assertEquals(1, result.size)
    }

    @Test
    fun shouldGetAllContestData() {
        val result: List<Map<String, Any>> = getDraftKingsContestData("nfl") { fakeDraftKingsData }
        assertEquals("PIT vs CLE (11/14)", result[0]["contest"])
        val players = result[0]["players"] as List<Map<String, Any>>
        val playerInfo1 = players[0]
        assertEquals(822857, playerInfo1["playerId"])
        assertEquals("RB", playerInfo1["position"])
        assertEquals(10600, playerInfo1["salary"])
        val playerInfo2 = players[1]
        assertEquals(742390, playerInfo2["playerId"])
        assertEquals("RB", playerInfo2["position"])
        assertEquals(10800, playerInfo2["salary"])
        assertEquals(38, players.size)
    }
}