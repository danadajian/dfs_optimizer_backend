package collect.stats

import org.json.JSONObject
import util.getEasternTime

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
    if (apiResponse.isNotEmpty()) {
        val projectionsJson = JSONObject(apiResponse).getJSONArray("apiResults").getJSONObject(0)
                .getJSONObject("league").getJSONObject("season").getJSONArray("eventType").getJSONObject(0)
                .getJSONObject("fantasyProjections")
        val offensiveJsonArray = projectionsJson.getJSONArray("offensiveProjections")
        val defensiveJsonArray = projectionsJson.getJSONArray("defensiveProjections")
        val offensiveMap = offensiveJsonArray.map { it ->
            it as JSONObject
            val playerId = it.getJSONObject("player").getInt("playerId")
            val eventId = it.getInt("eventId")
            val teamId = it.getJSONObject("team").getInt("teamId")
            val eventOddsData = oddsData.getOrDefault(eventId, mapOf())
            val spreadMultiplier = if ((eventOddsData.getOrDefault("favoriteTeamId", 0)) as Int == teamId) 1 else -1
            val spreadSign = if ((eventOddsData.getOrDefault("favoriteTeamId", 0)) as Int == teamId) "" else "+"
            val fantasyProjections = it.getJSONArray("fantasyProjections")
            val draftKingsProjection = (fantasyProjections.find {
                it as JSONObject
                it.getString("name").contains("DraftKings")
            } as? JSONObject)?.getString("points")?.toDouble()
            val fanduelProjection = (fantasyProjections.find {
                it as JSONObject
                it.getString("name").contains("FanDuel")
            } as? JSONObject)?.getString("points")?.toDouble()
            playerId to mapOf(
                    "name" to it.getJSONObject("player").getString("firstName") + " " +
                            it.getJSONObject("player").getString("lastName"),
                    "team" to it.getJSONObject("team").getString("abbreviation"),
                    "opponent" to eventData.getValue(eventId)[teamId],
                    "gameDate" to getEasternTime(it.getJSONObject("gameDate").getString("full"),
                            it.getJSONObject("gameDate").getString("dateType"), "EEE h:mma z"),
                    "weather" to weatherData.getOrDefault(eventId, mapOf()),
                    "spread" to spreadSign + spreadMultiplier * (eventOddsData.getOrDefault("spread", 0.0)) as Double,
                    "overUnder" to (eventOddsData.getOrDefault("overUnder", 0)),
                    "DraftKingsProjection" to (draftKingsProjection ?: 0),
                    "FanduelProjection" to (fanduelProjection ?: 0),
            )
        }.toMap()
        val defensiveMap = defensiveJsonArray.map { it ->
            it as JSONObject
            val eventId = it.getInt("eventId")
            val teamId = it.getJSONObject("team").getInt("teamId")
            val eventOddsData = oddsData.getOrDefault(eventId, mapOf())
            val spreadMultiplier = if ((eventOddsData.getOrDefault("favoriteTeamId", 0)) as Int == teamId) 1 else -1
            val spreadSign = if ((eventOddsData.getOrDefault("favoriteTeamId", 0)) as Int == teamId) "" else "+"
            val fantasyProjections = it.getJSONArray("fantasyProjections")
            val draftKingsProjection = (fantasyProjections.find {
                it as JSONObject
                it.getString("name").contains("DraftKings")
            } as? JSONObject)?.getString("points")?.toDouble()
            val fanduelProjection = (fantasyProjections.find {
                it as JSONObject
                it.getString("name").contains("FanDuel")
            } as? JSONObject)?.getString("points")?.toDouble()
            teamId to mapOf(
                    "name" to it.getJSONObject("team").getString("nickname") + " D/ST",
                    "team" to it.getJSONObject("team").getString("abbreviation"),
                    "opponent" to eventData.getValue(eventId)[teamId],
                    "gameDate" to getEasternTime(it.getJSONObject("gameDate").getString("full"),
                            it.getJSONObject("gameDate").getString("dateType"), "EEE h:mma z"),
                    "weather" to weatherData.getOrDefault(eventId, mapOf()),
                    "spread" to spreadSign + spreadMultiplier * (eventOddsData.getOrDefault("spread", 0.0)) as Double,
                    "overUnder" to (eventOddsData.getOrDefault("overUnder", 0)),
                    "DraftKingsProjection" to (draftKingsProjection ?: 0),
                    "FanduelProjection" to (fanduelProjection ?: 0),
            )
        }.toMap()
        return offensiveMap.mergeReduce(defensiveMap)
    }
    return mapOf()
}

fun <K, V> Map<K, V>.mergeReduce(vararg others: Map<K, V>, reduce: (V, V) -> V = { _, b -> b }): Map<K, V> =
        this.toMutableMap().apply { others.forEach { other -> other.forEach { merge(it.key, it.value, reduce) } } }