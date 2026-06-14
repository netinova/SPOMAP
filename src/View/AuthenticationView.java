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
    private LoginPanel loginPanel;
    private CardLayout cardLayout;
    private JPanel cardPanel;

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
            } else if (e.getPropertyName().equals((SingUpPanel.SWITCH_LOGIN_PROP))) {
                controller.onShowLogIn();
            } else if (e.getPropertyName().equals((SingUpPanel.SING_UP_PROP))) {
                controller.onSingUp();
            }
        });

        this.loginPanel.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals(LoginPanel.USERNAME_PROP)) {
                controller.onUsernameLoginChange((String) evt.getNewValue());
            } else if (evt.getPropertyName().equals(LoginPanel.PASSWORD_PROP)) {
                controller.onPasswordLoginChange((String) evt.getNewValue());
            } else if (evt.getPropertyName().equals(LoginPanel.LOGIN_PROP)) {
                controller.onLogin();
            } else if (evt.getPropertyName().equals(LoginPanel.SHOW_SIGNUP_PROP)) {
                controller.onShowSingUp();
            }
        });
    }

    private void setupUI() {
        this.setBackground(ColorPalette.BG_MAIN);
        this.setLayout(new GridBagLayout());

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        cardPanel.setPreferredSize(new Dimension(400, 480));

        singUpPanel = new SingUpPanel();
        singUpPanel.setAuthenticationController(controller); // Connect controller to View

        loginPanel = new LoginPanel();
        loginPanel.setAuthenticationController(controller); // Connect controller to View
        loginPanel.resetForm();
        cardPanel.add(loginPanel, "LOG_IN");
        cardPanel.add(singUpPanel, "SING_UP");

        this.add(cardPanel);
    }

    // getters

    public String getPassword() {
        return singUpPanel != null ? singUpPanel.getPassword() : "";
    }

    public SingUpPanel getSingUpPanel() {
        return singUpPanel;
    }

    public String getLoginUsername() {
        return loginPanel != null ? loginPanel.getUsernameInput() : "";
    }

    public String getLoginPassword() {
        return loginPanel != null ? loginPanel.getPassword() : "";
    }

    public AuthenticationView getView() {
        return this;
    }

    // singUp Errors handler
    public void showPhoneError(String error) {
        if (singUpPanel != null)
            singUpPanel.showPhoneError(error);
    }

    public void clearPhoneError() {
        if (singUpPanel != null)
            singUpPanel.clearPhoneError();
    }

    public void showFirstNameError(String error) {
        if (singUpPanel != null)
            singUpPanel.showFirstNameError(error);
    }

    public void clearFirstNameError() {
        if (singUpPanel != null)
            singUpPanel.clearFirstNameError();
    }

    public void showLastNameError(String error) {
        if (singUpPanel != null)
            singUpPanel.showLastNameError(error);
    }

    public void clearLastNameError() {
        if (singUpPanel != null)
            singUpPanel.clearLastNameError();
    }

    public void showPasswordError(String error) {
        if (singUpPanel != null)
            singUpPanel.showPasswordError(error);
    }

    public void clearPasswordError() {
        if (singUpPanel != null)
            singUpPanel.clearPasswordError();
    }

    public void showConfirmPasswordError(String error) {
        if (singUpPanel != null)
            singUpPanel.showConfirmPasswordError(error);
    }

    public void clearConfirmPasswordError() {
        if (singUpPanel != null)
            singUpPanel.clearConfirmPasswordError();
    }

    public void showFindUsError(String error) {
        if (singUpPanel != null)
            singUpPanel.showFindUsError(error);
    }

    public void clearFindUsError() {
        if (singUpPanel != null)
            singUpPanel.clearFindUsError();
    }

    // logIn Error Handler

    public void showLoginUsernameError(String error) {
        if (loginPanel != null)
            loginPanel.showUsernameError(error);
    }

    public void showLoginPasswordError(String error) {
        if (loginPanel != null)
            loginPanel.showPasswordError(error);
    }

    public boolean validateAndSignUp() {
        if (singUpPanel == null)
            return false;
        String phone = singUpPanel.getPhoneNumber();
        String firstName = singUpPanel.getFirstName();
        String lastName = singUpPanel.getLastName();
        String password = singUpPanel.getPassword();
        String confirmPassword = singUpPanel.getConfirmPassword();
        String findUs = singUpPanel.getFindUs();

        return controller.validateFullSingUpForm(phone, firstName, lastName, password, confirmPassword, findUs);
    }

    // switching
    public void showLoginPanel() {
        cardPanel.setPreferredSize(new Dimension(400, 480));
        loginPanel.resetForm();
        cardLayout.show(cardPanel, "LOG_IN");
    }

    public void showSingUpPanel() {
        cardPanel.setPreferredSize(new Dimension(700, 480));
        singUpPanel.resetForm();
        cardLayout.show(cardPanel, "SING_UP");
    }
}
