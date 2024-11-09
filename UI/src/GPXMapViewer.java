import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
import io.jenetics.jpx.WayPoint;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GPXMapViewer {
    private static final String GPX_FILE_PATH = "UI/gpxgenerator_path.gpx"; // Update to your actual file path

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                JXMapViewer mapViewer = initializeMap();
                List<List<GeoPosition>> routePositions = loadRoutes();

                // Set initial location to the first waypoint of the first route if available
                if (!routePositions.isEmpty() && !routePositions.get(0).isEmpty()) {
                    mapViewer.setAddressLocation(routePositions.get(0).get(0)); // Center on first waypoint
                    mapViewer.setZoom(5); // Set initial zoom level as desired
                }

                GPXWaypointViewer waypointViewer = new GPXWaypointViewer(mapViewer);
                waypointViewer.plotRoutes(routePositions);

                JFrame frame = new JFrame("GPX Waypoint Viewer - Multiple Routes");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(mapViewer);
                frame.setSize(800, 600);
                frame.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static JXMapViewer initializeMap() {
        JXMapViewer mapViewer = new JXMapViewer();

        TileFactoryInfo info = new TileFactoryInfo(1, 17, 17, 256, true, true,
                "https://tile.openstreetmap.org", "{z}", "{x}", "{y}.png") {
            @Override
            public String getTileUrl(int x, int y, int zoom) {
                zoom = getTotalMapZoom() - zoom;
                return this.baseURL + "/" + zoom + "/" + x + "/" + y + ".png";
            }
        };

        mapViewer.setTileFactory(new DefaultTileFactory(info));
        return mapViewer;
    }

    private static List<List<GeoPosition>> loadRoutes() throws Exception {
        GPXParser parser = new GPXParser(Paths.get(GPX_FILE_PATH));
        List<List<WayPoint>> routes = parser.parseRoutes();

        List<List<GeoPosition>> routePositions = new ArrayList<>();
        for (List<WayPoint> waypoints : routes) {
            List<GeoPosition> positions = new ArrayList<>();
            for (WayPoint waypoint : waypoints) {
                positions.add(new GeoPosition(
                        waypoint.getLatitude().doubleValue(),
                        waypoint.getLongitude().doubleValue()
                ));
            }
            routePositions.add(positions);
        }
        return routePositions;
    }
}