package View;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;

import Components.SplashScreen;
import Components.ThemePickerPanel;
import Controller.SettingController;
import Model.ProductCatalog;
import Model.Theme;
import Service.ThemeService;
import Util.ColorPalette;
import Util.UIUtils;

import java.awt.BorderLayout;
import java.awt.Component;

public class SettingView extends JPanel {

    private SettingController controller;

    private JPanel contentPanel;
    private ThemePickerPanel themePickerPanel;

    public SettingView(SettingController controller) {
        this.controller = controller;

        setupUI();
        attachEvents();

        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            attachEvents();
            revalidate();
            repaint();
        });
    }

    private void attachEvents() {
        themePickerPanel.addThemeSelectionListener(e -> {
            String selectedTheme = themePickerPanel.getSelectedThemeName();
            if (selectedTheme == null || selectedTheme.isBlank()) {
                return;
            }
            controller.changeThemeByName(selectedTheme);
        });
    }

    private void setupUI() {
        this.setBackground(ColorPalette.getInstance().getBgMain());
        this.setLayout(new BorderLayout());

        contentPanel = new JPanel();
        contentPanel.setOpaque(true);
        contentPanel.setBackground(ColorPalette.getInstance().getBgSecondary());
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createLineBorder(ColorPalette.getInstance().getBorder()));
        contentPanel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        themePickerPanel = new ThemePickerPanel();
        themePickerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(themePickerPanel);
        contentPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.setBackground(ColorPalette.getInstance().getBgMain());
        scrollPane.getViewport().setBackground(ColorPalette.getInstance().getBgSecondary());

        scrollPane.getViewport().setBackground(ColorPalette.getInstance().getBgMain());
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        UIUtils.styleScrollBar(verticalBar);

        this.add(scrollPane, BorderLayout.CENTER);
    }

}
