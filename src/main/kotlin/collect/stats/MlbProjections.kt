package collect.stats

import api.DataCollector
import collect.misc.Odds
import collect.misc.Weather

class MlbProjections {
    private val sport = "mlb"

    fun getMlbProjectionsData(): Map<Int, Map<String, Any?>> {
        val eventData = getEventData()
        val participantsData = getParticipantsData()
        val oddsData = getOddsData()
        val weatherData = getWeatherData()
        val eventIds = eventData.keys
        return eventIds.map { eventId ->
            getProjectionsFromEvent(eventId)
        }.filter { apiResponse ->
            apiResponse.isNotEmpty()
        }.map { apiResponse ->
            buildProjectionsMap(apiResponse, listOf("batters", "pitchers"), eventData, participantsData, oddsData, weatherData)
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

    fun getWeatherData(): Map<Int, Map<String, String>> {
        return Weather().getWeatherData(sport)
    }

    fun getProjectionsFromEvent(eventId: Int): String {
        return DataCollector().getProjectionsFromEvent(sport, eventId)
    }
}