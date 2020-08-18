package util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DateOperationsKtTest {

    @Test
    fun shouldFormatTodaysDateStringCorrectly() {
        val result: String = getDateTodayString()
        assertEquals(10, result.length)
    }

    @Test
    fun shouldGetEasternTime() {
        val result: String = getEasternTime("2019-11-17T18:00:00", "utc", "EEE h:mma z")
        assertEquals("Sun 1:00PM EST", result)
    }
}