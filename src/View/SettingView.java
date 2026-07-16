package View;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import Controller.SettingController;
import Util.ColorPalette;
import Util.UIUtils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.Scrollbar;

public class SettingView extends JPanel {

    @SuppressWarnings("unused")
    private SettingController controller;

    private JPanel contentPanel;

    public SettingView(SettingController controller) {
        this.controller = controller;

        setupUI();
        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            revalidate();
            repaint();
        });
    }

    private void setupUI() {
        this.setBackground(ColorPalette.getInstance().getBgMain());
        this.setLayout(new BorderLayout());

        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPanel = new JPanel();
        contentPanel.setBackground(ColorPalette.getInstance().getBgSecondary());
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(
                BorderFactory.createLineBorder(ColorPalette.getInstance().getBorder()));

        // wrapping the grid in a scroll pane
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
