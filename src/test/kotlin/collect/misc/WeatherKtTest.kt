package collect.misc

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Test


import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.nio.file.Files
import java.nio.file.Paths

class WeatherKtTest {
    private val fakeWeatherResponse: String = String(Files.readAllBytes(Paths.get("src/main/resources/weatherResponse.json")))

    private val weather = spyk<Weather>()

    @BeforeEach
    fun setUp() {
        every { weather.callWeather("nfl") } returns fakeWeatherResponse
    }

    @Test
    fun shouldGetWeatherData() {
        val result: Map<Int, Map<String, String>> = weather.getWeatherData("nfl")
        assertEquals("Mostly Sunny", result[2246084]?.get("forecast"))
        assertEquals("85°, 0% precip", result[2246084]?.get("details"))
    }
}