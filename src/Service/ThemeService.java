package Service;

import Model.Theme;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ThemeService {
    private static final String THEME_FILE = "database/themes.json";
    private final ObjectMapper mapper;

    public ThemeService() {
        this.mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        initializeThemeDatabase();
    }

    private void initializeThemeDatabase() {
        File file = new File(THEME_FILE);
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            try {
                List<Theme> themes = new ArrayList<>();
                themes.add(Theme.defaultDark());
                mapper.writeValue(file, themes);
            } catch (IOException e) {
                throw new RuntimeException("Failed to initialize theme database", e);
            }
        }
    }

    public List<Theme> loadAllThemes() {
        File file = new File(THEME_FILE);
        if (!file.exists()) {
            initializeThemeDatabase();
        }

        try {
            return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, Theme.class));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load themes", e);
        }
    }

    public Theme loadThemeByName(String themeName) {
        if (themeName == null || themeName.isEmpty()) {
            return null;
        }

        List<Theme> themes = loadAllThemes();
        for (Theme theme : themes) {
            if (theme != null && theme.hasName(themeName)) {
                return theme;
            }
        }
        return null;
    }

    public void saveTheme(Theme theme) {
        if (theme == null || theme.getName() == null || theme.getName().isEmpty()) {
            throw new IllegalArgumentException("Theme and theme name must not be null or empty");
        }

        List<Theme> themes = loadAllThemes();
        boolean updated = false;
        for (int i = 0; i < themes.size(); i++) {
            Theme existing = themes.get(i);
            if (existing != null && existing.hasName(theme.getName())) {
                themes.set(i, theme);
                updated = true;
                break;
            }
        }

        if (!updated) {
            themes.add(theme);
        }

        try {
            File file = new File(THEME_FILE);
            file.getParentFile().mkdirs();
            mapper.writeValue(file, themes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save theme", e);
        }
    }
}
