import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenRouteServiceGPX {

    private static final String API_KEY = "5b3ce3597851110001cf6248a19910ccc5174a4d8ddf7e5be1f675d4"; // Replace with your OpenRouteService API key
    private static final String DIRECTIONS_URL = "https://api.openrouteservice.org/v2/directions/";

    public static String getOpenRouteGPX(String[] addresses) {
        try {
            // Define the transportation profile (e.g., "driving-car", "cycling-regular", "foot-walking")
            String profile = "driving-car";

            // Define the waypoints (latitude, longitude)
            double[][] waypoints = new double[addresses.length][2];
            int index = 0;
            for(String address:addresses) {
                String geoCode = OpenRouteServiceGeocoding.geocodeAddress(address);
                waypoints[index][0] = Double.parseDouble(geoCode.substring(geoCode.indexOf(':') + 1));
                waypoints[index][1] = Double.parseDouble(geoCode.substring(0, geoCode.indexOf(':') - 1));
            }

            // Get the GPX path
            String gpxPath = getGPXPath(profile, waypoints);

            // Output the GPX response
            return gpxPath;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getGPXPath(String profile, double[][] waypoints) throws IOException {
        // Construct the URL for the request
        StringBuilder urlBuilder = new StringBuilder(DIRECTIONS_URL + profile + "/");
        urlBuilder.append("?api_key=").append(API_KEY);

        // Add waypoints to the URL query string
        for (double[] waypoint : waypoints) {
            urlBuilder.append("&coordinates=").append(waypoint[0]).append(",").append(waypoint[1]);
        }

        String url = urlBuilder.toString();

        // Create a URL object from the final string
        URL requestUrl = new URL(url);

        // Open a connection to the URL
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        // Get the HTTP response code
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to get response from OpenRouteService API. HTTP code: " + responseCode);
        }

        // Read the response from the input stream
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder responseStringBuilder = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            responseStringBuilder.append(inputLine);
        }
        in.close();

        // Parse the JSON response
        JSONObject jsonResponse = new JSONObject(responseStringBuilder.toString());

        // Extract the GPX data from the response
        if (jsonResponse.has("routes")) {
            JSONArray routes = jsonResponse.getJSONArray("routes");
            if (routes.length() > 0) {
                JSONObject firstRoute = routes.getJSONObject(0);
                if (firstRoute.has("gpx")) {
                    return firstRoute.getString("gpx"); // Extract the GPX data
                }
            }
        }

        return "GPX data not found";
    }
}
