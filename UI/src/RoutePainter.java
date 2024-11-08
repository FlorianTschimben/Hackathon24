import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public class RoutePainter implements Painter<JXMapViewer> {
    private final List<GeoPosition> track;

    public RoutePainter(List<GeoPosition> track) {
        this.track = track;
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
        g = (Graphics2D) g.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.RED); // Route color
        g.setStroke(new BasicStroke(2)); // Route thickness

        // Convert GeoPositions to screen coordinates and draw lines between them
        Point2D lastPoint = null;
        for (GeoPosition geoPos : track) {
            Point2D pt = map.getTileFactory().geoToPixel(geoPos, map.getZoom());
            if (lastPoint != null) {
                g.drawLine((int) lastPoint.getX(), (int) lastPoint.getY(), (int) pt.getX(), (int) pt.getY());
            }
            lastPoint = pt;
        }

        g.dispose();
    }
}
