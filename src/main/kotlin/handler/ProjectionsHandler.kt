package handler

import collect.stats.MlbProjections
import collect.stats.NbaProjections
import collect.stats.NflProjections
import collect.stats.NhlProjections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import util.AWS

class ProjectionsHandler {
    suspend fun handleRequest(input: Map<String, String>): Map<String, Any> {
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

    suspend fun getMlbProjectionsData(): Map<Int, Map<String, Any?>> {
        return MlbProjections().getMlbProjectionsData()
    }

    suspend fun getNflProjectionsData(): Map<Int, Map<String, Any?>> {
        return NflProjections().getNflProjectionsData()
    }

    suspend fun getNhlProjectionsData(): Map<Int, Map<String, Any?>> {
        return NhlProjections().getNhlProjectionsData()
    }

    suspend fun getNbaProjectionsData(): Map<Int, Map<String, Any?>> {
        return NbaProjections().getNbaProjectionsData()
    }

    suspend fun uploadToS3(fileName: String, objectToUpload: Any) {
        return withContext(Dispatchers.Default) { AWS().uploadToS3(fileName, objectToUpload) }
    }
}
