public class GPXTester {
    public static void main(String[] ars){
        String[] arr = {"Neulandstraße 7, Neumarkt, Italien", "Sorrentstraße 20, Bozen, Italien"};
        //System.out.println(OpenRouteServiceGPX.getOpenRouteGPX(arr));
        RouteTimeFinder.routeTime("Neulandstraße 7, Neumarkt, Italien", "Sorrentstraße 20, Bozen, Italien");
    }
}
