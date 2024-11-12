import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.util.ArrayList;

public class DispatcherUI extends JFrame {

    private JPanel mainPanel;
    private JPanel dashboardPanel;
    private JPanel mapPanel;
    private CardLayout cardLayout;
    private static final String GPX_FILE_PATH = "UI/gpxgenerator_path.gpx"; // Path to the GPX file

    public DispatcherUI() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        setTitle("Dispatcher UI");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        dashboardPanel = new JPanel(new BorderLayout());
        mapPanel = new JPanel(new BorderLayout());

        mainPanel.add(dashboardPanel, "Dashboard");
        mainPanel.add(mapPanel, "Map");

        JPanel sideMenu = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton dashboardButton = new JButton("Dashboard");
        JButton mapButton = new JButton("Map View");

        sideMenu.add(dashboardButton);
        sideMenu.add(mapButton);

        dashboardButton.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));

        // Modified action listener for "Map View" button
        mapButton.addActionListener(e -> {
            try {
                // Connect to the database and generate the GPX file with routes
                Connection connection = DatabaseManager.connect("jdbc:mysql://localhost/hackathon", "root", "masterkey");
                GPXRouteGenerator.generateGPXRoutes(connection);
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to load routes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Display the GPXMapViewer in the mapPanel
            displayMap();
            cardLayout.show(mainPanel, "Map");
        });

        setupDashboardPanel();

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sideMenu, BorderLayout.WEST);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    private void setupDashboardPanel() {
        String[] columnNames = {"Transport ID", "Date", "Start Time", "End Time", "From City", "From Street", "To City", "To Street", "Type", "Reference", "Total KM", "Vehicle ID", "Section"};
        Object[][] data = fetchTransportData();
        JTable transportTable = new JTable(data, columnNames);

        dashboardPanel.add(new JScrollPane(transportTable), BorderLayout.CENTER);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addTransportButton = new JButton("Add Transport");
        JButton optimizeButton = new JButton("Optimize");

        topBar.add(addTransportButton);
        topBar.add(optimizeButton);

        dashboardPanel.add(topBar, BorderLayout.NORTH);
    }

    private Object[][] fetchTransportData() {
        ArrayList<Object[]> rows = new ArrayList<>();

        try {
            ResultSet rs = DatabaseManager.getEveryTransport(DatabaseManager.connect("jdbc:mysql://localhost/hackathon", "root", "masterkey"));

            while (rs.next()) {
                Object[] row = {
                        rs.getString("tnr"),
                        rs.getDate("tdatum"),
                        rs.getTime("tstart"),
                        rs.getTime("tende"),
                        rs.getString("tvonort"),
                        rs.getString("tvonstrasse"),
                        rs.getString("tbisort"),
                        rs.getString("tbisstrasse"),
                        rs.getString("tart"),
                        rs.getString("tbezugnr"),
                        rs.getInt("tkmtotale"),
                        rs.getInt("fnr"),
                        rs.getString("tsektionsort")
                };
                rows.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rows.toArray(new Object[0][0]);
    }

    private void displayMap() {
        mapPanel.removeAll();
        mapPanel.add(new GPXMapViewer(GPX_FILE_PATH), BorderLayout.CENTER);
        mapPanel.revalidate();
        mapPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DispatcherUI ui = new DispatcherUI();
            ui.setVisible(true);
        });
    }
}
