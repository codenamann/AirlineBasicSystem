package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import model.Booking;
import service.BookingService;
import service.FlightService;
import service.PassengerService;
import ui.components.ModernButton;
import ui.components.ModernScrollPane;
import ui.components.ModernTable;
import ui.components.ModernTextField;
import ui.dialogs.AddBookingDialog;
import ui.theme.Theme;

public class BookingsPanel extends JPanel {
    private MainFrame mainFrame;
    private BookingService bookingService;
    private FlightService flightService;
    private PassengerService passengerService;
    private ModernTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private ModernTextField searchField;

    public BookingsPanel(MainFrame mainFrame, BookingService bookingService, FlightService flightService,
            PassengerService passengerService) {
        this.mainFrame = mainFrame;
        this.bookingService = bookingService;
        this.flightService = flightService;
        this.passengerService = passengerService;

        setLayout(new BorderLayout());
        setBackground(Theme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("All Bookings");
        title.setFont(Theme.FONT_H1);
        title.setForeground(Theme.TEXT_PRIMARY);
        header.add(title, BorderLayout.WEST);

        // Actions
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);

        searchField = new ModernTextField(20);
        actions.add(new JLabel("Search: "));
        actions.add(searchField);

        ModernButton addBtn = new ModernButton("New Booking", ModernButton.Type.PRIMARY);
        addBtn.addActionListener(e -> openAddDialog());
        actions.add(addBtn);

        ModernButton cancelBtn = new ModernButton("Cancel Booking", ModernButton.Type.DANGER);
        cancelBtn.addActionListener(e -> cancelBooking());
        actions.add(cancelBtn);

        header.add(actions, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Table
        String[] cols = { "Booking ID", "Flight", "Passenger", "Seat", "Status" };
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new ModernTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        add(new ModernScrollPane(table), BorderLayout.CENTER);

        // Search Logic
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            public void changedUpdate(DocumentEvent e) {
                filter();
            }

            private void filter() {
                String text = searchField.getText();
                if (text.trim().length() == 0)
                    sorter.setRowFilter(null);
                else
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });

        loadData();
    }

    public void refreshTable() {
        loadData();
    }

    private void loadData() {
        if (bookingService == null)
            return;
        model.setRowCount(0);
        List<Booking> list = bookingService.getAllBookings();
        for (Booking b : list) {
            model.addRow(new Object[] {
                    b.getBookingId(), b.getFlightId(), b.getPassengerId(), b.getSeatNo(), b.getStatus()
            });
        }
    }

    private void openAddDialog() {
        AddBookingDialog d = new AddBookingDialog(mainFrame, bookingService, flightService, passengerService,
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
