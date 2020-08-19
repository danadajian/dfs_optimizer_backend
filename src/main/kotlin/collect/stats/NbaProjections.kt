package collect.stats

fun getNbaProjectionsData(
        eventGetter: (sport: String) -> Map<Int, Map<*, Any>>,
        participantsGetter: (sport: String) -> Map<Int, Map<String, String>>,
        oddsGetter: (sport: String) -> Map<Int, Map<String, Number>>,
        projectionsGetter: (sport: String, eventId: Int) -> String
): Map<Int, Map<String, Any?>> {
    val sport = "nba"
    val eventData = eventGetter(sport)
    val participantsData = participantsGetter(sport)
    val oddsData = oddsGetter(sport)
    val eventIds = eventData.keys
    return eventIds.map { eventId ->
        projectionsGetter(sport, eventId)
    }.filter { apiResponse ->
        apiResponse.isNotEmpty()
    }.map { apiResponse ->
        buildProjectionsMap(apiResponse, listOf("players"), eventData, participantsData, oddsData)
    }.flatten().toMap()
}