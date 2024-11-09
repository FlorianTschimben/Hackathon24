// MapApplication.java
import io.jenetics.jpx.WayPoint;

import java.io.IOException;
import java.util.List;
import javax.swing.SwingUtilities;

public class MapApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                String gpxFilePath = "UI/gpxgenerator_path.gpx";
                GPXHandler gpxHandler = new GPXHandler(gpxFilePath);
                List<WayPoint> waypoints = gpxHandler.getWayPoints();

                // Set to false to disable GPX path visualization
                boolean displayGPXPaths = false;

                MapViewer mapViewer = new MapViewer(waypoints, displayGPXPaths);
                mapViewer.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
