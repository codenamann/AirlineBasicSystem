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

public class EditFlightDialog extends JDialog {
    private FlightService flightService;
    private Flight flight;
    private ModernTextField originField, destinationField, departureField, arrivalField, capacityField, seatsField;
    private boolean success = false;

    public EditFlightDialog(Frame owner, FlightService flightService, Flight flight) {
        super(owner, "Edit Flight", true);
        this.flightService = flightService;
        this.flight = flight;

        setLayout(new BorderLayout());
        setSize(450, 500);
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

        addInput(form, gbc, "Origin", originField = new ModernTextField(flight.getOrigin(), 15));
        addInput(form, gbc, "Destination", destinationField = new ModernTextField(flight.getDestination(), 15));
        addInput(form, gbc, "Departure", departureField = new ModernTextField(flight.getDeparture(), 15));
        addInput(form, gbc, "Arrival", arrivalField = new ModernTextField(flight.getArrival(), 15));
        addInput(form, gbc, "Capacity", capacityField = new ModernTextField(String.valueOf(flight.getCapacity()), 15));
        addInput(form, gbc, "Seats Left", seatsField = new ModernTextField(String.valueOf(flight.getSeatsLeft()), 15));

        add(form, BorderLayout.CENTER);

        JPanel btns = new JPanel();
        btns.setOpaque(false);
        btns.setBorder(new EmptyBorder(0, 0, 20, 0));

        ModernButton save = new ModernButton("Save Changes", ModernButton.Type.PRIMARY);
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
            flight.setOrigin(originField.getText());
            flight.setDestination(destinationField.getText());
            flight.setDeparture(departureField.getText());
            flight.setArrival(arrivalField.getText());
            flight.setCapacity(Integer.parseInt(capacityField.getText()));
            flight.setSeatsLeft(Integer.parseInt(seatsField.getText()));

            flightService.updateFlight(flight);
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
