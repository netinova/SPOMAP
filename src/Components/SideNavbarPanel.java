package Components;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import Util.ColorPalette;

public class SideNavbarPanel extends JPanel {

    RoundedButton userPanelButton = new RoundedButton("user", 0);

    public SideNavbarPanel() {
        setupUI();
        createComponents();
    }

    private void setupUI() {
        this.setBackground(ColorPalette.BG_SECONDARY);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        userPanelButton.setPreferredSize(new Dimension(40, 40));
    }

    private void createComponents() {
//        this.add(Box.createVerticalStrut(5));
        // button factors
        SidebarOptionsPanel sidebarOptionsPanel = new SidebarOptionsPanel();
        sidebarOptionsPanel.setAlignmentX(CENTER_ALIGNMENT);
        this.add(sidebarOptionsPanel);

        this.add(Box.createVerticalStrut(10));


    }

    public void addUserPanelButtonListener(ActionListener listener) {
        userPanelButton.addActionListener(listener);
    }

}
