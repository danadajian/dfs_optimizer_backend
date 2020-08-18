package api

private val SPORT_MAP = mapOf(
        "nfl" to "football",
        "mlb" to "baseball",
        "nba" to "basketball",
        "nhl" to "hockey"
)

private fun callStatsApi(baseCall: String): String {
    val (key, secret) = getCredentials()
    val sig: String = getSignature(key, secret)
    return makeHttpRequest("http://api.stats.com/v1/$baseCall?accept=json&api_key=$key&sig=$sig")
}

fun getInjuryData(sport: String): String {
    return makeHttpRequest("http${(if (sport == "nfl") "s" else "")}://www.espn.com/$sport/injuries")
}

fun getCurrentEvents(sport: String): String {
    return callStatsApi("stats/${SPORT_MAP[sport]}/$sport/events/")
}

fun getProjectionsFromThisWeek(sport: String): String {
    return callStatsApi("stats/${SPORT_MAP[sport]}/$sport/fantasyProjections/weekly")
}

fun getProjectionsFromEvent(sport: String, eventId: Int): String {
    return callStatsApi("stats/{SPORT_MAP[sport]}/$sport/fantasyProjections/$eventId")
}

fun getParticipants(sport: String): String {
    return callStatsApi("stats/${SPORT_MAP[sport]}/$sport/participants/")
}

fun getOddsData(sport: String): String {
    return callStatsApi("stats/${SPORT_MAP[sport]}/$sport/odds/")
}

fun getWeatherData(sport: String): String {
    return callStatsApi("stats/${SPORT_MAP[sport]}/$sport/weatherforecasts/")
}

fun getFanduelData(dateString: String): String {
    return makeHttpRequest("https://www.fanduel.com/api/playerprices?date=$dateString")
}

fun getDraftKingsData(sport: String): String {
    return makeHttpRequest("https://api.draftkings.com/partners/v1/draftpool/sports/$sport")
}

fun getOpponentRanksData(sport: String): String {
    return if (sport == "nfl") makeHttpRequest("https://www.fantasypros.com/nfl/points-allowed.php") else ""
}
