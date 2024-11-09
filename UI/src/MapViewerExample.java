import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.OSMTileFactoryInfo;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;

import javax.swing.*;
import java.awt.*;

public class MapViewerExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Map Viewer Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            // Create a JXMapViewer instance
            JXMapViewer mapViewer = new JXMapViewer();

            // Set up the tile factory (using OpenStreetMap)
            TileFactoryInfo info = new OSMTileFactoryInfo();
            TileFactory tileFactory = new org.jdesktop.swingx.mapviewer.DefaultTileFactory(info);
            mapViewer.setTileFactory(tileFactory);

            // Set the initial focus location (e.g., New York City)
            GeoPosition geoPosition = new GeoPosition(46.6390, 11.3573);
            mapViewer.setZoom(9); // Set initial zoom level
            mapViewer.setAddressLocation(geoPosition);

            // Add the map viewer to a Swing container
            frame.getContentPane().add(mapViewer);

            frame.setVisible(true);
        });
    }
}
