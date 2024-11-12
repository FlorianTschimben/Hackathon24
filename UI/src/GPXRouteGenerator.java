import io.jenetics.jpx.GPX;
import io.jenetics.jpx.WayPoint;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GPXRouteGenerator {
    private static final String GPX_FILE_PATH = "UI/gpxgenerator_path.gpx"; // Update the file path if necessary
    private static final String GEOCODING_URL = "https://api.openrouteservice.org/geocode/search"; // Replace with actual geocoding URL
    private static final String API_KEY = "5b3ce3597851110001cf6248a19910ccc5174a4d8ddf7e5be1f675d4"; // Replace with your API key

    public static void generateGPXRoutes(Connection connection) throws SQLException, IOException {
        List<Route> routes = getRoutesFromDatabase(connection);
        List<WayPoint[]> waypoints = new ArrayList<>();

        for (Route route : routes) {
            String startCoords = geocodeAddress(route.getStartAddress());
            String endCoords = geocodeAddress(route.getEndAddress());

            if (startCoords != null && endCoords != null) {
                WayPoint startPoint = createWaypoint(startCoords);
                WayPoint endPoint = createWaypoint(endCoords);
                waypoints.add(new WayPoint[]{startPoint, endPoint});
            }
        }

        writeWaypointsToGPX(waypoints);
    }

    private static List<Route> getRoutesFromDatabase(Connection connection) throws SQLException {
        ResultSet rs = DatabaseManager.getEveryTransport(connection);
        List<Route> routes = new ArrayList<>();

        while (rs.next()) {
            // Include both city and country in the address format for accurate geocoding
            String startAddress = rs.getString("tvonstrasse") + ", " + rs.getString("tvonort") + ", South Tyrol, Italy";
            String endAddress = rs.getString("tbisstrasse") + ", " + rs.getString("tbisort") + ", South Tyrol, Italy";
            routes.add(new Route(startAddress, endAddress));
        }

        return routes;
    }

    public static String geocodeAddress(String address) throws IOException {
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
        String urlString = GEOCODING_URL + "?api_key=" + API_KEY + "&text=" + encodedAddress;
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            StringBuilder content = new StringBuilder();
            try (var in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }

            JSONObject jsonResponse = new JSONObject(content.toString());
            JSONArray features = jsonResponse.getJSONArray("features");

            if (features.length() > 0) {
                JSONObject geometry = features.getJSONObject(0).getJSONObject("geometry");
                JSONArray coordinates = geometry.getJSONArray("coordinates");
                double longitude = coordinates.getDouble(0);
                double latitude = coordinates.getDouble(1);
                System.out.println("Geocoded address: " + address + " to coordinates: " + longitude + ":" + latitude);
                return longitude + ":" + latitude;
            }
        } else {
            System.err.println("Failed to geocode address: " + address);
        }

        connection.disconnect();
        return null;
    }

    private static WayPoint createWaypoint(String coords) {
        String[] parts = coords.split(":");
        double longitude = Double.parseDouble(parts[0]);
        double latitude = Double.parseDouble(parts[1]);
        return WayPoint.of(latitude, longitude);
    }

    private static void writeWaypointsToGPX(List<WayPoint[]> waypoints) throws IOException {
        GPX.Builder gpxBuilder = GPX.builder();

        // Each waypoint pair is treated as a separate track for each route
        for (WayPoint[] waypointPair : waypoints) {
            if (waypointPair != null && waypointPair.length == 2 && waypointPair[0] != null && waypointPair[1] != null) {
                gpxBuilder.addTrack(track -> track.addSegment(segment -> {
                    // Each segment has its unique start and end point
                    segment.addPoint(waypointPair[0]);
                    segment.addPoint(waypointPair[1]);
                }));
            } else {
                System.err.println("Invalid waypoint pair encountered. Skipping entry.");
            }
        }

        Path filePath = Paths.get(GPX_FILE_PATH);
        GPX gpx = gpxBuilder.build();
        GPX.write(gpx, filePath);

        System.out.println("GPX file written with " + waypoints.size() + " routes.");
    }


    private static class Route {
        private final String startAddress;
        private final String endAddress;

        public Route(String startAddress, String endAddress) {
            this.startAddress = startAddress;
            this.endAddress = endAddress;
        }

        public String getStartAddress() {
            return startAddress;
        }

        public String getEndAddress() {
            return endAddress;
        }
    }
}
