package ui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import ui.theme.Theme;

public class ModernButton extends JButton {
    public enum Type {
        PRIMARY, SECONDARY, DANGER, TEXT
    }

    private Type type;
    private boolean isHovered = false;
    private boolean isPressed = false;

    public ModernButton(String text) {
        this(text, Type.PRIMARY);
    }

    public ModernButton(String text, Type type) {
        super(text);
        this.type = type;

        setFont(Theme.FONT_BOLD);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(new EmptyBorder(10, 20, 10, 20));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });

        updateColors();
    }

    // Constructor for custom colors (legacy support or specific needs)
    public ModernButton(String text, Color normal, Color hover) {
        this(text, Type.PRIMARY);
        // This is a fallback to keep existing code compiling if I missed any usages,
        // but ideally we migrate to Type.
    }

    private void updateColors() {
        switch (type) {
            case PRIMARY:
                setForeground(Color.WHITE);
                break;
            case SECONDARY:
                setForeground(Theme.PRIMARY);
                break;
            case DANGER:
                setForeground(Color.WHITE);
                break;
            case TEXT:
                setForeground(Theme.TEXT_SECONDARY);
                break;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color bg = null;

        switch (type) {
            case PRIMARY:
                bg = isPressed ? Theme.PRIMARY_DARK : (isHovered ? Theme.ACCENT : Theme.PRIMARY);
                break;
            case SECONDARY:
                bg = isPressed ? new Color(0xE0F2FE) : (isHovered ? new Color(0xF0F9FF) : Color.WHITE);
                break;
            case DANGER:
                bg = isPressed ? Theme.DANGER.darker() : (isHovered ? Theme.DANGER.brighter() : Theme.DANGER);
                break;
            case TEXT:
                bg = isHovered ? new Color(0, 0, 0, 10) : new Color(0, 0, 0, 0);
                break;
        }

        if (bg != null) {
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), Theme.ROUNDING, Theme.ROUNDING);
        }

        // Border for secondary
        if (type == Type.SECONDARY) {
            g2.setColor(Theme.PRIMARY);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, Theme.ROUNDING, Theme.ROUNDING);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
