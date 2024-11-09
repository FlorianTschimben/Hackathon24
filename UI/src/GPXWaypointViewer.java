import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.input.PanMouseInputListener;
import org.jdesktop.swingx.input.ZoomMouseWheelListenerCenter;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.WaypointPainter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GPXWaypointViewer {

    private final JXMapViewer mapViewer;

    public GPXWaypointViewer(JXMapViewer mapViewer) {
        this.mapViewer = mapViewer;

        // Zoom mit dem Mausrad aktivieren
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));

        // Kartenverschiebung (Pan) mit der Maus aktivieren
        PanMouseInputListener panListener = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(panListener);
        mapViewer.addMouseMotionListener(panListener);
    }

    public void plotWaypoints(List<GeoPosition> positions) {
        List<CustomWaypoint> waypoints = new ArrayList<>();

        Icon icon = null; // Customize if needed

        for (GeoPosition pos : positions) {
            waypoints.add(new CustomWaypoint(pos, icon));
        }

        CustomWaypointRenderer renderer = new CustomWaypointRenderer();
        renderer.setWaypoints(waypoints);

        WaypointPainter<CustomWaypoint> waypointPainter = new WaypointPainter<>();
        waypointPainter.setWaypoints(new HashSet<>(waypoints));
        waypointPainter.setRenderer(renderer);

        mapViewer.setOverlayPainter(waypointPainter);

        if (!positions.isEmpty()) {
            mapViewer.setAddressLocation(positions.get(0));
            mapViewer.setZoom(5);
        }
        mapViewer.repaint();
    }
}
