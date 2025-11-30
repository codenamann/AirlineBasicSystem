package ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import model.Flight;
import service.FlightService;
import ui.components.ModernButton;
import ui.components.ModernTextField;
import ui.theme.Theme;

public class AddFlightDialog extends JDialog {
    private FlightService flightService;
    private ModernTextField originField, destinationField, departureField, arrivalField, capacityField;
    private boolean success = false;

    public AddFlightDialog(Frame owner, FlightService flightService) {
        super(owner, "Add Flight", true);
        this.flightService = flightService;

        setLayout(new BorderLayout());
        setSize(450, 450);
        setLocationRelativeTo(owner);
        getContentPane().setBackground(Color.WHITE);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        addInput(form, gbc, "Origin", originField = new ModernTextField(15));
        addInput(form, gbc, "Destination", destinationField = new ModernTextField(15));
        addInput(form, gbc, "Departure (HH:MM)", departureField = new ModernTextField(15));
        addInput(form, gbc, "Arrival (HH:MM)", arrivalField = new ModernTextField(15));
        addInput(form, gbc, "Capacity", capacityField = new ModernTextField(15));

        add(form, BorderLayout.CENTER);

        JPanel btns = new JPanel();
        btns.setOpaque(false);
        btns.setBorder(new EmptyBorder(0, 0, 20, 0));

        ModernButton save = new ModernButton("Save", ModernButton.Type.PRIMARY);
        save.addActionListener(e -> onSave());

        ModernButton cancel = new ModernButton("Cancel", ModernButton.Type.TEXT);
        cancel.addActionListener(e -> dispose());

        btns.add(save);
        btns.add(cancel);
        add(btns, BorderLayout.SOUTH);
    }

    private void addInput(JPanel p, GridBagConstraints gbc, String label, ModernTextField field) {
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        p.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        p.add(field, gbc);

        gbc.gridy++;
    }

    private void onSave() {
        try {
            int cap = Integer.parseInt(capacityField.getText().trim());
            Flight f = new Flight(null, originField.getText(), destinationField.getText(),
                    departureField.getText(), arrivalField.getText(), cap, cap);
            flightService.addFlight(f);
            success = true;
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Input");
        }
    }

    public boolean isSuccess() {
        return success;
    }
}
