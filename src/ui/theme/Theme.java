package ui.theme;

import java.awt.Color;
import java.awt.Font;

public class Theme {
    // Brand Colors
    public static final Color PRIMARY = new Color(0x0066CC); // Deep Sky Blue
    public static final Color PRIMARY_DARK = new Color(0x004C99); // Darker Blue
    public static final Color ACCENT = new Color(0x00C2FF); // Cyan/Bright Blue

    // Backgrounds
    public static final Color BG_MAIN = new Color(0xF4F7FA); // Very light grey-blue
    public static final Color BG_PANEL = Color.WHITE;
    public static final Color BG_SIDEBAR = new Color(0x0F172A); // Dark Navy

    // Text
    public static final Color TEXT_PRIMARY = new Color(0x1E293B); // Dark Slate
    public static final Color TEXT_SECONDARY = new Color(0x64748B);// Slate Grey
    public static final Color TEXT_ON_DARK = new Color(0xF1F5F9); // Off-white

    // Status
    public static final Color SUCCESS = new Color(0x10B981); // Emerald
    public static final Color WARNING = new Color(0xF59E0B); // Amber
    public static final Color DANGER = new Color(0xEF4444); // Red

    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_H1 = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_H2 = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);

    // Dimensions
    public static final int ROUNDING = 12;
    public static final int PADDING = 20;
}
