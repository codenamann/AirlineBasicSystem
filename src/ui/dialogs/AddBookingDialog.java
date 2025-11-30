package ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import model.Flight;
import model.Passenger;
import service.BookingService;
import service.FlightService;
import service.PassengerService;
import ui.components.ModernButton;

public class AddBookingDialog extends JDialog {
    private BookingService bookingService;
    private FlightService flightService;
    private PassengerService passengerService;
    private Runnable onSuccess;
    private JComboBox<String> passengerCombo, flightCombo;

    public AddBookingDialog(Frame owner, BookingService bs, FlightService fs, PassengerService ps, Runnable onSuccess) {
        super(owner, "New Booking", true);
        this.bookingService = bs;
        this.flightService = fs;
        this.passengerService = ps;
        this.onSuccess = onSuccess;

        setLayout(new BorderLayout());
        setSize(500, 300);
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

        gbc.weightx = 0.3;
        form.add(new JLabel("Passenger"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        passengerCombo = new JComboBox<>();
        for (Passenger p : ps.getAllPassengers())
            passengerCombo.addItem(p.getPassengerId() + " - " + p.getName());
        form.add(passengerCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;
        form.add(new JLabel("Flight"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        flightCombo = new JComboBox<>();
        for (Flight f : fs.getAllFlights()) {
            if (f.getSeatsLeft() > 0)
                flightCombo.addItem(f.getFlightId() + " (" + f.getOrigin() + "->" + f.getDestination() + ")");
        }
        form.add(flightCombo, gbc);

        add(form, BorderLayout.CENTER);

        JPanel btns = new JPanel();
        btns.setOpaque(false);
        btns.setBorder(new EmptyBorder(0, 0, 20, 0));

        ModernButton save = new ModernButton("Create Booking", ModernButton.Type.PRIMARY);
        save.addActionListener(e -> onSave());

        ModernButton cancel = new ModernButton("Cancel", ModernButton.Type.TEXT);
        cancel.addActionListener(e -> dispose());

        btns.add(save);
        btns.add(cancel);
        add(btns, BorderLayout.SOUTH);
    }

    private void onSave() {
        try {
            String pid = ((String) passengerCombo.getSelectedItem()).split(" - ")[0];
            String fid = ((String) flightCombo.getSelectedItem()).split(" ")[0];

            if (bookingService.createBooking(fid, pid)) {
                if (onSuccess != null)
                    onSuccess.run();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Booking Failed");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Selection Error");
        }
    }
}
