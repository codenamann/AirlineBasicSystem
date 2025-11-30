package ui.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import ui.theme.Theme;

public class GlassPanel extends JPanel {
    private float alpha = 0.5f;
    private Color color = Color.WHITE;
    private int rounding = Theme.ROUNDING;

    public GlassPanel(float alpha) {
        this.alpha = alpha;
        setOpaque(false);
    }

    public GlassPanel(float alpha, Color color) {
        this(alpha);
        this.color = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(color);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), rounding, rounding);

        g2.dispose();
        super.paintComponent(g);
    }
}
