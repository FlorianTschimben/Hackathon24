import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Route;
import io.jenetics.jpx.WayPoint;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GPXParser {
    private final Path filePath;

    public GPXParser(Path filePath) {
        this.filePath = filePath;
    }

    public List<List<WayPoint>> parseRoutes() throws IOException {
        GPX gpx = GPX.read(filePath);
        List<List<WayPoint>> routes = new ArrayList<>();

        for (Route route : gpx.getRoutes()) {
            List<WayPoint> routeWaypoints = new ArrayList<>(route.getPoints());
            routes.add(routeWaypoints);
        }

        return routes;
    }
}