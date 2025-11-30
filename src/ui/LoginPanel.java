package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import ui.components.ModernButton;
import ui.components.ModernTextField;
import ui.components.ShadowPanel;
import ui.theme.Theme;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());

        ShadowPanel card = new ShadowPanel();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(400, 300));
        card.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Header
        JLabel title = new JLabel("Welcome Back");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);
        title.setHorizontalAlignment(JLabel.CENTER);

        gbc.gridy = 0;
        gbc.insets = new Insets(30, 20, 30, 20);
        card.add(title, gbc);

        // Buttons
        ModernButton staffBtn = new ModernButton("Login as Staff", ModernButton.Type.PRIMARY);
        staffBtn.addActionListener(e -> {
            mainFrame.setRole("STAFF");
            mainFrame.showPage("dashboard_staff");
        });

        ModernButton passengerBtn = new ModernButton("Login as Passenger", ModernButton.Type.SECONDARY);
        passengerBtn.addActionListener(e -> {
            mainFrame.setRole("PASSENGER");
            mainFrame.setCurrentUserId("P001");
            mainFrame.showPage("dashboard_passenger");
        });

        gbc.gridy = 1;
        gbc.insets = new Insets(10, 40, 10, 40);
        card.add(staffBtn, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(10, 40, 30, 40);
        card.add(passengerBtn, gbc);

        add(card);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Simple background
        g2.setColor(Theme.BG_MAIN);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Decorative circle
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(220, 230, 255));
        g2.fillOval(-100, -100, 400, 400);
    }
}
