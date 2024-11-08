import DirectionsServicePOST.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;

public class Main {
    public static void main(String[] args) {
        HttpClient orsClient = HttpClient.newHttpClient();

        try {
            DirectionsServicePOSTResult output = new DirectionsServicePOSTRequest(
                    "driving-car", //driver profile
                    36.37,    //latitude a
                    -94.2,    //longitude a
                    36.39,    //latitude b
                    -94.22,   //longitude b
                    0.6,      //Maximum fraction of the route that alternatives may share with the optimal route.
                    2,        //Target number of alternative routes to compute.
                    1.4,       //Maximum factor by which route weight may diverge from the optimal route.
                    true,      //include avg speed
                    true,      //include elevation
                    60.00, //max speed in mph
                    "mi",      //distance units
                    orsClient, //HttpClient object
                    "https://api.openrouteservice.org/v2/directions/", //server endpoint address
                    "5b3ce3597851110001cf6248a19910ccc5174a4d8ddf7e5be1f675d4"
            ).postDirections();
            System.out.println("Hallo welt");
            System.out.println(output.toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Hello world!");
    }
}