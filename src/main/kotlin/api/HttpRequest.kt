package api

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

fun makeHttpRequest(url: String): String {
    val response = StringBuilder()
    try {
        val obj = URL(url)
        val con: HttpURLConnection = obj.openConnection() as HttpURLConnection
        con.setRequestProperty("User-Agent", "Mozilla/5.0")
        val `in` = BufferedReader(InputStreamReader(con.inputStream))
        var inputLine: String?
        while (`in`.readLine().also { inputLine = it } != null) {
            response.append(inputLine)
        }
        `in`.close()
    } catch (e: IOException) {
        return e.toString()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return response.toString()
}
