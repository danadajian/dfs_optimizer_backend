package collect.stats

import api.DataCollector
import org.json.JSONObject
import util.getEasternTime

class Events {
    fun getEventData(sport: String): Map<Int, Map<*, Any>> {
        val apiResponse: String = callEvents(sport)
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

    fun callEvents(sport: String): String {
        return DataCollector().callEvents(sport)
    }
}