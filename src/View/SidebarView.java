package View;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import Components.LogoPanel;
import Components.LogoutPanel;
import Components.SideNavbarPanel;
import Controller.SidebarController;
import Util.ColorPalette;

public class SidebarView extends JPanel {

    private LogoPanel logoPanel = new LogoPanel();
    private SideNavbarPanel sideNavbarPanel = new SideNavbarPanel();
    private LogoutPanel logoutPanel = new LogoutPanel();

    private SidebarController controller;

    public SidebarView() {
        setupUI();
        attachEvents();
    }

    private void attachEvents() {

    }

    private void setupUI() {
        this.setPreferredSize(new Dimension(150, 0));
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER));

        GridBagConstraints gbc = new GridBagConstraints();

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
        this.add(logoutPanel, gbc);
    }

}
