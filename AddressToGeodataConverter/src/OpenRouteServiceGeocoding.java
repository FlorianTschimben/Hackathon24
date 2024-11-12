import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class OpenRouteServiceGeocoding {

    private static final String API_KEY = "5b3ce3597851110001cf6248a19910ccc5174a4d8ddf7e5be1f675d4";  // Replace with your ORS API key
    private static final String GEOCODING_URL = "https://api.openrouteservice.org/geocode/search";

    public static void main(String[] args) {
        String address = "Sorrentostraße 20, Bozen BZ";
        try {
            String coordinates = geocodeAddress(address);
            if (coordinates != null) {
                System.out.println("Coordinates for '" + address + "': " + coordinates.toString());
            } else {
                System.out.println("No coordinates found for the address.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param address
     * @return String longitude:latitude
     * @throws IOException
     */
    public static String geocodeAddress(String address) throws IOException {
        // Encode the address to be URL-friendly
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
        String urlString = GEOCODING_URL + "?api_key=" + API_KEY + "&text=" + encodedAddress;
        URL url = new URL(urlString);

        // Create an HttpURLConnection instance
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        // Check for successful response code (HTTP 200)
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Read the response from the input stream
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            // Close the BufferedReader
            in.close();

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(content.toString());
            JSONArray features = jsonResponse.getJSONArray("features");

            // Check if there are any results
            if (features.length() > 0) {
                // Extract the first result's coordinates
                JSONObject geometry = features.getJSONObject(0).getJSONObject("geometry");
                JSONArray coordinates = geometry.getJSONArray("coordinates");

                // Longitude is first, latitude is second
                double longitude = coordinates.getDouble(0);
                double latitude = coordinates.getDouble(1);
                System.out.println(longitude + ":" + latitude);
                return longitude + ":" + latitude;
            }
        } else {
            System.out.println("Request failed with status code: " + connection.getResponseCode());
        }

        // Disconnect the connection
        connection.disconnect();
        return null;
    }
}
