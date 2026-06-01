package View;

import javax.swing.JPanel;

import Components.LoginPanel;
import Components.SingUpPanel;
import Controller.AuthenticationController;
import Util.ColorPalette;

import java.awt.*;

public class AuthenticationView extends JPanel {

    private AuthenticationController controller;

    private SingUpPanel singUpPanel;
    // private LoginPanel loginPanel;

    public AuthenticationView(AuthenticationController controller) {

        this.controller = controller;

        setupUI();
        attachEvents();
    }

    private void attachEvents() {
        this.singUpPanel.addPropertyChangeListener(e -> {

            if (e.getPropertyName().equals(SingUpPanel.PHONE_NUMBER_PROP)) {
                controller.usernameValueChange((String) e.getNewValue());
            }

        });

    }

    private void setupUI() {
        this.setBackground(ColorPalette.BG_MAIN);
        this.setLayout(new GridBagLayout());
        singUpPanel = new SingUpPanel();
        this.add(singUpPanel);
        // loginPanel = new LoginPanel();
        // this.add(loginPanel);
    }
}
