package Components;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class SideNavbarPanel extends JPanel {

    RoundedButton userPanelButton = new RoundedButton("user", 40);

    public SideNavbarPanel() {
        this.setBackground(Color.red);
        this.add(userPanelButton);
    }

    public void addUserPanelButtonListener(ActionListener listener) {
        userPanelButton.addActionListener(listener);
    }

}
