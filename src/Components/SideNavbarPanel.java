package Components;

import java.awt.*;

import javax.swing.*;

import Controller.SidebarController;
import Util.ColorPalette;

public class SideNavbarPanel extends JPanel {

    private SidebarController controller;

    public SideNavbarPanel(SidebarController controller) {

        this.controller = controller;
        setupUI();
        createComponents();
    }

    private void setupUI() {
        this.setBackground(ColorPalette.BG_SECONDARY);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private void createComponents() {
        // button factors
        SidebarOptionsPanel sidebarOptionsPanel = new SidebarOptionsPanel();
        sidebarOptionsPanel.setListener(new SidebarOptionsPanel.SidebarButtonListener() {
            @Override
            public void onButtonFactorsClick() {
                controller.HandelButtonOptionalClick(0);
            }

            @Override
            public void onSettingsClick() {
                controller.HandelButtonOptionalClick(2);
            }
        });
        sidebarOptionsPanel.setAlignmentX(CENTER_ALIGNMENT);
        this.add(sidebarOptionsPanel);

        this.add(Box.createVerticalStrut(10));

    }
}
