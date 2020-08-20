package handler

import collect.stats.*
import util.AWS

class ProjectionsHandler {
    fun handleRequest(input: Map<String, String>): Map<String, Any?> {
        val sport = input.getValue("sport")
        val invocationType = input.getOrDefault("invocationType", "web")
        val projections: Map<Int, Map<String, Any?>> =
                when (sport) {
                    "mlb" -> getMlbProjectionsData()
                    "nfl" -> getNflProjectionsData()
                    "nhl" -> getNhlProjectionsData()
                    "nba" -> getNbaProjectionsData()
                    else -> mapOf()
                }
        return if (invocationType == "pipeline") {
            uploadToS3("${sport}ProjectionsData.json", projections)
            mapOf("sport" to sport)
        } else mapOf("body" to projections)
    }

    fun getMlbProjectionsData(): Map<Int, Map<String, Any?>> {
        return MlbProjections().getMlbProjectionsData()
    }

    fun getNflProjectionsData(): Map<Int, Map<String, Any?>> {
        return NflProjections().getNflProjectionsData()
    }

    fun getNhlProjectionsData(): Map<Int, Map<String, Any?>> {
        return NhlProjections().getNhlProjectionsData()
    }

    fun getNbaProjectionsData(): Map<Int, Map<String, Any?>> {
        return NbaProjections().getNbaProjectionsData()
    }

    fun uploadToS3(fileName: String, objectToUpload: Any) {
        return AWS().uploadToS3(fileName, objectToUpload)
    }
}
