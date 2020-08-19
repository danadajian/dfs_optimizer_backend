package collect.stats

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
        buildProjectionsMap(apiResponse, listOf("batters", "pitchers"), eventData, participantsData, oddsData, weatherData)
    }.flatten().toMap()
}