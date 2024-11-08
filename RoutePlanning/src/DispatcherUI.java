import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DispatcherUI extends JFrame {

    private JPanel mainPanel;
    private JPanel dashboardPanel;
    private JPanel mapPanel;
    private JPanel patientManagementPanel;
    private JPanel historyPanel;
    private CardLayout cardLayout;

    public DispatcherUI() {
        setTitle("Dispatcher UI");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create layout manager for switching panels
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize each main view panel
        dashboardPanel = new JPanel(new BorderLayout());
        mapPanel = new JPanel(); // Placeholder for map rendering, leave empty for now
        patientManagementPanel = new JPanel(new BorderLayout());
        historyPanel = new JPanel(new BorderLayout());

        // Add panels to main panel with unique identifiers
        mainPanel.add(dashboardPanel, "Dashboard");
        mainPanel.add(mapPanel, "Map");
        mainPanel.add(patientManagementPanel, "PatientManagement");
        mainPanel.add(historyPanel, "History");

        // Initialize side menu
        JPanel sideMenu = new JPanel(new GridLayout(4, 1, 5, 5));
        JButton dashboardButton = new JButton("Dashboard");
        JButton mapButton = new JButton("Map View");
        JButton patientManagementButton = new JButton("Patient Management");
        JButton historyButton = new JButton("History");

        // Add buttons to the side menu
        sideMenu.add(dashboardButton);
        sideMenu.add(mapButton);
        sideMenu.add(patientManagementButton);
        sideMenu.add(historyButton);

        // Add ActionListeners for navigation buttons
        dashboardButton.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
        mapButton.addActionListener(e -> cardLayout.show(mainPanel, "Map"));
        patientManagementButton.addActionListener(e -> cardLayout.show(mainPanel, "PatientManagement"));
        historyButton.addActionListener(e -> cardLayout.show(mainPanel, "History"));

        // Configure dashboard view
        setupDashboardPanel();

        // Configure patient management view
        setupPatientManagementPanel();

        // Configure history view
        setupHistoryPanel();

        // Set up the frame layout
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sideMenu, BorderLayout.WEST);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    private void setupDashboardPanel() {
        // Placeholder table for transport overview
        String[] columnNames = {"Vehicle ID", "Patients", "Departure", "Arrival", "Status"};
        Object[][] data = {}; // Empty data for now
        JTable transportTable = new JTable(data, columnNames);

        dashboardPanel.add(new JScrollPane(transportTable), BorderLayout.CENTER);

        // Top bar for quick actions
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addTransportButton = new JButton("Add Transport");
        JButton optimizeButton = new JButton("Optimize");

        topBar.add(addTransportButton);
        topBar.add(optimizeButton);

        dashboardPanel.add(topBar, BorderLayout.NORTH);
    }

    private void setupPatientManagementPanel() {
        // Placeholder table for patient management
        String[] columnNames = {"Patient ID", "Name", "Destination", "Arrival Time"};
        Object[][] data = {}; // Empty data for now
        JTable patientTable = new JTable(data, columnNames);

        patientManagementPanel.add(new JScrollPane(patientTable), BorderLayout.CENTER);

        // Top bar for quick actions
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addPatientButton = new JButton("Add Patient");
        JButton editPatientButton = new JButton("Edit Patient");

        topBar.add(addPatientButton);
        topBar.add(editPatientButton);

        patientManagementPanel.add(topBar, BorderLayout.NORTH);
    }

    private void setupHistoryPanel() {
        // Placeholder table for history view
        String[] columnNames = {"Transport ID", "Vehicle", "Patients", "Departure", "Arrival"};
        Object[][] data = {}; // Empty data for now
        JTable historyTable = new JTable(data, columnNames);

        historyPanel.add(new JScrollPane(historyTable), BorderLayout.CENTER);

        // Top bar for report generation
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton generateReportButton = new JButton("Generate Report");

        topBar.add(generateReportButton);

        historyPanel.add(topBar, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DispatcherUI ui = new DispatcherUI();
            ui.setVisible(true);
        });
    }
}
