package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import ui.components.ModernButton;
import ui.components.ShadowPanel;
import ui.theme.Theme;

public class PassengerDashboardPanel extends JPanel {
    private ActionListener navigationListener;

    public PassengerDashboardPanel(ActionListener navigationListener) {
        this.navigationListener = navigationListener;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_MAIN);

        // Hero Section
        JPanel hero = new JPanel();
        hero.setLayout(new BoxLayout(hero, BoxLayout.Y_AXIS));
        hero.setBackground(Theme.PRIMARY);
        hero.setBorder(new EmptyBorder(60, 60, 60, 60));

        JLabel welcome = new JLabel("Where to next?");
        welcome.setFont(Theme.FONT_TITLE);
        welcome.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Discover your next adventure with SkyHigh.");
        sub.setFont(Theme.FONT_H2);
        sub.setForeground(new Color(255, 255, 255, 200));

        hero.add(welcome);
        hero.add(Box.createRigidArea(new Dimension(0, 10)));
        hero.add(sub);

        add(hero, BorderLayout.NORTH);

        // Quick Actions
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        actions.setOpaque(false);

        actions.add(createActionCard("Book a Flight", "Explore destinations", "Search", "view_flights"));
        actions.add(createActionCard("My Trips", "Manage bookings", "View", "my_bookings"));

        add(actions, BorderLayout.CENTER);
    }

    private JPanel createActionCard(String title, String sub, String btnText, String cmd) {
        ShadowPanel card = new ShadowPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(250, 180));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel t = new JLabel(title);
        t.setFont(Theme.FONT_H2);
        t.setForeground(Theme.TEXT_PRIMARY);

        JLabel s = new JLabel(sub);
        s.setFont(Theme.FONT_BODY);
        s.setForeground(Theme.TEXT_SECONDARY);

        ModernButton btn = new ModernButton(btnText, ModernButton.Type.PRIMARY);
        btn.setActionCommand(cmd);
        btn.addActionListener(navigationListener);

        card.add(t);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(s);
        card.add(Box.createVerticalGlue());
        card.add(btn);

        return card;
    }
}
