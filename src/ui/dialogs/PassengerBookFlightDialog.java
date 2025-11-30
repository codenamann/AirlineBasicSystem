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
import service.BookingService;
import service.FlightService;
import ui.MainFrame;
import ui.components.ModernButton;

public class PassengerBookFlightDialog extends JDialog {
    private MainFrame mainFrame;
    private BookingService bookingService;
    private FlightService flightService;
    private Runnable onSuccess;
    private JComboBox<String> flightCombo;

    public PassengerBookFlightDialog(Frame owner, MainFrame mf, BookingService bs, FlightService fs,
            Runnable onSuccess) {
        super(owner, "Book Flight", true);
        this.mainFrame = mf;
        this.bookingService = bs;
        this.flightService = fs;
        this.onSuccess = onSuccess;

        setLayout(new BorderLayout());
        setSize(500, 250);
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

        form.add(new JLabel("Select Flight"), gbc);
        gbc.gridx = 1;
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

        ModernButton save = new ModernButton("Confirm Booking", ModernButton.Type.PRIMARY);
        save.addActionListener(e -> onSave());

        ModernButton cancel = new ModernButton("Cancel", ModernButton.Type.TEXT);
        cancel.addActionListener(e -> dispose());

        btns.add(save);
        btns.add(cancel);
        add(btns, BorderLayout.SOUTH);
    }

    private void onSave() {
        try {
            String fid = ((String) flightCombo.getSelectedItem()).split(" ")[0];
            String pid = mainFrame.getCurrentUserId();

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
