package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import service.BookingService;
import service.FlightService;
import service.PassengerService;
import ui.components.ShadowPanel;
import ui.theme.Theme;

public class DashboardPanel extends JPanel {
    private FlightService flightService;
    private PassengerService passengerService;
    private BookingService bookingService;

    private JLabel flightsVal;
    private JLabel passengersVal;
    private JLabel bookingsVal;

    public DashboardPanel(FlightService fs, PassengerService ps, BookingService bs) {
        this.flightService = fs;
        this.passengerService = ps;
        this.bookingService = bs;

        setLayout(new BorderLayout());
        setBackground(Theme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JLabel title = new JLabel("Operations Overview");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        // Metrics Grid
        JPanel grid = new JPanel(new GridLayout(1, 3, 20, 0));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(30, 0, 0, 0));

        flightsVal = new JLabel("0");
        passengersVal = new JLabel("0");
        bookingsVal = new JLabel("0");

        grid.add(createMetricCard("Total Flights", flightsVal, Theme.PRIMARY));
        grid.add(createMetricCard("Total Passengers", passengersVal, Theme.SUCCESS));
        grid.add(createMetricCard("Total Bookings", bookingsVal, Theme.WARNING));

        add(grid, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        if (flightService != null)
            flightsVal.setText(String.valueOf(flightService.getAllFlights().size()));
        if (passengerService != null)
            passengersVal.setText(String.valueOf(passengerService.getAllPassengers().size()));
        if (bookingService != null)
            bookingsVal.setText(String.valueOf(bookingService.getAllBookings().size()));
    }

    private JPanel createMetricCard(String label, JLabel valLabel, Color accent) {
        ShadowPanel card = new ShadowPanel();
        card.setLayout(new BorderLayout());

        valLabel.setFont(Theme.FONT_TITLE);
        valLabel.setForeground(Theme.TEXT_PRIMARY);

        JLabel nameLabel = new JLabel(label);
        nameLabel.setFont(Theme.FONT_BODY);
        nameLabel.setForeground(Theme.TEXT_SECONDARY);

        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(15, 20, 15, 20));
        content.add(valLabel, BorderLayout.CENTER);
        content.add(nameLabel, BorderLayout.SOUTH);

        card.add(content, BorderLayout.CENTER);

        // Accent Strip
        JPanel strip = new JPanel();
        strip.setBackground(accent);
        strip.setPreferredSize(new java.awt.Dimension(5, 0));
        card.add(strip, BorderLayout.WEST);

        return card;
    }
}
