package ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import ui.theme.Theme;

public class Navbar extends JPanel {
    private ActionListener navigationListener;
    private Map<String, JButton> navButtons = new HashMap<>();
    private String activeCommand = "";

    public Navbar(ActionListener navigationListener) {
        this.navigationListener = navigationListener;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(0, 70));
        setBorder(new EmptyBorder(0, 30, 0, 30));

        // Brand
        JLabel brand = new JLabel("SkyHigh Airlines");
        brand.setFont(Theme.FONT_H2);
        brand.setForeground(Theme.PRIMARY);
        add(brand, BorderLayout.WEST);

        // Menu
        JPanel menu = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        menu.setOpaque(false);

        addNavItem(menu, "Dashboard", "dashboard_passenger");
        addNavItem(menu, "Book Flight", "view_flights");
        addNavItem(menu, "My Trips", "my_bookings");
        addNavItem(menu, "Logout", "logout");

        add(menu, BorderLayout.CENTER);

        // Bottom Border
        JPanel border = new JPanel();
        border.setBackground(new Color(0xE2E8F0));
        border.setPreferredSize(new Dimension(0, 1));
        add(border, BorderLayout.SOUTH);
    }

    public void setActive(String command) {
        this.activeCommand = command;
        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            boolean isActive = entry.getKey().equals(command);
            JButton btn = entry.getValue();
            if (isActive) {
                btn.setForeground(Theme.PRIMARY);
                btn.setFont(Theme.FONT_BOLD);
            } else {
                btn.setForeground(Theme.TEXT_SECONDARY);
                btn.setFont(Theme.FONT_BODY);
            }
            btn.repaint();
        }
    }

    private void addNavItem(JPanel parent, String text, String command) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getActionCommand().equals(activeCommand)) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Theme.PRIMARY);
                    g2.fillRect(0, getHeight() - 3, getWidth(), 3);
                    g2.dispose();
                }
            }
        };

        btn.setFont(Theme.FONT_BODY);
        btn.setForeground(Theme.TEXT_SECONDARY);
        btn.setActionCommand(command);
        btn.addActionListener(e -> {
            setActive(command);
            navigationListener.actionPerformed(e);
        });
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        navButtons.put(command, btn);
        parent.add(btn);
    }
}
