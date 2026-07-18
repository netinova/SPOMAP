package Controller;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import Components.SplashScreen;
import Model.ProductCatalog;
import Model.Settings;
import Model.Theme;
import Service.SettingsService;
import Service.ThemeService;
import Util.ColorPalette;

public class SettingController {
    private ProductCatalog model;
    private JFrame mainFrame;
    private final SettingsService settingsService;

    public SettingController(ProductCatalog model) {
        this.model = model;
        this.settingsService = new SettingsService();
    }

    public void setMainFrame(JFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void changeThemeByName(String themeName) {
        ThemeService themeService = new ThemeService();

        SplashScreen themeSplash = new SplashScreen();
        themeSplash.setStatus("Applying theme...");
        themeSplash.setProgress(20);

        if (mainFrame != null) {
            mainFrame.setVisible(false);
        }

        themeSplash.setVisible(true);

        SwingWorker<Theme, Void> worker = new SwingWorker<>() {
            @Override
            protected Theme doInBackground() {
                return themeService.loadThemeByName(themeName);
            }

            @Override
            protected void done() {
                try {
                    Theme theme = get();
                    saveSelectedTheme(themeName);
                    changeTheme(theme);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    SwingUtilities.invokeLater(() -> {
                        if (mainFrame != null) {
                            mainFrame.setVisible(true);
                        }
                        SwingUtilities.invokeLater(themeSplash::dispose);
                    });
                }
            }
        };
        worker.execute();
    }

    private void saveSelectedTheme(String themeName) {
        if (themeName == null || themeName.isBlank()) {
            return;
        }
        Settings settings = settingsService.loadSettings();
        settings.setActiveThemeName(themeName);
        settingsService.saveSettings(settings);
    }

    public void changeTheme(Theme theme) {
        if (theme == null) {
            return;
        }
        ColorPalette.getInstance().applyTheme(theme);
        model.updateView();
    }
}
