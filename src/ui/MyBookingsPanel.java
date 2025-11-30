package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.Booking;
import service.BookingService;
import service.FlightService;
import ui.components.ModernButton;
import ui.components.ModernScrollPane;
import ui.components.ModernTable;
import ui.dialogs.PassengerBookFlightDialog;
import ui.theme.Theme;

public class MyBookingsPanel extends JPanel {
    private MainFrame mainFrame;
    private BookingService bookingService;
    private FlightService flightService;
    private ModernTable table;
    private DefaultTableModel model;

    public MyBookingsPanel(MainFrame mainFrame, BookingService bookingService, FlightService flightService) {
        this.mainFrame = mainFrame;
        this.bookingService = bookingService;
        this.flightService = flightService;

        setLayout(new BorderLayout());
        setBackground(Theme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("My Trips");
        title.setFont(Theme.FONT_H1);
        title.setForeground(Theme.TEXT_PRIMARY);
        header.add(title, BorderLayout.WEST);

        // Actions
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);

        ModernButton bookBtn = new ModernButton("Book New Flight", ModernButton.Type.PRIMARY);
        bookBtn.addActionListener(e -> openBookDialog());
        actions.add(bookBtn);

        ModernButton cancelBtn = new ModernButton("Cancel Selected", ModernButton.Type.DANGER);
        cancelBtn.addActionListener(e -> cancelBooking());
        actions.add(cancelBtn);

        header.add(actions, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Table
        String[] cols = { "Booking ID", "Flight", "Seat", "Status" };
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new ModernTable(model);
        add(new ModernScrollPane(table), BorderLayout.CENTER);

        loadData();
    }

    public void refreshTable() {
        loadData();
    }

    private void loadData() {
        if (bookingService == null || mainFrame == null)
            return;
        String uid = mainFrame.getCurrentUserId();
        if (uid == null)
            return;

        model.setRowCount(0);
        List<Booking> list = bookingService.getAllBookings();
        for (Booking b : list) {
            if (uid.equals(b.getPassengerId())) {
                model.addRow(new Object[] {
                        b.getBookingId(), b.getFlightId(), b.getSeatNo(), b.getStatus()
                });
            }
        }
    }

    private void openBookDialog() {
        PassengerBookFlightDialog d = new PassengerBookFlightDialog(mainFrame, mainFrame, bookingService, flightService,
                this::refreshTable);
        d.setVisible(true);
    }

    private void cancelBooking() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        String id = (String) table.getValueAt(row, 0);
        if (bookingService.cancelBooking(id)) {
            refreshTable();
            mainFrame.refreshFlights();
        }
    }
}
