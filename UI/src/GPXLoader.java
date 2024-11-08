import io.jenetics.jpx.GPX;
import io.jenetics.jpx.WayPoint;
import org.jxmapviewer.viewer.GeoPosition;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GPXLoader {
    public static List<GeoPosition> loadGPX(String filePath) throws IOException {
        List<GeoPosition> positions = new ArrayList<>();

        GPX gpx = GPX.read(Paths.get(filePath));
        gpx.tracks().forEach(track -> track.segments().forEach(segment ->
                segment.points().forEach(point ->
                        positions.add(new GeoPosition(point.getLatitude().doubleValue(), point.getLongitude().doubleValue()))
                )
        ));

        return positions;
    }
}
