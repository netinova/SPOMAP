package Components;

import Controller.SidebarController;
import Util.ColorPalette;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class SideNavbarPanel extends JPanel {

    private SidebarController controller;

    public SideNavbarPanel(SidebarController controller) {

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
        this.setBackground(ColorPalette.getInstance().getBgSecondary());
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        createComponents();
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
