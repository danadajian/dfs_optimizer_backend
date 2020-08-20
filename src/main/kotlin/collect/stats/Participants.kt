package collect.stats

import api.DataCollector
import org.json.JSONObject

class Participants {
    fun getParticipantsData(sport: String): Map<Int, Map<String, String>> {
        val apiResponse: String = callParticipants(sport)
        return if (apiResponse.isEmpty()) mapOf() else {
            val playersArray = JSONObject(apiResponse).getJSONArray("apiResults").getJSONObject(0)
                    .getJSONObject("league").getJSONArray("players")
            playersArray.map {
                it as JSONObject
                it.getInt("playerId") to mapOf(
                        "name" to "${it.getString("firstName")} ${it.getString("lastName")}",
                        *(if (it.has("team"))
                            arrayOf("team" to it.getJSONObject("team").getString("abbreviation")) else arrayOf())
                )
            }.toMap()
        }
    }

    fun callParticipants(sport: String): String {
        return DataCollector().callParticipants(sport)
    }
}