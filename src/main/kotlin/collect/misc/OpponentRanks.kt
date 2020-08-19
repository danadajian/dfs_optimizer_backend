package collect.misc

import org.json.JSONArray
import org.json.JSONObject
import org.json.XML

fun getOpponentRanks(sport: String, dataGetter: (sport: String) -> String): Map<String, Map<String, Int>> {
    val scrapedResponse: String = dataGetter(sport)
    return if (scrapedResponse.isEmpty()) mapOf() else {
        val startIndex = scrapedResponse.indexOf("<tbody>")
        val endIndex = scrapedResponse.indexOf("</tbody>") + "</tbody>".length
        val responseSubstring = scrapedResponse.substring(startIndex, endIndex)
        val adjustedResponse = responseSubstring.replace("</td><tr>", "</td></tr>")
        val rankingsJson = XML.toJSONObject(adjustedResponse).getJSONObject("tbody")
        val rankingsArray = rankingsJson.getJSONArray("tr")
        rankingsArray.map {
            it as JSONObject
            val teamArray = it.getJSONArray("td")
            val teamName = teamArray.getJSONObject(0).getJSONObject("a").getString("content")
            val qbRank = getRank(teamArray, 1)
            val rbRank = getRank(teamArray, 3)
            val wrRank = getRank(teamArray, 5)
            val teRank = getRank(teamArray, 7)
            val kRank = getRank(teamArray, 9)
            val dstRank = getRank(teamArray, 11)
            teamName to mapOf(
                    "QB" to 33 - qbRank,
                    "RB" to 33 - rbRank,
                    "WR" to 33 - wrRank,
                    "TE" to 33 - teRank,
                    "K" to 33 - kRank,
                    "D/ST" to 33 - dstRank
            )
        }.toMap()
    }
}

private fun getRank(teamArray: JSONArray, index: Int): Int {
    return if (teamArray.getJSONObject(index)["span"] is JSONObject)
        teamArray.getJSONObject(index).getJSONObject("span").getInt("content")
    else teamArray.getJSONObject(index).getInt("span")
}