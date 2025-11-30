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
import model.Passenger;
import service.PassengerService;
import ui.components.ModernButton;
import ui.components.ModernScrollPane;
import ui.components.ModernTable;
import ui.components.ModernTextField;
import ui.dialogs.AddPassengerDialog;
import ui.dialogs.EditPassengerDialog;
import ui.theme.Theme;

public class PassengersPanel extends JPanel {
    private MainFrame mainFrame;
    private PassengerService passengerService;
    private ModernTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private ModernTextField searchField;

    public PassengersPanel(MainFrame mainFrame, PassengerService passengerService) {
        this.mainFrame = mainFrame;
        this.passengerService = passengerService;

        setLayout(new BorderLayout());
        setBackground(Theme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Passenger Directory");
        title.setFont(Theme.FONT_H1);
        title.setForeground(Theme.TEXT_PRIMARY);
        header.add(title, BorderLayout.WEST);

        // Actions
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);

        searchField = new ModernTextField(20);
        actions.add(new JLabel("Search: "));
        actions.add(searchField);

        ModernButton addBtn = new ModernButton("Add Passenger", ModernButton.Type.PRIMARY);
        addBtn.addActionListener(e -> openAddDialog());
        actions.add(addBtn);

        ModernButton editBtn = new ModernButton("Edit", ModernButton.Type.SECONDARY);
        editBtn.addActionListener(e -> openEditDialog());
        actions.add(editBtn);

        ModernButton delBtn = new ModernButton("Delete", ModernButton.Type.DANGER);
        delBtn.addActionListener(e -> deletePassenger());
        actions.add(delBtn);

        header.add(actions, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Table
        String[] cols = { "ID", "Name", "Age", "Gender", "Passport" };
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
        if (passengerService == null)
            return;
        model.setRowCount(0);
        List<Passenger> list = passengerService.getAllPassengers();
        for (Passenger p : list) {
            model.addRow(new Object[] {
                    p.getPassengerId(), p.getName(), p.getAge(), p.getGender(), p.getPassportNumber()
            });
        }
    }

    private void openAddDialog() {
        AddPassengerDialog d = new AddPassengerDialog(mainFrame, passengerService);
        d.setVisible(true);
        if (d.isSuccess())
            refreshTable();
    }

    private void openEditDialog() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        String id = (String) table.getValueAt(row, 0);
        Passenger p = passengerService.getPassengerById(id);
        if (p != null) {
            EditPassengerDialog d = new EditPassengerDialog(mainFrame, passengerService, p);
            d.setVisible(true);
            if (d.isSuccess())
                refreshTable();
        }
    }

    private void deletePassenger() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        String id = (String) table.getValueAt(row, 0);
        if (passengerService.deletePassenger(id))
            refreshTable();
    }
}
