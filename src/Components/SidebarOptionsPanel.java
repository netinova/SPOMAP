package Components;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import Util.ColorPalette;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;

public class SidebarOptionsPanel extends JPanel {

    public interface SidebarButtonListener {
        void onButtonFactorsClick();

        void onSettingsClick();
    }

    private SidebarButtonListener listener;
    public int rounded = 45;

    public void setListener(SidebarButtonListener listener) {
        this.listener = listener;
    }

    public SidebarOptionsPanel() {
        setupUI();
        crateComponents();
    }

    private void setupUI() {
        this.setOpaque(false);
        this.setLayout(new GridBagLayout());
    }

    private void crateComponents() {
        GridBagConstraints gbc = new GridBagConstraints();

        RoundedButton factorButton = new RoundedButton("", rounded);
        File svgFile = new File("icons/invoice.svg");
        FlatSVGIcon factorIcon = new FlatSVGIcon(svgFile).derive(35, 35);

        if (!factorIcon.hasFound()) {
            System.err.println("SVG not found: " + svgFile.getAbsolutePath());
        }
        factorIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> ColorPalette.getInstance().getAccentPrimary()));

        factorButton.setPreferredSize(new Dimension(45, 45));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        factorButton.setIcon(factorIcon);
        this.add(factorButton, gbc);

        factorButton.addActionListener(e -> {
            if (listener != null) {
                listener.onButtonFactorsClick();
            }
        });

        svgFile = new File("icons/settings.svg");
        FlatSVGIcon settingsIcon = new FlatSVGIcon(svgFile).derive(35, 35);

        if (!settingsIcon.hasFound()) {
            System.err.println("SVG not found: " + svgFile.getAbsolutePath());
        }
        settingsIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> ColorPalette.getInstance().getAccentPrimary()));

        RoundedButton settingsButton = new RoundedButton("", rounded);
        settingsButton.setPreferredSize(new Dimension(45, 45));
        settingsButton.setIcon(settingsIcon);

        gbc.gridy = 1;
        this.add(settingsButton, gbc);

        settingsButton.addActionListener(e -> {
            if (listener != null) {
                listener.onSettingsClick();
            }
        });
    }
}
