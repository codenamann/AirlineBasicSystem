package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ui.components.ModernButton;
import ui.theme.Theme;

public class LandingPanel extends JPanel {
    private ActionListener navigationListener;

    public LandingPanel(ActionListener navigationListener) {
        this.navigationListener = navigationListener;
        setLayout(new GridBagLayout());

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("SKYHIGH");
        title.setFont(new Font("Segoe UI", Font.BOLD, 64));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("The Future of Aviation Management");
        subtitle.setFont(Theme.FONT_H2);
        subtitle.setForeground(new Color(255, 255, 255, 200));
        subtitle.setAlignmentX(CENTER_ALIGNMENT);

        ModernButton enterBtn = new ModernButton("ENTER SYSTEM", ModernButton.Type.PRIMARY);
        enterBtn.setAlignmentX(CENTER_ALIGNMENT);
        enterBtn.addActionListener(navigationListener);

        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        content.add(subtitle);
        content.add(Box.createRigidArea(new Dimension(0, 50)));
        content.add(enterBtn);

        add(content);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Gradient Background
        java.awt.GradientPaint gp = new java.awt.GradientPaint(
                0, 0, Theme.PRIMARY_DARK,
                getWidth(), getHeight(), Theme.ACCENT);
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
