package example.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.helper.FileLoader;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Collections;
import java.util.Objects;
import example.weather.WeatherResponse.Weather;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class WeatherResponseTest {

    @Test
    public void shouldDeserializeJson() throws Exception {
        var jsonResponse = FileLoader.read("classpath:weatherApiResponse.json");
        var expectedResponse = new WeatherResponse("raining", "a light drizzle");

        var parsedResponse = new ObjectMapper().readValue(jsonResponse, WeatherResponse.class);

        assertThat(parsedResponse, is(expectedResponse));
    }

    @Test
    public void testHashCodeWithEmptyWeatherList() {
        WeatherResponse weatherResponse = new WeatherResponse(Collections.emptyList());
        int hashCode = weatherResponse.hashCode();
        assertEquals(1, hashCode);
    }

    @Test
    public void testHashCodeWithSingleWeatherObject() {
        List<Weather> weatherList = List.of(new Weather("raining", "description"));
        WeatherResponse weatherResponse = new WeatherResponse(weatherList);
        int hashCode = weatherResponse.hashCode();
        assertEquals(Objects.hash(weatherList), hashCode);
    }

    @Test
    public void testHashCodeWithMultipleWeatherObjects() {
        List<Weather> weatherList = List.of(new Weather("raining", "description1"), new Weather("cloudy", "description2"));
        WeatherResponse weatherResponse = new WeatherResponse(weatherList);
        int hashCode = weatherResponse.hashCode();
        assertEquals(Objects.hash(weatherList), hashCode);
    }

    @Test
    public void testHashCodeWithEqualWeatherResponses() {
        List<Weather> weatherList = List.of(new Weather("raining", "description"));
        WeatherResponse weatherResponse1 = new WeatherResponse(weatherList);
        WeatherResponse weatherResponse2 = new WeatherResponse(weatherList);
        int hashCode1 = weatherResponse1.hashCode();
        int hashCode2 = weatherResponse2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToStringWithEmptyWeatherList() {
        WeatherResponse weatherResponse = new WeatherResponse(Collections.emptyList());
        String toString = weatherResponse.toString();
        assertThat(toString, is("WeatherResponse{weather=[]}"));
    }

    @Test
    public void testToStringWithSingleWeatherObject() {
        List<Weather> weatherList = List.of(new Weather("raining", "description"));
        WeatherResponse weatherResponse = new WeatherResponse(weatherList);
        String toString = weatherResponse.toString();
        assertThat(toString, is("WeatherResponse{weather=[Weather{main='raining', description='description'}]}"));
    }

    @Test
    public void testToStringWithMultipleWeatherObjects() {
        List<Weather> weatherList = List.of(new Weather("raining", "description1"), new Weather("cloudy", "description2"));
        WeatherResponse weatherResponse = new WeatherResponse(weatherList);
        String toString = weatherResponse.toString();
        assertThat(toString, is("WeatherResponse{weather=[Weather{main='raining', description='description1'}, Weather{main='cloudy', description='description2'}]}"));
    }
}