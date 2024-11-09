// MapViewer.java
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.OSMTileFactoryInfo;
import org.jdesktop.swingx.mapviewer.*;
import io.jenetics.jpx.WayPoint;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MapViewer extends JFrame {
    private JXMapViewer mapViewer;

    public MapViewer(List<WayPoint> waypoints, boolean displayGPXPaths) {
        setTitle("GPX Map Viewer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Setup JXMapViewer
        mapViewer = new JXMapViewer();

        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        tileFactory.setThreadPoolSize(8); // Increase the concurrency for tile loading
        mapViewer.setTileFactory(tileFactory);

        // Center the map
        GeoPosition initialPosition = new GeoPosition(0, 0); // Default to the equator
        mapViewer.setZoom(4);
        mapViewer.setAddressLocation(initialPosition);

        // Handle GPX path visualization
        if (displayGPXPaths) {
            List<GeoPosition> geoPositions = waypoints.stream()
                    .map(wp -> new GeoPosition(wp.getLatitude().doubleValue(), wp.getLongitude().doubleValue()))
                    .collect(Collectors.toList());

            // Center the map on the first GPX point if available
            if (!geoPositions.isEmpty()) {
                mapViewer.setAddressLocation(geoPositions.get(0));
            }

            // Add waypoints to a Set
            Set<Waypoint> waypointSet = new HashSet<>();
            for (GeoPosition position : geoPositions) {
                waypointSet.add(new DefaultWaypoint(position));
            }

            // Create a WaypointPainter for waypoints
            WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
            waypointPainter.setWaypoints(waypointSet);

            // Set the overlay painter
            mapViewer.setOverlayPainter(waypointPainter);
        }

        // Add the JXMapViewer to the JFrame
        setLayout(new BorderLayout());
        add(mapViewer, BorderLayout.CENTER);
    }
}

