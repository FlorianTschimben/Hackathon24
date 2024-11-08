import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.OSMTileFactoryInfo;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GPXMapViewer {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Load the GPX route
                List<GeoPosition> gpxRoute = GPXLoader.loadGPX("path/to/your/file.gpx");

                // Set up the map viewer
                JXMapViewer mapViewer = new JXMapViewer();
                TileFactoryInfo info = new OSMTileFactoryInfo();
                mapViewer.setTileFactory(new DefaultTileFactory(info));

                // Set the map center and zoom level
                mapViewer.setZoom(4);
                mapViewer.setAddressLocation(gpxRoute.get(0)); // Start position of the route

                // Add the route painter
                RoutePainter routePainter = new RoutePainter(gpxRoute);
                mapViewer.setOverlayPainter(routePainter);

                // Set up the frame
                JFrame frame = new JFrame("GPX Route Viewer");
                frame.getContentPane().add(new JScrollPane(mapViewer));
                frame.setSize(800, 600);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
