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
import model.Passenger;
import service.PassengerService;
import ui.components.ModernButton;
import ui.components.ModernTextField;

public class EditPassengerDialog extends JDialog {
    private PassengerService passengerService;
    private Passenger passenger;
    private ModernTextField nameField, ageField, passportField;
    private JComboBox<String> genderCombo;
    private boolean success = false;

    public EditPassengerDialog(Frame owner, PassengerService passengerService, Passenger passenger) {
        super(owner, "Edit Passenger", true);
        this.passengerService = passengerService;
        this.passenger = passenger;

        setLayout(new BorderLayout());
        setSize(400, 400);
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

        addInput(form, gbc, "Name", nameField = new ModernTextField(passenger.getName(), 15));
        addInput(form, gbc, "Age", ageField = new ModernTextField(String.valueOf(passenger.getAge()), 15));

        gbc.gridx = 0;
        form.add(new JLabel("Gender"), gbc);
        gbc.gridx = 1;
        genderCombo = new JComboBox<>(new String[] { "Male", "Female", "Other" });
        genderCombo.setSelectedItem(passenger.getGender());
        form.add(genderCombo, gbc);
        gbc.gridy++;

        addInput(form, gbc, "Passport", passportField = new ModernTextField(passenger.getPassportNumber(), 15));

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
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        p.add(field, gbc);
        gbc.gridy++;
    }

    private void onSave() {
        try {
            passenger.setName(nameField.getText());
            passenger.setAge(Integer.parseInt(ageField.getText()));
            passenger.setGender((String) genderCombo.getSelectedItem());
            passenger.setPassportNumber(passportField.getText());

            passengerService.updatePassenger(passenger);
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
