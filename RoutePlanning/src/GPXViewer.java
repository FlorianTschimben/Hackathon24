import io.jenetics.jpx.GPX;
import io.jenetics.jpx.WayPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class GPXViewer extends JPanel {

    private List<WayPoint> routePoints;

    public GPXViewer(String gpxFilePath) {
        try {
            // Load GPX file and get waypoints
            GPX gpx = GPX.read(Paths.get(gpxFilePath));
            routePoints = gpx.tracks().flatMap(t -> t.segments())
                    .flatMap(s -> s.points()).toList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (routePoints == null || routePoints.isEmpty()) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLUE);

        // Define map bounds (for simple projection mapping)
        double minLat = routePoints.stream().mapToDouble(p -> p.getLatitude().doubleValue()).min().orElse(0);
        double maxLat = routePoints.stream().mapToDouble(p -> p.getLatitude().doubleValue()).max().orElse(0);
        double minLon = routePoints.stream().mapToDouble(p -> p.getLongitude().doubleValue()).min().orElse(0);
        double maxLon = routePoints.stream().mapToDouble(p -> p.getLongitude().doubleValue()).max().orElse(0);

        // Draw route using the points in the GPX file
        Path2D path = new Path2D.Double();
        for (int i = 0; i < routePoints.size(); i++) {
            WayPoint point = routePoints.get(i);

            // Convert GPS coordinates to panel coordinates
            int x = (int) ((point.getLongitude().doubleValue() - minLon) / (maxLon - minLon) * getWidth());
            int y = (int) ((maxLat - point.getLatitude().doubleValue()) / (maxLat - minLat) * getHeight());

            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }

        g2d.draw(path);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GPX Route Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        GPXViewer viewer = new GPXViewer("path/to/your/file.gpx");
        frame.add(viewer);

        frame.setVisible(true);
    }
}
