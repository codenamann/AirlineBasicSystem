package ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

public class TopHeader extends GradientPanel {
    private JLabel titleLabel;

    public TopHeader() {
        super(Color.WHITE, Color.WHITE); // Solid white for a cleaner look
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 70));
        setBorder(new EmptyBorder(0, 30, 0, 30));

        // Add a subtle bottom border effect using a shadow panel or just simple border?
        // For now, let's keep it clean.

        titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(StyleConstants.HEADER_FONT);
        titleLabel.setForeground(StyleConstants.TEXT_COLOR);

        add(titleLabel, BorderLayout.WEST);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }
}
