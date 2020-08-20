package handler

import collect.misc.OpponentRanks

class OpponentRanksHandler {
    fun handleRequest(input: Map<String, String>): Map<String, Map<String, Int>> {
        val sport = input.getValue("sport")
        return getOpponentRanksData(sport)
    }

    fun getOpponentRanksData(sport: String): Map<String, Map<String, Int>> {
        return OpponentRanks().getOpponentRanks(sport)
    }
}