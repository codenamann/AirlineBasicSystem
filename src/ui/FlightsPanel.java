package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import model.Flight;
import service.FlightService;
import ui.components.ModernButton;
import ui.components.ModernScrollPane;
import ui.components.ModernTable;
import ui.components.ModernTextField;
import ui.dialogs.AddFlightDialog;
import ui.dialogs.EditFlightDialog;
import ui.theme.Theme;

public class FlightsPanel extends JPanel {
    private MainFrame mainFrame;
    private FlightService flightService;
    private ModernTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private ModernTextField searchField;

    public FlightsPanel(MainFrame mainFrame, FlightService flightService) {
        this.mainFrame = mainFrame;
        this.flightService = flightService;

        setLayout(new BorderLayout());
        setBackground(Theme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Flight Management");
        title.setFont(Theme.FONT_H1);
        title.setForeground(Theme.TEXT_PRIMARY);
        header.add(title, BorderLayout.WEST);

        // Actions
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);

        searchField = new ModernTextField(20);
        actions.add(new JLabel("Search: "));
        actions.add(searchField);

        ModernButton addBtn = new ModernButton("New Flight", ModernButton.Type.PRIMARY);
        addBtn.addActionListener(e -> openAddDialog());
        actions.add(addBtn);

        ModernButton editBtn = new ModernButton("Edit", ModernButton.Type.SECONDARY);
        editBtn.addActionListener(e -> openEditDialog());
        actions.add(editBtn);

        ModernButton delBtn = new ModernButton("Delete", ModernButton.Type.DANGER);
        delBtn.addActionListener(e -> deleteFlight());
        actions.add(delBtn);

        header.add(actions, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Table
        String[] cols = { "ID", "Origin", "Destination", "Dep", "Arr", "Cap", "Seats" };
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new ModernTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(new EmptyBorder(0, 0, 0, 0)); // Clean look
        tableContainer.add(new ModernScrollPane(table));

        add(tableContainer, BorderLayout.CENTER);

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
        if (flightService == null)
            return;
        model.setRowCount(0);
        List<Flight> list = flightService.getAllFlights();
        for (Flight f : list) {
            model.addRow(new Object[] {
                    f.getFlightId(), f.getOrigin(), f.getDestination(),
                    f.getDeparture(), f.getArrival(), f.getCapacity(), f.getSeatsLeft()
            });
        }
    }

    private void openAddDialog() {
        AddFlightDialog d = new AddFlightDialog(mainFrame, flightService);
        d.setVisible(true);
        if (d.isSuccess())
            refreshTable();
    }

    private void openEditDialog() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        String id = (String) table.getValueAt(row, 0);
        Flight f = flightService.getFlightById(id);
        if (f != null) {
            EditFlightDialog d = new EditFlightDialog(mainFrame, flightService, f);
            d.setVisible(true);
            if (d.isSuccess())
                refreshTable();
        }
    }

    private void deleteFlight() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        String id = (String) table.getValueAt(row, 0);
        if (flightService.deleteFlight(id))
            refreshTable();
    }
}
