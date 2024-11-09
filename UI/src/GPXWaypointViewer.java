import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.input.PanMouseInputListener;
import org.jdesktop.swingx.input.ZoomMouseWheelListenerCenter;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdesktop.swingx.painter.CompoundPainter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GPXWaypointViewer {
    private final JXMapViewer mapViewer;

    public GPXWaypointViewer(JXMapViewer mapViewer) {
        this.mapViewer = mapViewer;
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));
        PanMouseInputListener panListener = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(panListener);
        mapViewer.addMouseMotionListener(panListener);
    }

    public void plotRoutes(List<List<GeoPosition>> routePositions) {
        List<List<CustomWaypoint>> routes = new ArrayList<>();

        for (List<GeoPosition> positions : routePositions) {
            List<CustomWaypoint> waypoints = new ArrayList<>();
            Icon icon = null; // Customize if needed

            for (int i = 0; i < positions.size(); i++) {
                CustomWaypointType type = (i == 0 || i == positions.size() - 1)
                        ? CustomWaypointType.START
                        : CustomWaypointType.INTERMEDIATE;
                waypoints.add(new CustomWaypoint(positions.get(i), icon, type));
            }
            routes.add(waypoints);
        }

        CustomWaypointRenderer renderer = new CustomWaypointRenderer();
        renderer.setRoutes(routes);

        WaypointPainter<CustomWaypoint> waypointPainter = new WaypointPainter<>();
        Set<CustomWaypoint> allWaypoints = new HashSet<>();
        routes.forEach(allWaypoints::addAll);
        waypointPainter.setWaypoints(allWaypoints);
        waypointPainter.setRenderer(renderer);

        // Use CompoundPainter to ensure map tiles and waypoints are layered correctly
        CompoundPainter<JXMapViewer> compoundPainter = new CompoundPainter<>(waypointPainter);
        mapViewer.setOverlayPainter(compoundPainter);
        mapViewer.repaint();
    }
}