import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.awt.Desktop;


public class MapViewerApp extends JFrame {

    public MapViewerApp(String start, String end) {
        /*
        setTitle("Map Viewer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Call OpenRouteService to get GeoJSON
        String geoJSON = OpenRouteServiceGPX.getGeoJSON(start, end);
        if (geoJSON != null) {
            // Generate HTML using Python script
            if (generateMapHTML(geoJSON)) {
                // Display the generated HTML in JEditorPane
                JEditorPane editorPane = new JEditorPane();
                editorPane.setEditable(false);
                try {
                    System.out.println("editorpane set page");
                    System.setProperty("jxbrowser.bindings", "jxbrowser-win64");

                    editorPane.setPage(new URL("file:C:\\Users\\User\\Info\\20241108 Hackathon 2024\\Hackathon24\\UI\\src\\map.html"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JScrollPane scrollPane = new JScrollPane(editorPane);
                add(scrollPane, BorderLayout.CENTER);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to generate map HTML", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to retrieve GeoJSON data", "Error", JOptionPane.ERROR_MESSAGE);
        }
        */

        try {
            // Specify the path to the HTML file
            File htmlFile = new File("C:\\Users\\User\\Info\\20241108 Hackathon 2024\\Hackathon24\\UI\\src\\map.html");

            // Check if Desktop is supported on the current platform
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (htmlFile.exists()) {
                    // Open the HTML file in the default browser
                    desktop.open(htmlFile);
                } else {
                    System.out.println("HTML file does not exist.");
                }
            } else {
                System.out.println("Desktop is not supported on your platform.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean generateMapHTML(String geoJSON) {
        try {
            // Run the Python script to generate the map
            System.out.println(geoJSON);
            geoJSON = geoJSON.replaceAll("\"", "\\\\\"");
            System.out.println(geoJSON);
            ProcessBuilder processBuilder = new ProcessBuilder("python", "C:\\Users\\User\\Info\\20241108 Hackathon 2024\\Hackathon24\\UI\\src\\generate_map.py", geoJSON);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Capture output for debugging
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("exit code " + exitCode);
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void showMap(String start, String end) {
        // Example coordinates for start and end points
        //String start = "8.681423,49.41461";  // Start coordinates
        //String end = "8.687872,49.420318";   // End coordinates

        SwingUtilities.invokeLater(() -> {
            MapViewerApp app = new MapViewerApp(start, end);
            app.setVisible(true);
        });
    }
}
