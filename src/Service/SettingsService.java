package Service;

import Model.Settings;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

public class SettingsService {
    private static final String SETTINGS_FILE = "database/settings.json";
    private final ObjectMapper mapper;

    public SettingsService() {
        this.mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        initializeSettingsFile();
    }

    private void initializeSettingsFile() {
        File file = new File(SETTINGS_FILE);
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            saveSettings(new Settings("default dark"));
        }
    }

    public Settings loadSettings() {
        File file = new File(SETTINGS_FILE);
        if (!file.exists()) {
            initializeSettingsFile();
        }

        try {
            return mapper.readValue(file, Settings.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load settings", e);
        }
    }

    public void saveSettings(Settings settings) {
        if (settings == null) {
            throw new IllegalArgumentException("Settings must not be null");
        }

        File file = new File(SETTINGS_FILE);
        file.getParentFile().mkdirs();
        try {
            mapper.writeValue(file, settings);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save settings", e);
        }
    }
}
