package collect.stats

import api.DataCollector
import collect.misc.Odds
import kotlinx.coroutines.*

class NhlProjections {
    private val sport = "nhl"

    suspend fun getNhlProjectionsData(): Map<Int, Map<String, Any?>> {
        val eventData = withContext(Dispatchers.Default) { getEventData() }
        val participantsData = withContext(Dispatchers.Default) { getParticipantsData() }
        val oddsData = withContext(Dispatchers.Default) { getOddsData() }
        val eventIds = eventData.keys
        return eventIds.map { eventId ->
            GlobalScope.async { getProjectionsFromEvent(eventId) }
        }.awaitAll().filter { apiResponse ->
            apiResponse.isNotEmpty()
        }.map { apiResponse ->
            buildProjectionsMap(apiResponse, listOf("skaters", "goalies"), eventData, participantsData, oddsData)
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