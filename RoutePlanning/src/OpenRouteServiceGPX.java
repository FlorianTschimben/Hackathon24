import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenRouteServiceGPX {

    private static final String API_KEY = "5b3ce3597851110001cf6248a19910ccc5174a4d8ddf7e5be1f675d4"; // Replace with your OpenRouteService API key
    private static final String DIRECTIONS_URL = "https://api.openrouteservice.org/v2/directions/";

    public static String getOpenRouteGPX(String[] addresses) {
        try {
            String profile = "driving-car";
            double[][] waypoints = new double[addresses.length][2];
            int index = 0;
            for (String address : addresses) {
                String geoCode = OpenRouteServiceGeocoding.geocodeAddress(address);
                waypoints[index][0] = Double.parseDouble(geoCode.substring(geoCode.indexOf(':') + 1));
                waypoints[index][1] = Double.parseDouble(geoCode.substring(0, geoCode.indexOf(':') - 1));
            }

            String gpxPath = getGPXPath(profile, waypoints);
            return gpxPath;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getGPXPath(String profile, double[][] waypoints) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(DIRECTIONS_URL + profile + "/");
        urlBuilder.append("?api_key=").append(API_KEY);

        for (double[] waypoint : waypoints) {
            urlBuilder.append("&coordinates=").append(waypoint[0]).append(",").append(waypoint[1]);
        }

        String url = urlBuilder.toString();
        URL requestUrl = new URL(url);

        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to get response from OpenRouteService API. HTTP code: " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder responseStringBuilder = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            responseStringBuilder.append(inputLine);
        }
        in.close();

        JSONObject jsonResponse = new JSONObject(responseStringBuilder.toString());
        if (jsonResponse.has("routes")) {
            JSONArray routes = jsonResponse.getJSONArray("routes");
            if (routes.length() > 0) {
                JSONObject firstRoute = routes.getJSONObject(0);
                if (firstRoute.has("gpx")) {
                    return firstRoute.getString("gpx");
                }
            }
        }

        return "GPX data not found";
    }

    public static String generateIntermediateGPXPointsAsGPX(double[] startPoint, double[] endPoint, int numberOfPoints) {
        StringBuilder gpxBuilder = new StringBuilder();

        gpxBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        gpxBuilder.append("<gpx version=\"1.1\" creator=\"OpenRouteServiceGPX\">\n");
        gpxBuilder.append("  <trk>\n");
        gpxBuilder.append("    <name>Intermediate Points</name>\n");
        gpxBuilder.append("    <trkseg>\n");

        double latIncrement = (endPoint[0] - startPoint[0]) / (numberOfPoints - 1);
        double lonIncrement = (endPoint[1] - startPoint[1]) / (numberOfPoints - 1);

        for (int i = 0; i < numberOfPoints; i++) {
            double latitude = startPoint[0] + (i * latIncrement);
            double longitude = startPoint[1] + (i * lonIncrement);

            gpxBuilder.append("      <trkpt lat=\"").append(latitude).append("\" lon=\"").append(longitude).append("\">\n");
            gpxBuilder.append("      </trkpt>\n");
        }

        gpxBuilder.append("    </trkseg>\n");
        gpxBuilder.append("  </trk>\n");
        gpxBuilder.append("</gpx>");

        return gpxBuilder.toString();
    }

    public static void main(String[] args) {
        double[] startPoint = {52.5200, 13.4050}; // Berlin
        double[] endPoint = {48.8566, 2.3522}; // Paris

        String gpxData = generateIntermediateGPXPointsAsGPX(startPoint, endPoint, 100);

        System.out.println(gpxData);
    }
}
