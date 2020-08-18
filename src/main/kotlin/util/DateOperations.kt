package util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun getDateTodayString(): String {
    val today = getDateToday()
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    return dateFormat.format(today.time)
}

fun getDateToday(): Calendar {
    return Calendar.getInstance()
}

fun getEasternTime(dateString: String, timeZone: String, timePattern: String): String {
    val cal = Calendar.getInstance()
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
    sdf.timeZone = TimeZone.getTimeZone(timeZone)
    cal.time = sdf.parse(dateString)
    val format: DateFormat = SimpleDateFormat(timePattern, Locale.US)
    format.timeZone = TimeZone.getTimeZone("EST")
    return format.format(cal.time)
}