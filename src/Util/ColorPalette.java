package Util;

import Model.Theme;
import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public final class ColorPalette {

    private static ColorPalette instance;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    // Backgrounds
    private Color bgMain = new Color(0x1E1E1E); // #1E1E1E
    private Color bgSecondary = new Color(0x2D2D2D); // #2D2D2D
    private Color bgTertiary = new Color(0x3C3C3C); // #3C3C3C

    // Borders & lines
    private Color border = new Color(0x555555); // #555555

    // Foreground
    private Color textPrimary = new Color(0xE0E0E0); // #E0E0E0
    private Color textMuted = new Color(0xA0A0A0); // #A0A0A0
    private Color textPlaceholder = new Color(0x616161); // #616161

    // Accents
    private Color accentPrimary = new Color(0x4F9EFF); // #4F9EFF
    private Color accentSuccess = new Color(0x6FCF97); // #6FCF97
    private Color accentWarning = new Color(0xF2994A); // #F2994A
    private Color accentDanger = new Color(0xC74B44); // #C74B44
    private Color accentConfirm = new Color(0x5B9E5F); // #5B9E5F

    // Selection
    private Color selectionBg = new Color(0x3A6EA5); // #3A6EA5

    // Button states
    private Color buttonNormal = new Color(0x3C3C3C); // #3C3C3C
    private Color buttonHover = new Color(0x6BB0FF); // #6BB0FF
    private Color buttonPressed = new Color(0x3A7BC8); // #3A7BC8

    private ColorPalette() {
    }

    public static synchronized ColorPalette getInstance() {
        if (instance == null) {
            instance = new ColorPalette();
        }
        return instance;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    // --- Backgrounds ---

    public Color getBgMain() {
        return bgMain;
    }

    public void setBgMain(Color bgMain) {

        if (this.bgMain.equals(bgMain)) {
            return;
        }
        Color old = this.bgMain;
        this.bgMain = bgMain;
        support.firePropertyChange("bgMain", old, bgMain);
    }

    public Color getBgSecondary() {
        return bgSecondary;
    }

    public void setBgSecondary(Color bgSecondary) {
        if (this.bgSecondary.equals(bgSecondary)) {
            return;
        }
        Color old = this.bgSecondary;
        this.bgSecondary = bgSecondary;
        support.firePropertyChange("bgSecondary", old, bgSecondary);
    }

    public Color getBgTertiary() {
        return bgTertiary;
    }

    public void setBgTertiary(Color bgTertiary) {
        if (this.bgTertiary.equals(bgTertiary)) {
            return;
        }
        Color old = this.bgTertiary;
        this.bgTertiary = bgTertiary;
        support.firePropertyChange("bgTertiary", old, bgTertiary);
    }

    // --- Borders ---

    public Color getBorder() {
        return border;
    }

    public void setBorder(Color border) {
        if (this.border.equals(border)) {
            return;
        }
        Color old = this.border;
        this.border = border;
        support.firePropertyChange("border", old, border);
    }

    // --- Foreground ---

    public Color getTextPrimary() {
        return textPrimary;
    }

    public void setTextPrimary(Color textPrimary) {
        if (this.textPrimary.equals(textPrimary)) {
            return;
        }
        Color old = this.textPrimary;
        this.textPrimary = textPrimary;
        support.firePropertyChange("textPrimary", old, textPrimary);
    }

    public Color getTextMuted() {
        return textMuted;
    }

    public void setTextMuted(Color textMuted) {
        if (this.textMuted.equals(textMuted)) {
            return;
        }
        Color old = this.textMuted;
        this.textMuted = textMuted;
        support.firePropertyChange("textMuted", old, textMuted);
    }

    public Color getTextPlaceholder() {
        return textPlaceholder;
    }

    public void setTextPlaceholder(Color textPlaceholder) {
        if (this.textPlaceholder.equals(textPlaceholder)) {
            return;
        }
        Color old = this.textPlaceholder;
        this.textPlaceholder = textPlaceholder;
        support.firePropertyChange("textPlaceholder", old, textPlaceholder);
    }

    // --- Accents ---

    public Color getAccentPrimary() {
        return accentPrimary;
    }

    public void setAccentPrimary(Color accentPrimary) {
        if (this.accentPrimary.equals(accentPrimary)) {
            return;
        }
        Color old = this.accentPrimary;
        this.accentPrimary = accentPrimary;
        support.firePropertyChange("accentPrimary", old, accentPrimary);
    }

    public Color getAccentSuccess() {
        return accentSuccess;
    }

    public void setAccentSuccess(Color accentSuccess) {
        if (this.accentSuccess.equals(accentSuccess)) {
            return;
        }
        Color old = this.accentSuccess;
        this.accentSuccess = accentSuccess;
        support.firePropertyChange("accentSuccess", old, accentSuccess);
    }

    public Color getAccentWarning() {
        return accentWarning;
    }

    public void setAccentWarning(Color accentWarning) {
        if (this.accentWarning.equals(accentWarning)) {
            return;
        }
        Color old = this.accentWarning;
        this.accentWarning = accentWarning;
        support.firePropertyChange("accentWarning", old, accentWarning);
    }

    public Color getAccentDanger() {
        return accentDanger;
    }

    public void setAccentDanger(Color accentDanger) {
        if (this.accentDanger.equals(accentDanger)) {
            return;
        }
        Color old = this.accentDanger;
        this.accentDanger = accentDanger;
        support.firePropertyChange("accentDanger", old, accentDanger);
    }

    public Color getAccentConfirm() {
        return accentConfirm;
    }

    public void setAccentConfirm(Color accentConfirm) {
        if (this.accentConfirm.equals(accentConfirm)) {
            return;
        }
        Color old = this.accentConfirm;
        this.accentConfirm = accentConfirm;
        support.firePropertyChange("accentConfirm", old, accentConfirm);
    }

    // --- Selection ---

    public Color getSelectionBg() {
        return selectionBg;
    }

    public void setSelectionBg(Color selectionBg) {
        if (this.selectionBg.equals(selectionBg)) {
            return;
        }
        Color old = this.selectionBg;
        this.selectionBg = selectionBg;
        support.firePropertyChange("selectionBg", old, selectionBg);
    }

    // --- Button states ---

    public Color getButtonNormal() {
        return buttonNormal;
    }

    public void setButtonNormal(Color buttonNormal) {
        if (this.buttonNormal.equals(buttonNormal)) {
            return;
        }
        Color old = this.buttonNormal;
        this.buttonNormal = buttonNormal;
        support.firePropertyChange("buttonNormal", old, buttonNormal);
    }

    public Color getButtonHover() {
        return buttonHover;
    }

    public void setButtonHover(Color buttonHover) {
        if (this.buttonHover.equals(buttonHover)) {
            return;
        }
        Color old = this.buttonHover;
        this.buttonHover = buttonHover;
        support.firePropertyChange("buttonHover", old, buttonHover);
    }

    public Color getButtonPressed() {
        return buttonPressed;
    }

    public void setButtonPressed(Color buttonPressed) {
        if (this.buttonPressed.equals(buttonPressed)) {
            return;
        }
        Color old = this.buttonPressed;
        this.buttonPressed = buttonPressed;
        support.firePropertyChange("buttonPressed", old, buttonPressed);
    }

    public void applyTheme(Theme theme) {
        if (theme == null) {
            return;
        }

        bgMain = parseColor(theme.getBgMain(), bgMain);
        bgSecondary = parseColor(theme.getBgSecondary(), bgSecondary);
        bgTertiary = parseColor(theme.getBgTertiary(), bgTertiary);
        border = parseColor(theme.getBorder(), border);
        textPrimary = parseColor(theme.getTextPrimary(), textPrimary);
        textMuted = parseColor(theme.getTextMuted(), textMuted);
        textPlaceholder = parseColor(theme.getTextPlaceholder(), textPlaceholder);
        accentPrimary = parseColor(theme.getAccentPrimary(), accentPrimary);
        accentSuccess = parseColor(theme.getAccentSuccess(), accentSuccess);
        accentWarning = parseColor(theme.getAccentWarning(), accentWarning);
        accentDanger = parseColor(theme.getAccentDanger(), accentDanger);
        accentConfirm = parseColor(theme.getAccentConfirm(), accentConfirm);
        selectionBg = parseColor(theme.getSelectionBg(), selectionBg);
        buttonNormal = parseColor(theme.getButtonNormal(), buttonNormal);
        buttonHover = parseColor(theme.getButtonHover(), buttonHover);
        buttonPressed = parseColor(theme.getButtonPressed(), buttonPressed);

        support.firePropertyChange("theme", null, theme);
    }

    private static Color parseColor(String hex, Color fallback) {
        if (hex == null || hex.isBlank()) {
            return fallback;
        }

        try {
            return Color.decode(hex);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
