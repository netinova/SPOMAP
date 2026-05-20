package Components;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import Util.ColorPalette;

public class SideNavbarPanel extends JPanel {

    RoundedButton userPanelButton = new RoundedButton("user", 0);

    public SideNavbarPanel() {
        this.setBackground(ColorPalette.BG_SECONDARY);
        userPanelButton.setPreferredSize(new Dimension(40, 40));
        this.add(userPanelButton);
    }

    public void addUserPanelButtonListener(ActionListener listener) {
        userPanelButton.addActionListener(listener);
    }

}
