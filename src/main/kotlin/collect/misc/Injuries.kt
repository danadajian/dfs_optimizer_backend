package collect.misc

import api.DataCollector
import org.json.JSONArray
import org.json.JSONObject
import org.json.XML

class Injuries {
    fun getNFLInjuryData(): Map<String, String> {
        val scrapedResponse: String = getInjuryData("nfl")
        val startIndex = scrapedResponse.indexOf("<section class=\"Card\">")
        val endIndex = scrapedResponse.indexOf("</section></div></section>") + "</section></div></section>".length
        val responseSubstring = scrapedResponse.substring(startIndex, endIndex)
        val teamArray = XML.toJSONObject(responseSubstring).getJSONObject("section").getJSONObject("div")
                .getJSONObject("section").getJSONArray("section")
        return teamArray.map { it ->
            val teamObject = (it as JSONObject).getJSONArray("div").getJSONObject(1)
                    .getJSONObject("div").getJSONArray("div").getJSONObject(1).getJSONObject("table")
                    .getJSONObject("tbody")["tr"]
            val playerArray = if (teamObject is JSONArray) teamObject else JSONArray("[$teamObject]")
            playerArray.map {
                val playerName = (it as JSONObject).getJSONArray("td").getJSONObject(0)
                        .getJSONObject("a").getString("content")
                val status = it.getJSONArray("td").getJSONObject(3)
                        .getJSONObject("span").getString("content")
                playerName to status
            }
        }.flatten().toMap()
    }

    fun getStandardInjuryData(sport: String): Map<String, String> {
        val scrapedResponse: String = getInjuryData(sport)
        val startIndex = scrapedResponse.indexOf("<table")
        val endIndex = scrapedResponse.indexOf("</table>") + "</table>".length
        val responseSubstring = scrapedResponse.substring(startIndex, endIndex)
        val playerArray = XML.toJSONObject(responseSubstring).getJSONObject("table").getJSONArray("tr")
        val playerTracker = mutableListOf<String>()
        return playerArray.filter {
            it as JSONObject
            it["td"] is JSONArray && it.getJSONArray("td")[0] is JSONObject
        }.map {
            it as JSONObject
            val playerName = it.getJSONArray("td").getJSONObject(0).getJSONObject("a").getString("content")
            val status = it.getJSONArray("td").getString(1)
            if (!playerTracker.contains(playerName)) {
                playerTracker.add(playerName)
                playerName to status
            } else null
        }.mapNotNull { p -> p?.second?.let { Pair(p.first, it) } }.toMap()
    }

    fun getInjuryData(sport: String): String {
        return DataCollector().getInjuryData(sport)
    }
}