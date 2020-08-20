package collect.dfs

import api.DataCollector
import org.json.JSONObject
import util.getEasternTime

private val supportedGameTypes = listOf("Classic", "Showdown Captain Mode", "Showdown")
private val supportedContests = listOf("Main", "Thu", "Sat", "Sun")

class DraftKingsContests {
    fun getDraftKingsContestData(sport: String): List<Map<String, Any>> {
        return getValidDraftKingsContests(sport).map { it ->
            val gameDate = it.getJSONArray("competitions").getJSONObject(0).getString("startTime")
            val easternTime = getEasternTime(gameDate, "UTC", "MM/dd")
            val contest = if (it.has("suffix")) {
                val contestName = it.getString("suffix").substring(2, it.getString("suffix").length - 1)
                "$contestName ($easternTime)"
            } else {
                "Main ($easternTime)"
            }
            val playerList = it.getJSONArray("draftPool").filter {
                val playerObject = it as JSONObject
                playerObject["rosterSlots"].toString() != "[\"CPT\"]"
            }.map {
                it as JSONObject
                mapOf(
                        "playerId" to it.getInt("playerId"),
                        "position" to it.getString("position"),
                        "salary" to it.getInt("salary"),
                )
            }
            mapOf(
                    "contest" to contest,
                    "players" to playerList
            )
        }
    }

    fun getValidDraftKingsContests(sport: String): List<JSONObject> {
        val apiResponse: String = getDraftKingsData(sport)
        return JSONObject(apiResponse).getJSONArray("draftPool").filter {
            it as JSONObject
            supportedGameTypes.contains(it.getString("gameType")) &&
                    (!it.has("suffix") ||
                            supportedContests.stream().anyMatch { s: CharSequence ->
                                it.getString("suffix").contains(s)
                            } || it.getString("gameType").contains("Showdown")
                            ) && it.getJSONArray("draftPool").length() > 0
        }.map { it as JSONObject }.toList()
    }

    fun getDraftKingsData(sport: String): String {
        return DataCollector().getDraftKingsData(sport)
    }
}