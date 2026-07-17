package Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Settings {
    private String activeThemeName;

    public Settings() {
    }

    public Settings(String activeThemeName) {
        this.activeThemeName = activeThemeName;
    }

    public String getActiveThemeName() {
        return activeThemeName;
    }

    public void setActiveThemeName(String activeThemeName) {
        this.activeThemeName = activeThemeName;
    }
}
