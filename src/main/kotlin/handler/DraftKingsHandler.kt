package handler

import collect.dfs.DraftKingsContests

class DraftKingsHandler {
    fun handleRequest(input: Map<String, String>): List<Map<String, Any>> {
        val sport = input.getValue("sport")
        return getDraftKingsContestData(sport)
    }

    fun getDraftKingsContestData(sport: String): List<Map<String, Any>> {
        return DraftKingsContests().getDraftKingsContestData(sport)
    }
}