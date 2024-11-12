import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.WaypointRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class CustomWaypointRenderer implements WaypointRenderer<CustomWaypoint> {
    private final List<List<CustomWaypoint>> routes = new ArrayList<>();

    // Set multiple routes for rendering
    public void setRoutes(List<List<CustomWaypoint>> routes) {
        this.routes.clear();
        this.routes.addAll(routes);
    }

    @Override
    public void paintWaypoint(Graphics2D g, JXMapViewer map, CustomWaypoint waypoint) {
        // Set line color and thickness for routes
        g.setStroke(new BasicStroke(4.0f));

        // Draw each route with black lines
        for (List<CustomWaypoint> route : routes) {
            if (route.size() > 1) {
                g.setColor(Color.BLACK); // Ensure color is set to black before each route

                for (int i = 1; i < route.size(); i++) {
                    Point2D previousPosition2D = map.getTileFactory().geoToPixel(route.get(i - 1).getPosition(), map.getZoom());
                    Point2D currentPosition2D = map.getTileFactory().geoToPixel(route.get(i).getPosition(), map.getZoom());
                    g.drawLine((int) previousPosition2D.getX(), (int) previousPosition2D.getY(),
                            (int) currentPosition2D.getX(), (int) currentPosition2D.getY());
                }
            }

            // Draw red circles for start and end waypoints only
            for (CustomWaypoint wp : route) {

                    Point2D position2D = map.getTileFactory().geoToPixel(wp.getPosition(), map.getZoom());
                    Point position = new Point((int) Math.round(position2D.getX()), (int) Math.round(position2D.getY()));

                    g.setColor(Color.RED); // Set color to red for extremity points
                    int size = 10;
                    g.fillOval(position.x - size / 2, position.y - size / 2, size, size);

            }
        }
    }
}