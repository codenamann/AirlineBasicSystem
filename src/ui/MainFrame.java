package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JPanel;
import service.BookingService;
import service.FlightService;
import service.PassengerService;
import ui.components.Navbar;
import ui.components.Sidebar;
import ui.theme.Theme;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private Sidebar sidebar;
    private Navbar navbar;
    private JPanel rootPanel; // Holds Sidebar/Navbar + Content

    // Services
    private FlightService flightService;
    private PassengerService passengerService;
    private BookingService bookingService;

    // Panels
    private LandingPanel landingPanel;
    private LoginPanel loginPanel;
    private DashboardPanel dashboardPanel;
    private FlightsPanel flightsPanel;
    private PassengersPanel passengersPanel;
    private BookingsPanel bookingsPanel;

    private PassengerDashboardPanel passengerDashboardPanel;
    private ViewFlightsPanel viewFlightsPanel;
    private MyBookingsPanel myBookingsPanel;

    // State
    private String currentRole = "NONE";
    private String currentUserId = null;

    public MainFrame() {
        setTitle("SkyHigh Airline Management System");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Services
        flightService = new FlightService();
        passengerService = new PassengerService();
        bookingService = new BookingService(flightService, passengerService);

        // Root Layout
        rootPanel = new JPanel(new BorderLayout());
        setContentPane(rootPanel);

        // Navigation Components
        sidebar = new Sidebar(e -> handleNavigation(e.getActionCommand()));
        navbar = new Navbar(e -> handleNavigation(e.getActionCommand()));

        // Main Content Area (CardLayout)
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(Theme.BG_MAIN);

        // Initialize Panels
        initializePanels();

        // Add Content to Root (Initially just content, no nav)
        rootPanel.add(mainContentPanel, BorderLayout.CENTER);

        showPage("landing");
    }

    private void initializePanels() {
        landingPanel = new LandingPanel(e -> showPage("login"));
        loginPanel = new LoginPanel(this);

        // Staff Panels
        dashboardPanel = new DashboardPanel(flightService, passengerService, bookingService);
        flightsPanel = new FlightsPanel(this, flightService);
        passengersPanel = new PassengersPanel(this, passengerService);
        bookingsPanel = new BookingsPanel(this, bookingService, flightService, passengerService);

        // Passenger Panels
        passengerDashboardPanel = new PassengerDashboardPanel(e -> handleNavigation(e.getActionCommand()));
        viewFlightsPanel = new ViewFlightsPanel(this, flightService);
        myBookingsPanel = new MyBookingsPanel(this, bookingService, flightService);

        // Register Panels
        mainContentPanel.add(landingPanel, "landing");
        mainContentPanel.add(loginPanel, "login");

        mainContentPanel.add(dashboardPanel, "dashboard_staff");
        mainContentPanel.add(flightsPanel, "flights");
        mainContentPanel.add(passengersPanel, "passengers");
        mainContentPanel.add(bookingsPanel, "bookings");

        mainContentPanel.add(passengerDashboardPanel, "dashboard_passenger");
        mainContentPanel.add(viewFlightsPanel, "view_flights");
        mainContentPanel.add(myBookingsPanel, "my_bookings");
    }

    public void showPage(String pageName) {
        cardLayout.show(mainContentPanel, pageName);

        // Handle Layout Changes based on Page
        if (pageName.equals("landing") || pageName.equals("login")) {
            setFullscreenMode();
        } else if (currentRole.equals("STAFF")) {
            setStaffMode();
        } else if (currentRole.equals("PASSENGER")) {
            setPassengerMode();
        }

        // Refresh data if needed
        if (pageName.equals("dashboard_staff"))
            dashboardPanel.refresh();
        if (pageName.equals("flights") || pageName.equals("view_flights"))
            refreshFlights();
        if (pageName.equals("passengers"))
            refreshPassengers();
        if (pageName.equals("bookings") || pageName.equals("my_bookings"))
            refreshBookings();

        // Update Active State
        sidebar.setActive(pageName);
        navbar.setActive(pageName);
    }

    private void handleNavigation(String command) {
        if (command.equals("logout")) {
            setRole("NONE");
            setCurrentUserId(null);
            showPage("landing");
        } else {
            showPage(command);
        }
    }

    private void setFullscreenMode() {
        rootPanel.remove(sidebar);
        rootPanel.remove(navbar);
        rootPanel.revalidate();
        rootPanel.repaint();
    }

    private void setStaffMode() {
        rootPanel.remove(navbar);
        rootPanel.add(sidebar, BorderLayout.WEST);
        sidebar.updateMenu("STAFF");
        rootPanel.revalidate();
        rootPanel.repaint();
    }

    private void setPassengerMode() {
        rootPanel.remove(sidebar);
        rootPanel.add(navbar, BorderLayout.NORTH);
        rootPanel.revalidate();
        rootPanel.repaint();
    }

    public void setRole(String role) {
        this.currentRole = role;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String id) {
        this.currentUserId = id;
    }

    public void refreshFlights() {
        flightsPanel.refreshTable();
        viewFlightsPanel.refreshTable();
    }

    public void refreshPassengers() {
        passengersPanel.refreshTable();
    }

    public void refreshBookings() {
        bookingsPanel.refreshTable();
        myBookingsPanel.refreshTable();
    }
}
