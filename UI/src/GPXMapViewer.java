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

    private static final String GPX_FILE_PATH = "UI/gpxgenerator_path.gpx";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                JXMapViewer mapViewer = initializeMap();
                List<GeoPosition> positions = loadGPXGeoPositions();

                GPXWaypointViewer waypointViewer = new GPXWaypointViewer(mapViewer);
                waypointViewer.plotWaypoints(positions);

                JFrame frame = new JFrame("GPX Waypoint Viewer");
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
                "https://tile.openstreetmap.org", "{z}", "{x}", "{y}") {
            @Override
            public String getTileUrl(int x, int y, int zoom) {
                zoom = getTotalMapZoom() - zoom;
                return this.baseURL + "/" + zoom + "/" + x + "/" + y + ".png";
            }
        };

        mapViewer.setTileFactory(new DefaultTileFactory(info));
        mapViewer.setZoom(5);
        mapViewer.setAddressLocation(new GeoPosition(50.0, 8.0));
        return mapViewer;
    }

    private static List<GeoPosition> loadGPXGeoPositions() throws Exception {
        GPXParser parser = new GPXParser(Paths.get(GPX_FILE_PATH));
        List<WayPoint> waypoints = parser.parseWaypoints();

        List<GeoPosition> positions = new ArrayList<>();
        for (WayPoint waypoint : waypoints) {
            positions.add(new GeoPosition(
                    waypoint.getLatitude().doubleValue(),
                    waypoint.getLongitude().doubleValue()
            ));
        }
        return positions;
    }
}