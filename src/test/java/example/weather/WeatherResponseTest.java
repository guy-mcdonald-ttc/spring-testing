package example.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.helper.FileLoader;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class WeatherResponseTest {

    @Test
    public void shouldDeserializeJson() throws Exception {
        var jsonResponse = FileLoader.read("classpath:weatherApiResponse.json");
        var expectedResponse = new WeatherResponse("raining", "a light drizzle");

        var parsedResponse = new ObjectMapper().readValue(jsonResponse, WeatherResponse.class);

        assertThat(parsedResponse, is(expectedResponse));
    }

    @Test
    public void shouldReturnCorrectSummaryWithMultipleWeatherConditions() {
        WeatherResponse response = new WeatherResponse();
        response.getWeather().add(new WeatherResponse.Weather("Clouds", "scattered clouds"));
        response.getWeather().add(new WeatherResponse.Weather("Rain", "light rain"));

        String expectedSummary = "Clouds: scattered clouds\nRain: light rain";
        String actualSummary = response.getSummary();

        assertEquals(expectedSummary, actualSummary);
    }

    @Test
    public void shouldReturnCorrectSummaryWithEmptyWeatherList() {
        WeatherResponse response = new WeatherResponse();
        response.getWeather().clear(); // Ensure the list is empty
        String summary = response.getSummary();
        assertEquals("", summary);
    }

    @Test
    public void shouldReturnCorrectSummaryWithNullWeatherList() throws Exception {
        WeatherResponse response = new WeatherResponse();
        Field weatherField = WeatherResponse.class.getDeclaredField("weather");
        weatherField.setAccessible(true);
        weatherField.set(response, null); // Directly set weather to null using reflection

        String summary = response.getSummary(); // Expecting empty string as per the current implementation
        assertEquals("", summary);
    }

    @Test
    public void shouldTestEqualsMethod() {
        WeatherResponse response1 = createWeatherResponse("Clouds", "scattered clouds");
        WeatherResponse response2 = createWeatherResponse("Clouds", "scattered clouds");
        WeatherResponse response3 = createWeatherResponse("Rain", "light rain");

        assertEquals(response1, response1); // Reflexivity
        assertEquals(response1, response2); // Symmetry
        assertEquals(response2, response1);
        assertNotEquals(response1, response3);
        assertNotEquals(response1, null);   // Null comparison
        assertNotEquals(response1, "test"); // Different type
    }


    @Test
    public void shouldTestHashCodeMethod() {
        WeatherResponse response1 = createWeatherResponse("Clouds", "scattered clouds");
        WeatherResponse response2 = createWeatherResponse("Clouds", "scattered clouds");
        WeatherResponse response3 = createWeatherResponse("Rain", "light rain");

        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }

    @Test
    public void shouldTestToStringMethod() {
        WeatherResponse response = createWeatherResponse("Clouds", "scattered clouds");

        // Using toString() of WeatherResponse and Weather.  Indirectly testing 'weather' field.
        String expected = "WeatherResponse{weather=[Weather{main='Clouds', description='scattered clouds'}]}";
        assertEquals(expected, response.toString());
    }

    private WeatherResponse createWeatherResponse(String main, String description) {
        WeatherResponse response = new WeatherResponse();
        response.getWeather().add(new WeatherResponse.Weather(main, description));
        return response;
    }
}