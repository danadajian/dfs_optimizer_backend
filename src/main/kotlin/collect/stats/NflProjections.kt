package collect.stats

import org.json.JSONObject

fun getNflProjectionsData(
        eventGetter: (sport: String) -> Map<Int, Map<*, Any>>,
        oddsGetter: (sport: String) -> Map<Int, Map<String, Number>>,
        weatherGetter: (sport: String) -> Map<Int, Map<String, String>>,
        projectionsGetter: (sport: String) -> String
): Map<Int, Map<String, Any?>> {
    val sport = "nfl"
    val eventData: Map<Int, Map<*, Any>> = eventGetter(sport)
    val oddsData: Map<Int, Map<String, Number>> = oddsGetter(sport)
    val weatherData: Map<Int, Map<String, String>> = weatherGetter(sport)
    val apiResponse: String = projectionsGetter(sport)
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
