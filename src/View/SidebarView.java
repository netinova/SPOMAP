package View;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import Components.LogoPanel;
import Components.CopyrightPanel;
import Components.SideNavbarPanel;
import Controller.SidebarController;
import Util.ColorPalette;
import Util.UIUtils;

public class SidebarView extends JPanel {

    private SidebarController controller;
    private LogoPanel logoPanel = new LogoPanel();
    private SideNavbarPanel sideNavbarPanel;
    private CopyrightPanel copyrightPanel = new CopyrightPanel();

    public SidebarView(SidebarController controller) {
        this.controller = controller;
        this.sideNavbarPanel = new SideNavbarPanel(this.controller);
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
        logoPanel.setListener(new LogoPanel.LogoListener() {
            @Override
            public void onClickLogo() {
                controller.HandelLogoSidebar();
            }
        });
    }

    private void setupUI() {
        this.setPreferredSize(new Dimension(100, 0));
        this.setMinimumSize(new Dimension(100, 0));
        this.setLayout(new GridBagLayout());
        this.setBackground(ColorPalette.getInstance().getBgSecondary());
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, ColorPalette.getInstance().getBorder()));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 0;
        this.add(logoPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        this.add(sideNavbarPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.weighty = 0;
        this.add(copyrightPanel, gbc);
    }

}
