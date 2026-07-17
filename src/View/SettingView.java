package View;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import Components.ThemePickerPanel;
import Controller.SettingController;
import Model.ProductCatalog;
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
        // Add theme picker listener
        themePickerPanel.addThemeSelectionListener(e -> {
            String selectedTheme = themePickerPanel.getSelectedThemeName();
            System.out.println("Theme selected: " + selectedTheme);

            controller.changeThemeByName(selectedTheme);
        });
    }

    private void setupUI() {
        this.setBackground(ColorPalette.getInstance().getBgMain());
        this.setLayout(new BorderLayout());

        contentPanel = new JPanel();
        contentPanel.setBackground(ColorPalette.getInstance().getBgMain());
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(ColorPalette.getInstance().getBorder())));

        // Add theme picker component
        themePickerPanel = new ThemePickerPanel();
        themePickerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(themePickerPanel);

        // wrapping the components in a scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.setBackground(ColorPalette.getInstance().getBgMain());

        // Custom scrollbar styling
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        UIUtils.styleScrollBar(verticalBar);

        this.add(scrollPane, BorderLayout.CENTER);
    }

}
