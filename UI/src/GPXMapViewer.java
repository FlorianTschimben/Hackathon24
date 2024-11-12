import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
import io.jenetics.jpx.GPX;
import io.jenetics.jpx.WayPoint;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GPXMapViewer extends JPanel {
    private String gpxFilePath;
    private JXMapViewer mapViewer;

    public GPXMapViewer(String gpxFilePath) {
        this.gpxFilePath = gpxFilePath;
        setLayout(new BorderLayout());
        initializeMapViewer();
        loadRoutesFromGPX();
        add(mapViewer, BorderLayout.CENTER);
    }

    private void initializeMapViewer() {
        mapViewer = new JXMapViewer();

        TileFactoryInfo info = new TileFactoryInfo(1, 17, 17, 256, true, true,
                "https://tile.openstreetmap.org", "{z}", "{x}", "{y}.png") {
            @Override
            public String getTileUrl(int x, int y, int zoom) {
                zoom = getTotalMapZoom() - zoom;
                return this.baseURL + "/" + zoom + "/" + x + "/" + y + ".png";
            }
        };

        mapViewer.setTileFactory(new DefaultTileFactory(info));
    }

    private void loadRoutesFromGPX() {
        try {
            GPX gpx = GPX.read(Paths.get(gpxFilePath));
            List<GeoPosition> positions = new ArrayList<>();

            // Extract waypoints from GPX and add to map
            gpx.tracks().forEach(track ->
                    track.segments().forEach(segment ->
                            segment.points().forEach(point ->
                                    positions.add(new GeoPosition(point.getLatitude().doubleValue(), point.getLongitude().doubleValue()))
                            )
                    )
            );

            if (!positions.isEmpty()) {
                mapViewer.setAddressLocation(positions.get(0)); // Center on the first point
                mapViewer.setZoom(5); // Adjust zoom as needed
            }

            GPXWaypointViewer waypointViewer = new GPXWaypointViewer(mapViewer);
            waypointViewer.plotRoutes(Collections.singletonList(positions));

            System.out.println("Routes loaded from GPX file.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load GPX file: " + gpxFilePath);
        }
    }
}
