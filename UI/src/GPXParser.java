import io.jenetics.jpx.GPX;
import io.jenetics.jpx.WayPoint;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class GPXParser {

    private final Path filePath;

    public GPXParser(Path filePath) {
        this.filePath = filePath;
    }

    public List<WayPoint> parseWaypoints() throws IOException {
        GPX gpx = GPX.read(filePath);
        List<WayPoint> waypoints = gpx.getWayPoints();

        return waypoints;
    }
}
