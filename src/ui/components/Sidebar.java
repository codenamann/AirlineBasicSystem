package ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import ui.theme.Theme;

public class Sidebar extends JPanel {
    private ActionListener navigationListener;
    private JPanel menuPanel;
    private Map<String, JButton> navButtons = new HashMap<>();
    private String activeCommand = "";

    public Sidebar(ActionListener navigationListener) {
        this.navigationListener = navigationListener;

        setBackground(Theme.BG_SIDEBAR);
        setPreferredSize(new Dimension(260, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Brand Area
        JPanel brandPanel = new JPanel();
        brandPanel.setOpaque(false);
        brandPanel.setMaximumSize(new Dimension(260, 100));
        brandPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));

        JLabel brandLabel = new JLabel("SKYHIGH");
        brandLabel.setFont(Theme.FONT_H1);
        brandLabel.setForeground(Color.WHITE);

        JLabel subBrand = new JLabel("OPERATIONS");
        subBrand.setFont(Theme.FONT_SMALL);
        subBrand.setForeground(Theme.TEXT_SECONDARY);

        brandPanel.add(brandLabel);
        brandPanel.add(subBrand);

        add(brandPanel);

        // Menu Items
        menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new EmptyBorder(0, 15, 0, 15));

        add(menuPanel);
        add(Box.createVerticalGlue());

        // User Profile / Logout
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setMaximumSize(new Dimension(260, 80));
        bottomPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        addDivider(bottomPanel);
        addNavItem(bottomPanel, "Logout", "logout", true);

        add(bottomPanel);
    }

    public void updateMenu(String role) {
        menuPanel.removeAll();
        navButtons.clear();

        if ("STAFF".equals(role)) {
            addNavItem(menuPanel, "Dashboard", "dashboard_staff", false);
            addDivider(menuPanel);
            addNavItem(menuPanel, "Flights", "flights", false);
            addNavItem(menuPanel, "Passengers", "passengers", false);
            addNavItem(menuPanel, "Bookings", "bookings", false);
        }

        revalidate();
        repaint();
    }

    public void setActive(String command) {
        this.activeCommand = command;
        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            boolean isActive = entry.getKey().equals(command);
            JButton btn = entry.getValue();
            if (isActive) {
                btn.setForeground(Theme.ACCENT);
                btn.setFont(Theme.FONT_BOLD);
            } else {
                btn.setForeground(Theme.TEXT_ON_DARK);
                btn.setFont(Theme.FONT_BODY);
            }
        }
    }

    private void addNavItem(JPanel parent, String text, String command, boolean isDanger) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getActionCommand().equals(activeCommand) && !isDanger) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 255, 255, 20));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };

        btn.setFont(Theme.FONT_BODY);
        btn.setForeground(isDanger ? Theme.DANGER : Theme.TEXT_ON_DARK);
        btn.setActionCommand(command);
        btn.addActionListener(e -> {
            setActive(command);
            navigationListener.actionPerformed(e);
        });
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(JButton.LEFT);
        btn.setMaximumSize(new Dimension(230, 45));

        navButtons.put(command, btn);
        parent.add(btn);
        parent.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void addDivider(JPanel parent) {
        JPanel div = new JPanel();
        div.setBackground(new Color(255, 255, 255, 30));
        div.setMaximumSize(new Dimension(230, 1));
        parent.add(Box.createRigidArea(new Dimension(0, 10)));
        parent.add(div);
        parent.add(Box.createRigidArea(new Dimension(0, 10)));
    }
}
