package ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import ui.theme.Theme;

public class ShadowPanel extends JPanel {
    private int shadowSize = 5;
    private Color shadowColor = new Color(0, 0, 0, 30);
    private Color bgColor = Color.WHITE;
    private int rounding = Theme.ROUNDING;

    public ShadowPanel() {
        setOpaque(false);
        setBorder(new EmptyBorder(shadowSize, shadowSize, shadowSize, shadowSize));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw Shadow
        g2.setColor(shadowColor);
        g2.fillRoundRect(shadowSize, shadowSize, getWidth() - shadowSize, getHeight() - shadowSize, rounding, rounding);

        // Draw Panel
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth() - shadowSize, getHeight() - shadowSize, rounding, rounding);

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public void setBackground(Color bg) {
        this.bgColor = bg;
        repaint();
    }
}
