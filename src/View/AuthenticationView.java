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
                controller.onPhoneNumberChange((String) e.getNewValue());
            } else if (e.getPropertyName().equals(SingUpPanel.PASSWORD_PROP)) {
                controller.onPasswordChange((String) e.getNewValue());
            } else if (e.getPropertyName().equals(SingUpPanel.PASSWORD_CONFIRM_PROP)) {
                controller.onConfirmPasswordChange((String) e.getNewValue());
            } else if (e.getPropertyName().equals(SingUpPanel.L_NAME_PROP)) {
                controller.onLastNameChange((String) e.getNewValue());
            } else if (e.getPropertyName().equals(SingUpPanel.F_NAME_PROP)) {
                controller.onFirstNameChange((String) e.getNewValue());
            } else if (e.getPropertyName().equals(SingUpPanel.FOUND_US_PROP)) {
                controller.onFoundUsChange((String) e.getNewValue());
            }

        });

    }

    private void setupUI() {
        this.setBackground(ColorPalette.BG_MAIN);
        this.setLayout(new GridBagLayout());
        singUpPanel = new SingUpPanel();
        singUpPanel.setAuthenticationController(controller);  // Connect controller to View
        this.add(singUpPanel);
        // loginPanel = new LoginPanel();
        // this.add(loginPanel);
    }
}
