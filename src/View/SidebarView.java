package View;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import Components.LogoPanel;
import Components.LogoutPanel;
import Components.SideNavbarPanel;
import Util.ColorPalette;

public class SidebarView extends JPanel {

    private LogoPanel logoPanel = new LogoPanel();
    private SideNavbarPanel sideNavbarPanel = new SideNavbarPanel();
    private LogoutPanel logoutPanel = new LogoutPanel();

    public SidebarView() {
        setupUI();
        attachEvents();
    }

    private void attachEvents() {

    }

    private void setupUI() {
        this.setPreferredSize(new Dimension(250, 0));
        this.setMinimumSize(new Dimension(250, 0));
        this.setLayout(new GridBagLayout());
        this.setBackground(ColorPalette.BG_SECONDARY);

        // border
        Border line = BorderFactory.createLineBorder(ColorPalette.BORDER);
        Border etched = BorderFactory.createEtchedBorder();
        this.setBorder(BorderFactory.createCompoundBorder(line, etched));

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
        this.add(logoutPanel, gbc);
    }

}
