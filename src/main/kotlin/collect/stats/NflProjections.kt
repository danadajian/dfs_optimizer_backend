package collect.stats

import api.DataCollector
import collect.misc.Odds
import collect.misc.Weather
import org.json.JSONObject


class NflProjections {
    private val sport = "nfl"

    fun getNflProjectionsData(): Map<Int, Map<String, Any?>> {
        val eventData = getEventData()
        val oddsData = getOddsData()
        val weatherData = getWeatherData()
        val apiResponse: String = getProjectionsFromThisWeek()
        return if (apiResponse.isEmpty()) mapOf() else {
            val projectionsJson = JSONObject(apiResponse).getJSONArray("apiResults").getJSONObject(0)
                    .getJSONObject("league").getJSONObject("season").getJSONArray("eventType").getJSONObject(0)
                    .getJSONObject("fantasyProjections")
            val offensiveJsonArray = projectionsJson.getJSONArray("offensiveProjections")
            val defensiveJsonArray = projectionsJson.getJSONArray("defensiveProjections")
            listOf(offensiveJsonArray, defensiveJsonArray).flatten().map { playerJson ->
                playerJson as JSONObject
                val isPlayer: Boolean = playerJson.has("player")
                val teamId = playerJson.getJSONObject("team").getInt("teamId")
                val id = if (isPlayer) playerJson.getJSONObject("player").getInt("playerId") else teamId
                val eventId = playerJson.getInt("eventId")
                val name = if (isPlayer)
                    "${playerJson.getJSONObject("player").getString("firstName")} ${playerJson.getJSONObject("player").getString("lastName")}"
                else "${playerJson.getJSONObject("team").getString("nickname")} D/ST"
                id to mapOf(
                        "name" to name,
                        "team" to playerJson.getJSONObject("team").getString("abbreviation"),
                        *(getSpreadCommonMapValues(playerJson, eventId, teamId, eventData, oddsData, weatherData))
                )
            }.toMap()
        }
    }

    fun getEventData(): Map<Int, Map<*, Any>> {
        return Events().getEventData(sport)
    }

    fun getOddsData(): Map<Int, Map<String, Number>> {
        return Odds().getOddsData(sport)
    }

    fun getWeatherData(): Map<Int, Map<String, String>> {
        return Weather().getWeatherData(sport)
    }

    fun getProjectionsFromThisWeek(): String {
        return DataCollector().getProjectionsFromThisWeek(sport)
    }
}
