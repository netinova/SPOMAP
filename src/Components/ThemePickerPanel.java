package Components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import Model.Theme;
import Service.SettingsService;
import Service.ThemeService;
import Util.ColorPalette;

public class ThemePickerPanel extends JPanel {

    private JLabel titleLabel;
    private RoundedScrollableComboBox<String> themeComboBox;
    private ThemeService themeService;
    private EventListenerList listenerList = new EventListenerList();

    public ThemePickerPanel() {
        this.themeService = new ThemeService();
        setupUI();
        loadThemes();
        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            loadThemes();
            revalidate();
            repaint();
        });
    }

    private void setupUI() {
        this.setBackground(ColorPalette.getInstance().getBgSecondary());
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        titleLabel = new JLabel("Theme");
        titleLabel.setForeground(ColorPalette.getInstance().getTextPrimary());
        titleLabel.setFont(titleLabel.getFont().deriveFont(12f));
        titleLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.add(titleLabel);

        this.add(Box.createHorizontalStrut(16));

        themeComboBox = new RoundedScrollableComboBox<>(new String[] {});
        themeComboBox.setMaxVisibleItems(5);
        themeComboBox.setAlignmentY(Component.CENTER_ALIGNMENT);
        themeComboBox.setPreferredSize(new Dimension(180, 30));
        themeComboBox.setMaximumSize(new Dimension(180, 30));

        this.add(themeComboBox);
        this.add(Box.createHorizontalGlue());
    }

    private void loadThemes() {
        try {
            List<Theme> themes = themeService.loadAllThemes();
            themeComboBox.removeAllItems();

            for (Theme theme : themes) {
                if (theme != null && theme.getName() != null) {
                    themeComboBox.addItem(theme.getName());
                }
            }

            String activeThemeName = new SettingsService().loadSettings().getActiveThemeName();
            if (activeThemeName != null) {
                themeComboBox.setSelectedItem(activeThemeName);
            } else if (themeComboBox.getItemCount() > 0) {
                themeComboBox.setSelectedIndex(0);
            }

        } catch (Exception e) {
            System.err.println("Failed to load themes: " + e.getMessage());
        }
    }

    public void addThemeSelectionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
        themeComboBox.addActionListener(e -> {
            listener.actionPerformed(e);
        });
    }

    public String getSelectedThemeName() {
        Object selected = themeComboBox.getSelectedItem();
        return selected != null ? selected.toString() : null;
    }

    public void setSelectedTheme(String themeName) {
        themeComboBox.setSelectedItem(themeName);
    }
}
