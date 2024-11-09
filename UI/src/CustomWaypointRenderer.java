import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.WaypointRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class CustomWaypointRenderer implements WaypointRenderer<CustomWaypoint> {
    private final List<CustomWaypoint> waypoints = new ArrayList<>();

    public void setWaypoints(List<CustomWaypoint> waypoints) {
        this.waypoints.clear();
        this.waypoints.addAll(waypoints);
    }

    @Override
    public void paintWaypoint(Graphics2D g, JXMapViewer map, CustomWaypoint waypoint) {
        Icon icon = waypoint.getIcon();

        // Convert GeoPosition to screen coordinates
        Point2D position2D = map.getTileFactory().geoToPixel(waypoint.getPosition(), map.getZoom());
        Point position = new Point((int) Math.round(position2D.getX()), (int) Math.round(position2D.getY()));

        // Draw continuous line between waypoints
        if (waypoints.size() > 1) {
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(4.0f));

            for (int i = 1; i < waypoints.size(); i++) {
                Point2D previousPosition2D = map.getTileFactory().geoToPixel(waypoints.get(i - 1).getPosition(), map.getZoom());
                Point2D currentPosition2D = map.getTileFactory().geoToPixel(waypoints.get(i).getPosition(), map.getZoom());
                g.drawLine((int) previousPosition2D.getX(), (int) previousPosition2D.getY(),
                        (int) currentPosition2D.getX(), (int) currentPosition2D.getY());
            }
        }

        // Draw the waypoint icon or a default circle if no icon is provided
        if (icon != null) {
            icon.paintIcon(map, g, position.x - icon.getIconWidth() / 2, position.y - icon.getIconHeight() / 2);
        } else {
            g.setColor(Color.RED);
            int size = 10;
            g.fillOval(position.x - size / 2, position.y - size / 2, size, size);
        }
    }
}
