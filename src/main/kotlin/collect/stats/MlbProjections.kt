package collect.stats

import org.json.JSONObject

fun getMlbProjectionsData(
        eventGetter: (sport: String) -> Map<Int, Map<*, Any>>,
        participantsGetter: (sport: String) -> Map<Int, Map<String, String>>,
        oddsGetter: (sport: String) -> Map<Int, Map<String, Number>>,
        weatherGetter: (sport: String) -> Map<Int, Map<String, String>>,
        projectionsGetter: (sport: String, eventId: Int) -> String,
): Map<Int, Map<String, Any?>> {
    val sport = "mlb"
    val eventData = eventGetter(sport)
    val participantsData = participantsGetter(sport)
    val oddsData = oddsGetter(sport)
    val weatherData = weatherGetter(sport)
    val eventIds = eventData.keys
    return eventIds.map { eventId ->
        projectionsGetter(sport, eventId)
    }.filter { apiResponse ->
        apiResponse.isNotEmpty()
    }.map { apiResponse ->
        val projectionsJson = JSONObject(apiResponse).getJSONArray("apiResults").getJSONObject(0)
                .getJSONObject("league").getJSONObject("season").getJSONArray("eventType")
                .getJSONObject(0).getJSONObject("fantasyProjections")
        val eventId = projectionsJson.getInt("eventId")
        val teamsJson = projectionsJson.getJSONArray("teams")
        teamsJson.map { projectionObject ->
            projectionObject as JSONObject
            val teamId = projectionObject.getInt("teamId")
            listOf("batters", "pitchers").map { playerType ->
                projectionObject.getJSONArray(playerType).filter { player ->
                    player as JSONObject
                    val playerId = player.getInt("playerId")
                    participantsData.containsKey(playerId)
                }.map { player ->
                    player as JSONObject
                    val playerId = player.getInt("playerId")
                    val eventOddsData = oddsData.getOrDefault(eventId, mapOf())
                    val spreadMultiplier = if ((eventOddsData.getOrDefault("favoriteTeamId", 0)) as Int == teamId) 1 else -1
                    val spreadSign = if ((eventOddsData.getOrDefault("favoriteTeamId", 0)) as Int == teamId) "" else "+"
                    val fantasyProjections = player.getJSONArray("fantasyProjections")
                    val draftKingsProjection = (fantasyProjections.find {
                        it as JSONObject
                        it.getString("name").contains("DraftKings")
                    } as? JSONObject)?.getString("points")?.toDouble()
                    val fanduelProjection = (fantasyProjections.find {
                        it as JSONObject
                        it.getString("name").contains("FanDuel")
                    } as? JSONObject)?.getString("points")?.toDouble()
                    playerId to mapOf(
                            "name" to participantsData.getValue(playerId)["name"],
                            "team" to participantsData.getValue(playerId)["team"],
                            "opponent" to eventData.getValue(eventId)[teamId],
                            "gameDate" to eventData.getValue(eventId)["gameDate"],
                            "weather" to weatherData.getOrDefault(eventId, mapOf()),
                            "spread" to spreadSign + spreadMultiplier * (eventOddsData.getOrDefault("spread", 0.0)) as Double,
                            "overUnder" to (eventOddsData.getOrDefault("overUnder", 0)),
                            "DraftKingsProjection" to (draftKingsProjection ?: 0),
                            "FanduelProjection" to (fanduelProjection ?: 0),
                    )
                }
            }
        }
    }.flatten().flatten().flatten().toMap()
}