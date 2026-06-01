package View;

import javax.swing.JPanel;

import Components.LoginPanel;
import Components.SingUpPanel;
import Util.ColorPalette;

import java.awt.*;

public class UserView extends JPanel {
    private SingUpPanel singUpPanel;
//    private LoginPanel loginPanel;

    public UserView() {
        this.setBackground(ColorPalette.BG_MAIN);
        this.setLayout(new GridBagLayout());
        singUpPanel = new SingUpPanel();
        this.add(singUpPanel);
//        loginPanel = new LoginPanel();
//        this.add(loginPanel);
    }
}
