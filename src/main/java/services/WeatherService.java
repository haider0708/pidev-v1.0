package services;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class WeatherService {
    private static final String API_KEY = "a1a3228144677aa3eafa968a72a891e2";

    // Function to get weather by city, using coordinates for Tunis as an example.
    public String getWeather(String city) {
        String lat = "36.8065"; // Latitude for Tunis
        String lon = "10.1815"; // Longitude for Tunis
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s", lat, lon, API_KEY);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            return EntityUtils.toString(client.execute(request).getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
