package Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Theme {

    private String name;

    private String bgMain;
    private String bgSecondary;
    private String bgTertiary;

    private String border;

    private String textPrimary;
    private String textMuted;
    private String textPlaceholder;

    private String accentPrimary;
    private String accentSuccess;
    private String accentWarning;
    private String accentDanger;
    private String accentConfirm;

    private String selectionBg;

    private String buttonNormal;
    private String buttonHover;
    private String buttonPressed;

    public Theme() {
    }

    public Theme(String name,
            String bgMain,
            String bgSecondary,
            String bgTertiary,
            String border,
            String textPrimary,
            String textMuted,
            String textPlaceholder,
            String accentPrimary,
            String accentSuccess,
            String accentWarning,
            String accentDanger,
            String accentConfirm,
            String selectionBg,
            String buttonNormal,
            String buttonHover,
            String buttonPressed) {
        this.name = name;
        this.bgMain = bgMain;
        this.bgSecondary = bgSecondary;
        this.bgTertiary = bgTertiary;
        this.border = border;
        this.textPrimary = textPrimary;
        this.textMuted = textMuted;
        this.textPlaceholder = textPlaceholder;
        this.accentPrimary = accentPrimary;
        this.accentSuccess = accentSuccess;
        this.accentWarning = accentWarning;
        this.accentDanger = accentDanger;
        this.accentConfirm = accentConfirm;
        this.selectionBg = selectionBg;
        this.buttonNormal = buttonNormal;
        this.buttonHover = buttonHover;
        this.buttonPressed = buttonPressed;
    }

    public static Theme defaultDark() {
        return new Theme(
                "default dark",
                "#1E1E1E",
                "#2D2D2D",
                "#3C3C3C",
                "#555555",
                "#E0E0E0",
                "#A0A0A0",
                "#616161",
                "#4F9EFF",
                "#6FCF97",
                "#F2994A",
                "#C74B44",
                "#5B9E5F",
                "#3A6EA5",
                "#3C3C3C",
                "#6BB0FF",
                "#3A7BC8"
        );
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBgMain() {
        return bgMain;
    }

    public void setBgMain(String bgMain) {
        this.bgMain = bgMain;
    }

    public String getBgSecondary() {
        return bgSecondary;
    }

    public void setBgSecondary(String bgSecondary) {
        this.bgSecondary = bgSecondary;
    }

    public String getBgTertiary() {
        return bgTertiary;
    }

    public void setBgTertiary(String bgTertiary) {
        this.bgTertiary = bgTertiary;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getTextPrimary() {
        return textPrimary;
    }

    public void setTextPrimary(String textPrimary) {
        this.textPrimary = textPrimary;
    }

    public String getTextMuted() {
        return textMuted;
    }

    public void setTextMuted(String textMuted) {
        this.textMuted = textMuted;
    }

    public String getTextPlaceholder() {
        return textPlaceholder;
    }

    public void setTextPlaceholder(String textPlaceholder) {
        this.textPlaceholder = textPlaceholder;
    }

    public String getAccentPrimary() {
        return accentPrimary;
    }

    public void setAccentPrimary(String accentPrimary) {
        this.accentPrimary = accentPrimary;
    }

    public String getAccentSuccess() {
        return accentSuccess;
    }

    public void setAccentSuccess(String accentSuccess) {
        this.accentSuccess = accentSuccess;
    }

    public String getAccentWarning() {
        return accentWarning;
    }

    public void setAccentWarning(String accentWarning) {
        this.accentWarning = accentWarning;
    }

    public String getAccentDanger() {
        return accentDanger;
    }

    public void setAccentDanger(String accentDanger) {
        this.accentDanger = accentDanger;
    }

    public String getAccentConfirm() {
        return accentConfirm;
    }

    public void setAccentConfirm(String accentConfirm) {
        this.accentConfirm = accentConfirm;
    }

    public String getSelectionBg() {
        return selectionBg;
    }

    public void setSelectionBg(String selectionBg) {
        this.selectionBg = selectionBg;
    }

    public String getButtonNormal() {
        return buttonNormal;
    }

    public void setButtonNormal(String buttonNormal) {
        this.buttonNormal = buttonNormal;
    }

    public String getButtonHover() {
        return buttonHover;
    }

    public void setButtonHover(String buttonHover) {
        this.buttonHover = buttonHover;
    }

    public String getButtonPressed() {
        return buttonPressed;
    }

    public void setButtonPressed(String buttonPressed) {
        this.buttonPressed = buttonPressed;
    }

    public boolean hasName(String themeName) {
        return themeName != null && themeName.equalsIgnoreCase(this.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Theme)) return false;
        Theme theme = (Theme) o;
        return Objects.equals(name, theme.name) &&
                Objects.equals(bgMain, theme.bgMain) &&
                Objects.equals(bgSecondary, theme.bgSecondary) &&
                Objects.equals(bgTertiary, theme.bgTertiary) &&
                Objects.equals(border, theme.border) &&
                Objects.equals(textPrimary, theme.textPrimary) &&
                Objects.equals(textMuted, theme.textMuted) &&
                Objects.equals(textPlaceholder, theme.textPlaceholder) &&
                Objects.equals(accentPrimary, theme.accentPrimary) &&
                Objects.equals(accentSuccess, theme.accentSuccess) &&
                Objects.equals(accentWarning, theme.accentWarning) &&
                Objects.equals(accentDanger, theme.accentDanger) &&
                Objects.equals(accentConfirm, theme.accentConfirm) &&
                Objects.equals(selectionBg, theme.selectionBg) &&
                Objects.equals(buttonNormal, theme.buttonNormal) &&
                Objects.equals(buttonHover, theme.buttonHover) &&
                Objects.equals(buttonPressed, theme.buttonPressed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, bgMain, bgSecondary, bgTertiary, border, textPrimary,
                textMuted, textPlaceholder, accentPrimary, accentSuccess, accentWarning,
                accentDanger, accentConfirm, selectionBg, buttonNormal, buttonHover,
                buttonPressed);
    }
}
