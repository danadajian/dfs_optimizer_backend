package handler

import collect.dfs.FanduelContests
import util.AWS

class FanduelHandler {
    fun handleRequest(input: Map<String, String>): List<Map<String, Any>> {
        val date = input.getValue("date")
        val invocationType = input.getOrDefault("invocationType", "web")
        val fanduelData = getFanduelContestData(date)
        return if (invocationType == "pipeline") {
            uploadToS3("fanduelData.json", fanduelData)
            listOf()
        } else fanduelData
    }

    fun getFanduelContestData(date: String): List<Map<String, Any>> {
        return FanduelContests().getFanduelContestData(date)
    }

    fun uploadToS3(fileName: String, objectToUpload: Any) {
        return AWS().uploadToS3(fileName, objectToUpload)
    }
}