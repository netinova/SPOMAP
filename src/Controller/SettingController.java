package Controller;

import Model.ProductCatalog;
import Model.Theme;
import Service.ThemeService;
import Util.ColorPalette;

public class SettingController {
    private ProductCatalog model;

    public SettingController(ProductCatalog model) {
        this.model = model;
    }

    public void changeThemeByName(String themeName) {
        ThemeService themeService = new ThemeService();

        Theme newTheme = themeService.loadThemeByName(themeName);
        ColorPalette.getInstance().applyTheme(newTheme);

        model.updateView();
    }
}
