package ui;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ProfilePanel extends JPanel {
    public ProfilePanel() {
        setLayout(new GridBagLayout());
        add(new JLabel("Profile Screen (Phase 3)"));
    }
}
