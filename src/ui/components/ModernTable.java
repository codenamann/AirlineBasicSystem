package ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import ui.theme.Theme;

public class ModernTable extends JTable {

    public ModernTable(DefaultTableModel model) {
        super(model);

        setFont(Theme.FONT_BODY);
        setRowHeight(40);
        setShowVerticalLines(false);
        setShowHorizontalLines(true);
        setGridColor(new Color(0xE2E8F0));
        setSelectionBackground(new Color(0xE0F2FE));
        setSelectionForeground(Theme.TEXT_PRIMARY);
        setIntercellSpacing(new Dimension(0, 0));

        // Header Styling
        JTableHeader header = getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer());
        header.setPreferredSize(new Dimension(0, 40));
        header.setReorderingAllowed(false);

        // Cell Styling
        setDefaultRenderer(Object.class, new CellRenderer());
    }

    private class HeaderRenderer extends DefaultTableCellRenderer {
        public HeaderRenderer() {
            setHorizontalAlignment(JLabel.LEFT);
            setOpaque(true);
            setBackground(Color.WHITE);
            setForeground(Theme.TEXT_SECONDARY);
            setFont(Theme.FONT_BOLD);
            setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0xE2E8F0)));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBorder(BorderFactory.createCompoundBorder(getBorder(), BorderFactory.createEmptyBorder(0, 10, 0, 10)));
            return this;
        }
    }

    private class CellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                setBackground(Color.WHITE);
            }

            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

            // Status Badges Logic (Simple text color for now, can be enhanced)
            if (value != null) {
                String str = value.toString().toUpperCase();
                if (str.equals("CONFIRMED") || str.equals("ACTIVE")) {
                    setForeground(Theme.SUCCESS);
                } else if (str.equals("CANCELLED")) {
                    setForeground(Theme.DANGER);
                } else {
                    setForeground(Theme.TEXT_PRIMARY);
                }
            }

            return this;
        }
    }
}
