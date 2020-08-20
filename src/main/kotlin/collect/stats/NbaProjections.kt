package collect.stats

import api.DataCollector
import collect.misc.Odds

class NbaProjections {
    private val sport = "nba"

    fun getNbaProjectionsData(): Map<Int, Map<String, Any?>> {
        val eventData = getEventData()
        val participantsData = getParticipantsData()
        val oddsData = getOddsData()
        val eventIds = eventData.keys
        return eventIds.map { eventId ->
            getProjectionsFromEvent(eventId)
        }.filter { apiResponse ->
            apiResponse.isNotEmpty()
        }.map { apiResponse ->
            buildProjectionsMap(apiResponse, listOf("players"), eventData, participantsData, oddsData)
        }.flatten().toMap()
    }

    fun getEventData(): Map<Int, Map<*, Any>> {
        return Events().getEventData(sport)
    }

    fun getParticipantsData(): Map<Int, Map<String, String>> {
        return Participants().getParticipantsData(sport)
    }

    fun getOddsData(): Map<Int, Map<String, Number>> {
        return Odds().getOddsData(sport)
    }

    fun getProjectionsFromEvent(eventId: Int): String {
        return DataCollector().getProjectionsFromEvent(sport, eventId)
    }
}