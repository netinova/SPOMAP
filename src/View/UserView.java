package View;

import javax.swing.JPanel;

import Components.LoginPanel;
import Util.ColorPalette;

import java.awt.*;

public class UserView extends JPanel {
    private LoginPanel loginPanel;

    public UserView() {
        this.setBackground(ColorPalette.BG_MAIN);
        this.setLayout(new GridBagLayout());
        loginPanel = new LoginPanel();
//        singUpPanel.setLayout(new GridBagLayout());
        this.add(loginPanel);
    }
}
