package collect.misc

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.nio.file.Files
import java.nio.file.Paths

class WeatherKtTest {
    private val fakeWeatherResponse: String = String(Files.readAllBytes(Paths.get("src/main/resources/weatherResponse.json")))

    @Test
    fun shouldGetWeatherData() {
        val result: Map<Int, Map<String, String>> = getWeatherData("nfl") { fakeWeatherResponse }
        assertEquals("Mostly Sunny", result[2246084]?.get("forecast"))
        assertEquals("85°, 0% precip", result[2246084]?.get("details"))
    }
}