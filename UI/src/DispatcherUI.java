import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

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

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        dashboardPanel = new JPanel(new BorderLayout());
        mapPanel = new JPanel();
        patientManagementPanel = new JPanel(new BorderLayout());
        historyPanel = new JPanel(new BorderLayout());

        mainPanel.add(dashboardPanel, "Dashboard");
        mainPanel.add(mapPanel, "Map");
        mainPanel.add(patientManagementPanel, "PatientManagement");
        mainPanel.add(historyPanel, "History");

        JPanel sideMenu = new JPanel(new GridLayout(4, 1, 5, 5));
        JButton dashboardButton = new JButton("Dashboard");
        JButton mapButton = new JButton("Map View");
        JButton patientManagementButton = new JButton("Patient Management");
        JButton historyButton = new JButton("History");

        sideMenu.add(dashboardButton);
        sideMenu.add(mapButton);
        sideMenu.add(patientManagementButton);
        sideMenu.add(historyButton);

        dashboardButton.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
        mapButton.addActionListener(e -> cardLayout.show(mainPanel, "Map"));
        patientManagementButton.addActionListener(e -> cardLayout.show(mainPanel, "PatientManagement"));
        historyButton.addActionListener(e -> cardLayout.show(mainPanel, "History"));

        setupDashboardPanel();
        setupPatientManagementPanel();
        setupHistoryPanel();

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sideMenu, BorderLayout.WEST);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    private void setupDashboardPanel() {
        String[] columnNames = {"Transport ID", "Date", "Start Time", "End Time", "From City", "From Street", "To City", "To Street", "Type", "Reference", "Total KM", "Vehicle ID", "Section"};
        Object[][] data = fetchTransportData(); // Get data from the database
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
            ResultSet rs = DatabaseManager.getEveryTransport(DatabaseManager.connect("jdbc:mysql://10.199.228.183/hackathon", "root", ""));

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
                    rs.getString("tsektionsoirt")
                };
                rows.add(row);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rows.toArray(new Object[0][0]);
    }

    private void setupPatientManagementPanel() {
        String[] columnNames = {"Patient ID", "Name", "Destination", "Arrival Time"};
        Object[][] data = {};
        JTable patientTable = new JTable(data, columnNames);

        patientManagementPanel.add(new JScrollPane(patientTable), BorderLayout.CENTER);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addPatientButton = new JButton("Add Patient");
        JButton editPatientButton = new JButton("Edit Patient");

        topBar.add(addPatientButton);
        topBar.add(editPatientButton);

        patientManagementPanel.add(topBar, BorderLayout.NORTH);
    }

    private void setupHistoryPanel() {
        String[] columnNames = {"Transport ID", "Vehicle", "Patients", "Departure", "Arrival"};
        Object[][] data = {};
        JTable historyTable = new JTable(data, columnNames);

        historyPanel.add(new JScrollPane(historyTable), BorderLayout.CENTER);

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
