package collect.dfs

import api.DataCollector
import org.json.JSONObject
import org.json.XML

class FanduelContests {
    fun getFanduelContestData(date: String): List<Map<String, Any>> {
        return getValidFanduelContests(date).filter {
            it.has("player")
        }.map { it ->
            mapOf(
                    "sport" to it.getString("sport"),
                    "contest" to it.getJSONObject("game").getString("label"),
                    "players" to it.getJSONArray("player").map {
                        it as JSONObject
                        mapOf(
                                *(if (it["statsid"].toString().isNotEmpty())
                                    arrayOf("playerId" to it.getInt("statsid")) else arrayOf()),
                                "name" to it.getString("name"),
                                "team" to it.getString("team"),
                                "position" to it.getString("position"),
                                "salary" to it.getInt("salary")
                        )
                    }.toList()
            )
        }
    }

    fun getValidFanduelContests(date: String): List<JSONObject> {
        val apiResponse: String = getFanduelData(date)
        val xmlResponse = XML.toJSONObject(apiResponse)
        return if (xmlResponse.has("data")) {
            xmlResponse.getJSONObject("data").getJSONArray("fixturelist").map { it as JSONObject }
        } else listOf()
    }

    fun getFanduelData(date: String): String {
        return DataCollector().getFanduelData(date)
    }
}