package View;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import Components.LogoPanel;
import Components.LogoutPanel;
import Components.SideNavbarPanel;

public class SidebarView extends JPanel {

    private LogoPanel logoPanel = new LogoPanel();
    private SideNavbarPanel sideNavbarPanel = new SideNavbarPanel();
    private LogoutPanel logoutPanel = new LogoutPanel();

    public SidebarView() {
        setupUI();
    }

    private void setupUI() {
        this.setPreferredSize(new Dimension(150, 0));
        this.setLayout(new GridBagLayout());

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
