package ui;

import java.awt.BorderLayout;
import java.awt.Color;
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
import model.Flight;
import service.FlightService;
import ui.components.ModernScrollPane;
import ui.components.ModernTable;
import ui.components.ModernTextField;
import ui.theme.Theme;

public class ViewFlightsPanel extends JPanel {
    private MainFrame mainFrame;
    private FlightService flightService;
    private ModernTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private ModernTextField searchField;

    public ViewFlightsPanel(MainFrame mainFrame, FlightService flightService) {
        this.mainFrame = mainFrame;
        this.flightService = flightService;

        setLayout(new BorderLayout());
        setBackground(Theme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Available Flights");
        title.setFont(Theme.FONT_H1);
        title.setForeground(Theme.TEXT_PRIMARY);
        header.add(title, BorderLayout.WEST);

        // Search
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);

        searchField = new ModernTextField(20);
        actions.add(new JLabel("Destination: "));
        actions.add(searchField);

        header.add(actions, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Table
        String[] cols = { "ID", "Origin", "Destination", "Dep", "Arr", "Seats Left" };
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new ModernTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        add(new ModernScrollPane(table), BorderLayout.CENTER);

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
        if (flightService == null)
            return;
        model.setRowCount(0);
        List<Flight> list = flightService.getAllFlights();
        for (Flight f : list) {
            model.addRow(new Object[] {
                    f.getFlightId(), f.getOrigin(), f.getDestination(),
                    f.getDeparture(), f.getArrival(), f.getSeatsLeft()
            });
        }
    }
}
