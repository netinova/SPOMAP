package Util;

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
        Color old = this.bgSecondary;
        this.bgSecondary = bgSecondary;
        support.firePropertyChange("bgSecondary", old, bgSecondary);
    }

    public Color getBgTertiary() {
        return bgTertiary;
    }

    public void setBgTertiary(Color bgTertiary) {
        Color old = this.bgTertiary;
        this.bgTertiary = bgTertiary;
        support.firePropertyChange("bgTertiary", old, bgTertiary);
    }

    // --- Borders ---

    public Color getBorder() {
        return border;
    }

    public void setBorder(Color border) {
        Color old = this.border;
        this.border = border;
        support.firePropertyChange("border", old, border);
    }

    // --- Foreground ---

    public Color getTextPrimary() {
        return textPrimary;
    }

    public void setTextPrimary(Color textPrimary) {
        Color old = this.textPrimary;
        this.textPrimary = textPrimary;
        support.firePropertyChange("textPrimary", old, textPrimary);
    }

    public Color getTextMuted() {
        return textMuted;
    }

    public void setTextMuted(Color textMuted) {
        Color old = this.textMuted;
        this.textMuted = textMuted;
        support.firePropertyChange("textMuted", old, textMuted);
    }

    public Color getTextPlaceholder() {
        return textPlaceholder;
    }

    public void setTextPlaceholder(Color textPlaceholder) {
        Color old = this.textPlaceholder;
        this.textPlaceholder = textPlaceholder;
        support.firePropertyChange("textPlaceholder", old, textPlaceholder);
    }

    // --- Accents ---

    public Color getAccentPrimary() {
        return accentPrimary;
    }

    public void setAccentPrimary(Color accentPrimary) {
        Color old = this.accentPrimary;
        this.accentPrimary = accentPrimary;
        support.firePropertyChange("accentPrimary", old, accentPrimary);
    }

    public Color getAccentSuccess() {
        return accentSuccess;
    }

    public void setAccentSuccess(Color accentSuccess) {
        Color old = this.accentSuccess;
        this.accentSuccess = accentSuccess;
        support.firePropertyChange("accentSuccess", old, accentSuccess);
    }

    public Color getAccentWarning() {
        return accentWarning;
    }

    public void setAccentWarning(Color accentWarning) {
        Color old = this.accentWarning;
        this.accentWarning = accentWarning;
        support.firePropertyChange("accentWarning", old, accentWarning);
    }

    public Color getAccentDanger() {
        return accentDanger;
    }

    public void setAccentDanger(Color accentDanger) {
        Color old = this.accentDanger;
        this.accentDanger = accentDanger;
        support.firePropertyChange("accentDanger", old, accentDanger);
    }

    public Color getAccentConfirm() {
        return accentConfirm;
    }

    public void setAccentConfirm(Color accentConfirm) {
        Color old = this.accentConfirm;
        this.accentConfirm = accentConfirm;
        support.firePropertyChange("accentConfirm", old, accentConfirm);
    }

    // --- Selection ---

    public Color getSelectionBg() {
        return selectionBg;
    }

    public void setSelectionBg(Color selectionBg) {
        Color old = this.selectionBg;
        this.selectionBg = selectionBg;
        support.firePropertyChange("selectionBg", old, selectionBg);
    }

    // --- Button states ---

    public Color getButtonNormal() {
        return buttonNormal;
    }

    public void setButtonNormal(Color buttonNormal) {
        Color old = this.buttonNormal;
        this.buttonNormal = buttonNormal;
        support.firePropertyChange("buttonNormal", old, buttonNormal);
    }

    public Color getButtonHover() {
        return buttonHover;
    }

    public void setButtonHover(Color buttonHover) {
        Color old = this.buttonHover;
        this.buttonHover = buttonHover;
        support.firePropertyChange("buttonHover", old, buttonHover);
    }

    public Color getButtonPressed() {
        return buttonPressed;
    }

    public void setButtonPressed(Color buttonPressed) {
        Color old = this.buttonPressed;
        this.buttonPressed = buttonPressed;
        support.firePropertyChange("buttonPressed", old, buttonPressed);
    }
}
