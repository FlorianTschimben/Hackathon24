import DirectionsServicePOST.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;


public class RouteTimeFinder {
    public static double routeTime(String start, String destination) {
        HttpClient orsClient = HttpClient.newHttpClient();
        try {
            String geoCode1 = OpenRouteServiceGeocoding.geocodeAddress(start);
            String geoCode2 = OpenRouteServiceGeocoding.geocodeAddress(destination);

        try {
            DirectionsServicePOSTResult output = new DirectionsServicePOSTRequest(
                    "driving-car", //driver profile
                    Double.parseDouble(geoCode1.substring(geoCode1.indexOf(':') + 1)),    //latitude a
                    Double.parseDouble(geoCode1.substring(0, geoCode1.indexOf(':') - 1)),    //longitude a
                    Double.parseDouble(geoCode2.substring(geoCode1.indexOf(':') + 1)),    //latitude b
                    Double.parseDouble(geoCode2.substring(0, geoCode1.indexOf(':') - 1)),   //longitude b
                    0.6,      //Maximum fraction of the route that alternatives may share with the optimal route.
                    2,        //Target number of alternative routes to compute.
                    1.4,       //Maximum factor by which route weight may diverge from the optimal route.
                    true,      //include avg speed
                    true,      //include elevation
                    200.00, //max speed in mph
                    "mi",      //distance units
                    orsClient, //HttpClient object
                    "https://api.openrouteservice.org/v2/directions/", //server endpoint address
                    "5b3ce3597851110001cf6248a19910ccc5174a4d8ddf7e5be1f675d4"
            ).postDirections();
            System.out.println("Hallo welt");
            System.out.println(output.toString());
            JSONObject jsonResponseObj = new JSONObject(output);
            JSONArray routes = jsonResponseObj.getJSONArray("routes");
            JSONObject firstRoute = routes.getJSONObject(0);
            JSONObject summary = firstRoute.getJSONObject("summary");
            return summary.getDouble("duration");



        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}