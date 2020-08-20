package handler

import collect.misc.Injuries

class InjuryHandler {
    fun handleRequest(input: Map<String, String>): Map<String, String> {
        val sport = input.getValue("sport")
        return if (sport == "nfl") getNFLInjuryData() else getStandardInjuryData(sport)
    }

    fun getNFLInjuryData(): Map<String, String> {
        return Injuries().getNFLInjuryData()
    }

    fun getStandardInjuryData(sport: String): Map<String, String> {
        return Injuries().getStandardInjuryData(sport)
    }
}