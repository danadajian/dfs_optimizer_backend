package collect.stats

import org.json.JSONObject
import util.getEasternTime

fun getEventData(sport: String, dataGetter: (sport: String) -> String): Map<Int, Map<*, Any>> {
    val apiResponse: String = dataGetter(sport)
    return if (apiResponse.isEmpty()) mapOf() else {
        val eventsArray = JSONObject(apiResponse).getJSONArray("apiResults").getJSONObject(0)
                .getJSONObject("league").getJSONObject("season").getJSONArray("eventType").getJSONObject(0)
                .getJSONArray("events")
        eventsArray.map {
            it as JSONObject
            val eventId = it.getInt("eventId")
            val dateObject = it.getJSONArray("startDate").getJSONObject(1)
            val gameDate: String = getEasternTime(dateObject.getString("full"),
                    dateObject.getString("dateType"), "EEE h:mma z")
            val teamsArray = it.getJSONArray("teams")
            val homeTeamId = teamsArray.getJSONObject(0).getInt("teamId")
            val awayTeamId = teamsArray.getJSONObject(1).getInt("teamId")
            val homeAbbrev = teamsArray.getJSONObject(0).getString("abbreviation")
            val awayAbbrev = teamsArray.getJSONObject(1).getString("abbreviation")
            eventId to mapOf(
                    "gameDate" to gameDate,
                    homeTeamId to "v. $awayAbbrev",
                    awayTeamId to "@ $homeAbbrev"
            )
        }.toMap()
    }
}