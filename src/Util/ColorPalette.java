package Util;

import java.awt.Color;

public final class ColorPalette {
    private ColorPalette() {
    } // prevent instantiation

    // Backgrounds
    public static final Color BG_MAIN = new Color(0x1E1E1E);
    public static final Color BG_SECONDARY = new Color(0x2D2D2D);
    public static final Color BG_TERTIARY = new Color(0x3C3C3C);

    // Borders & lines
    public static final Color BORDER = new Color(0x555555);

    // Foreground
    public static final Color TEXT_PRIMARY = new Color(0xE0E0E0);
    public static final Color TEXT_MUTED = new Color(0xA0A0A0);

    // Accents
    public static final Color ACCENT_PRIMARY = new Color(0x4F9EFF);
    public static final Color ACCENT_SUCCESS = new Color(0x6FCF97);
    public static final Color ACCENT_WARNING = new Color(0xF2994A);

    // Selection
    public static final Color SELECTION_BG = new Color(0x3A6EA5);
}
