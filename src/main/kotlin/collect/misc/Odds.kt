package collect.misc

import api.DataCollector
import org.json.JSONException
import org.json.JSONObject

class Odds {
    fun getOddsData(sport: String): Map<Int, Map<String, Number>> {
        val apiResponse: String = callOdds(sport)
        return if (apiResponse.isEmpty()) mapOf() else {
            try {
                val oddsArray = JSONObject(apiResponse).getJSONArray("apiResults")
                        .getJSONObject(0).getJSONObject("league").getJSONObject("season")
                        .getJSONArray("eventType").getJSONObject(0).getJSONArray("lineEvents")
                oddsArray.map {
                    it as JSONObject
                    val eventId = it.getInt("eventId")
                    val currentLine = it.getJSONArray("lines").getJSONObject(0)
                            .getJSONArray("line").getJSONObject(1)
                    eventId to mapOf(
                            "favoriteTeamId" to currentLine.getInt("favoriteTeamId"),
                            "spread" to currentLine.getNumber("favoritePoints"),
                            "overUnder" to currentLine.getNumber("total")
                    )
                }.toMap()
            } catch (e: JSONException) {
                e.printStackTrace()
                mapOf()
            }
        }
    }

    fun callOdds(sport: String): String {
        return DataCollector().callOdds(sport)
    }
}