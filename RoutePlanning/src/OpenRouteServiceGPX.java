import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenRouteServiceGPX {

    private static final String API_KEY = "5b3ce3597851110001cf6248a19910ccc5174a4d8ddf7e5be1f675d4"; // Replace with your OpenRouteService API key
    private static final String DIRECTIONS_URL = "https://api.openrouteservice.org/v2/directions/{profile}?api_key={api_key}&start={start}&end={end}";

    public static String getGeoJSON(String start, String end) {
        try {
            // Define profile, start, and end coordinates
            String profile = "driving-car";
                // Example coordinates for the end

            // Replace placeholders in URL with actual values
            String urlString = DIRECTIONS_URL
                    .replace("{profile}", profile)
                    .replace("{api_key}", API_KEY)
                    .replace("{start}", start)
                    .replace("{end}", end);

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set up request headers
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8");

            // Check response code
            int status = connection.getResponseCode();
            System.out.println("Status: " + status);

            if (status == 200) {
                // Read and print response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                System.out.println("Body: " + response.toString());
                connection.disconnect();

                return response.toString();
            } else {
                // Read error response if status is not 200 OK
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    errorResponse.append(line);
                }
                reader.close();

                System.out.println("Error: " + errorResponse.toString());
                connection.disconnect();
                return errorResponse.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
