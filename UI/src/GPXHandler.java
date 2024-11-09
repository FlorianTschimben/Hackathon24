// GPXHandler.java
import io.jenetics.jpx.GPX;
import io.jenetics.jpx.WayPoint;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class GPXHandler {
    private GPX gpx;

    public GPXHandler(String filePath) throws IOException {
        loadGPX(filePath);
    }

    private void loadGPX(String filePath) throws IOException {
        gpx = GPX.read(Path.of(filePath));
    }

    public List<WayPoint> getWayPoints() {
        return gpx.tracks()
                .flatMap(track -> track.segments())
                .flatMap(segment -> segment.points())
                .toList();
    }
}