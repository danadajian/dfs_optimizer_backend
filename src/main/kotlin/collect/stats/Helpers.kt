package collect.stats

import org.json.JSONObject

fun buildProjectionsMap(
        apiResponse: String,
        playerTypes: List<String>,
        eventData: Map<Int, Map<*, Any>>,
        participantsData: Map<Int, Map<String, String>>,
        oddsData: Map<Int, Map<String, Number>>,
        weatherData: Map<Int, Map<String, String>> = mapOf()
): List<Pair<Int, Map<String, Any?>>> {
    val projectionsJson = JSONObject(apiResponse).getJSONArray("apiResults").getJSONObject(0)
            .getJSONObject("league").getJSONObject("season").getJSONArray("eventType")
            .getJSONObject(0).getJSONObject("fantasyProjections")
    val eventId = projectionsJson.getInt("eventId")
    val teamsJson = projectionsJson.getJSONArray("teams")
    return teamsJson.map { projectionObject ->
        projectionObject as JSONObject
        val teamId = projectionObject.getInt("teamId")
        playerTypes.map { playerType ->
            projectionObject.getJSONArray(playerType).filter { player ->
                player as JSONObject
                val playerId = player.getInt("playerId")
                participantsData.containsKey(playerId)
            }.map { playerJson ->
                playerJson as JSONObject
                val playerId = playerJson.getInt("playerId")
                playerId to mapOf(
                        "name" to participantsData.getValue(playerId)["name"],
                        "team" to participantsData.getValue(playerId)["team"],
                        *(getSpreadCommonMapValues(playerJson, eventId, teamId, eventData, oddsData, weatherData))
                )
            }
        }
    }.flatten().flatten()
}

fun getSpreadCommonMapValues(
        playerJson: JSONObject,
        eventId: Int,
        teamId: Int,
        eventData: Map<Int, Map<*, Any>>,
        oddsData: Map<Int, Map<String, Number>>,
        weatherData: Map<Int, Map<String, String>> = mapOf()
): Array<Pair<String, Any?>> {
    val eventOddsData = oddsData.getOrDefault(eventId, mapOf())
    val spreadMultiplier = if ((eventOddsData.getOrDefault("favoriteTeamId", 0)) as Int == teamId) 1 else -1
    val spreadSign = if ((eventOddsData.getOrDefault("favoriteTeamId", 0)) as Int == teamId) "" else "+"
    val fantasyProjections = playerJson.getJSONArray("fantasyProjections")
    val draftKingsProjection = (fantasyProjections.find {
        it as JSONObject
        it.getString("name").contains("DraftKings")
    } as? JSONObject)?.getString("points")?.toDouble()
    val fanduelProjection = (fantasyProjections.find {
        it as JSONObject
        it.getString("name").contains("FanDuel")
    } as? JSONObject)?.getString("points")?.toDouble()
    return arrayOf(
            "opponent" to eventData.getValue(eventId)[teamId],
            "gameDate" to eventData.getValue(eventId)["gameDate"],
            *(if (weatherData.isNotEmpty()) arrayOf("weather" to weatherData.getOrDefault(eventId, mapOf())) else arrayOf()),
            "spread" to spreadSign + spreadMultiplier * (eventOddsData.getOrDefault("spread", 0.0)) as Double,
            "overUnder" to (eventOddsData.getOrDefault("overUnder", 0)),
            "DraftKingsProjection" to (draftKingsProjection ?: 0),
            "FanduelProjection" to (fanduelProjection ?: 0)
    )
}