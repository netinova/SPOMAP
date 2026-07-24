// Components/EditProfilePanel.java
package Components;

import Controller.UserProfileController;
import Util.ColorPalette;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class UserProfileEditPanel extends JPanel {

    private RoundedInputText firstNameField;
    private RoundedInputText lastNameField;
    private RoundedInputText phoneNumberField;
    private RoundedInputPassword currentPasswordField;
    private RoundedInputPassword newPasswordField;
    private RoundedInputPassword confirmPasswordField;

    private FormTextFiledPanel firstNamePanel;
    private FormTextFiledPanel lastNamePanel;
    private FormTextFiledPanel phoneNumberPanel;
    private FormTextFiledPanel currentPasswordPanel;
    private FormTextFiledPanel newPasswordPanel;
    private FormTextFiledPanel confirmPasswordPanel;

    private RoundedButton saveButton;
    private RoundedButton cancelButton;

    private String currentPassword = "";
    private String newPassword = "";
    private String confirmPassword = "";
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    private UserProfileController controller;

    public static final String PHONE_NUMBER_PROP = "phoneNumber";
    public static final String PASSWORD_CURRENT_PROP = "passwordCurrent";
    public static final String PASSWORD_PROP = "password";
    public static final String PASSWORD_CONFIRM_PROP = "confirmPassword";
    public static final String F_NAME_PROP = "firstName";
    public static final String L_NAME_PROP = "lastName";
    public static final String SAVE_PROP = "save";
    public static final String CANCEL_PROP = "cancel";

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void setController(UserProfileController controller) {
        this.controller = controller;
    }

    public UserProfileEditPanel() {
        setupUI();

        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            revalidate();
            repaint();
        });
    }

    private void setupUI() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 320, 10, 320));

        createForm();
        createButtons();
    }

    private void createForm() {
        // First Name
        firstNameField = new RoundedInputText("First Name", 5);
        firstNamePanel = new FormTextFiledPanel("First Name", firstNameField, F_NAME_PROP);
        firstNameField.addActionListener(e -> {
            if (controller != null) {
                var result = controller.validateFirstName(firstNameField.getText());
                if (result.isValid()) {
                    firstNamePanel.clearError();
                } else
                    firstNamePanel.setError(result.getErrorMessage());

                support.firePropertyChange(F_NAME_PROP, null, firstNameField.getText());
            }
        });
        this.add(firstNamePanel);

        // Last Name
        lastNameField = new RoundedInputText("Last Name", 5);
        lastNamePanel = new FormTextFiledPanel("Last Name", lastNameField, L_NAME_PROP);
        lastNameField.addActionListener(e -> {
            if (controller != null) {
                var result = controller.validateLastName(lastNameField.getText());
                if (result.isValid())
                    lastNamePanel.clearError();
                else
                    lastNamePanel.setError(result.getErrorMessage());

                support.firePropertyChange(L_NAME_PROP, null, lastNameField.getText());

            }
        });
        this.add(lastNamePanel);

        // Phone Number
        phoneNumberField = new RoundedInputText("Username / Phone number", 5);
        phoneNumberPanel = new FormTextFiledPanel("Phone Number", phoneNumberField, PHONE_NUMBER_PROP);
        phoneNumberField.addActionListener(e -> {
            if (controller != null) {
                var result = controller.validatePhoneNumber(phoneNumberField.getText());
                if (result.isValid())
                    phoneNumberPanel.clearError();
                else
                    phoneNumberPanel.setError(result.getErrorMessage());

                support.firePropertyChange(PHONE_NUMBER_PROP, null, phoneNumberField.getText());
            }
        });
        this.add(phoneNumberPanel);

        // password

        currentPasswordField = new RoundedInputPassword("Current Password", 5);
        currentPasswordPanel = new FormTextFiledPanel("Current Password", currentPasswordField, PASSWORD_CURRENT_PROP);
        currentPasswordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validator();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validator();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validator();
            }

            private void validator() {
                currentPassword = (new String(currentPasswordField.getPassword()).equals("Current Password")) ? ""
                        : new String(currentPasswordField.getPassword());
                support.firePropertyChange(PASSWORD_CURRENT_PROP, null, currentPassword);
            }
        });
        this.add(currentPasswordPanel);

        newPasswordField = new RoundedInputPassword("Password", 5);
        newPasswordPanel = new FormTextFiledPanel("New Password", newPasswordField, PASSWORD_PROP);
        newPasswordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validator();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validator();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validator();
            }

            public void validator() {
                newPassword = (new String(newPasswordField.getPassword()).equals("Password")) ? ""
                        : new String(newPasswordField.getPassword());
                var result = controller.validatePassword(newPassword);
                if (result.isValid() || currentPassword.isEmpty())
                    newPasswordPanel.clearError();
                else
                    newPasswordPanel.setError(result.getErrorMessage());

                support.firePropertyChange(PASSWORD_PROP, null, newPassword);
            }
        });
        this.add(newPasswordPanel);

        confirmPasswordField = new RoundedInputPassword("Repeat Password", 5);
        confirmPasswordPanel = new FormTextFiledPanel("Confirm Password", confirmPasswordField, "confirmPassword");
        confirmPasswordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validator();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validator();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validator();
            }

            public void validator() {
                confirmPassword = (new String(confirmPasswordField.getPassword()).equals("Repeat Password")) ? ""
                        : new String(confirmPasswordField.getPassword());
                var result = controller.validateConfirmPassword(newPassword, confirmPassword);
                if (result.isValid() || newPassword.isEmpty())
                    confirmPasswordPanel.clearError();
                else
                    confirmPasswordPanel.setError(result.getErrorMessage());

                support.firePropertyChange(PASSWORD_CONFIRM_PROP, null, confirmPassword);
            }
        });
        this.add(confirmPasswordPanel);

        JPanel buttonPanel = createButtons();
        this.add(buttonPanel);
        saveButton.addActionListener(e -> {
            if (controller.fullValidator(firstNameField.getText(), lastNameField.getText(), phoneNumberField.getText(),
                    currentPassword, newPassword, confirmPassword)) {
                boolean statusEdit = controller.editProfileHandler(firstNameField.getText(), lastNameField.getText(),
                        phoneNumberField.getText(), newPassword);
                if (statusEdit) {
                    controller.loadProfile();
                    controller.showMainPage();
                }
            }

            support.firePropertyChange(SAVE_PROP, null, null);
        });
        cancelButton.addActionListener(e -> {
            controller.showMainPage();
            support.firePropertyChange(CANCEL_PROP, null, null);
        });
        this.add(Box.createVerticalGlue());
    }

    private JPanel createButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        saveButton = new RoundedButton("Save", 15);
        saveButton.setPreferredSize(new Dimension(100, 30));
        saveButton.setBackground(ColorPalette.getInstance().getAccentConfirm());
        saveButton.setForeground(ColorPalette.getInstance().getTextPrimary());

        cancelButton = new RoundedButton("Cancel", 15);
        cancelButton.setPreferredSize(new Dimension(100, 30));
        cancelButton.setBackground(ColorPalette.getInstance().getAccentDanger());
        cancelButton.setHoverColor(ColorPalette.getInstance().getAccentDanger());
        cancelButton.setForeground(ColorPalette.getInstance().getTextPrimary());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.setAlignmentX(LEFT_ALIGNMENT);

        return buttonPanel;
    }

    public void loadUserData(String fName, String lName, String phoneNumber) {
        currentPasswordField.setActivePlaceHolder(true);
        newPasswordField.setActivePlaceHolder(true);
        confirmPasswordField.setActivePlaceHolder(true);

        firstNameField.setActivePlaceHolder(false);
        firstNameField.setText(fName);
        firstNameField.setForeground(ColorPalette.getInstance().getTextPrimary());
        lastNameField.setActivePlaceHolder(false);
        lastNameField.setText(lName);
        lastNameField.setForeground(ColorPalette.getInstance().getTextPrimary());
        phoneNumberField.setActivePlaceHolder(false);
        phoneNumberField.setText(phoneNumber);
        phoneNumberField.setForeground(ColorPalette.getInstance().getTextPrimary());
    }

    // Getters for form data
    public String getFirstName() {
        return firstNameField.getText();
    }

    public String getLastName() {
        return lastNameField.getText();
    }

    public String getPhoneNumber() {
        return phoneNumberField.getText();
    }

    public String getNewPassword() {
        return newPassword;
    }

    // error handler
    public void showPhoneError(String error) {
        phoneNumberPanel.setError(error);
    }

    public void showFirstNameError(String error) {
        lastNamePanel.setError(error);
    }

    public void showLastNameError(String error) {
        lastNamePanel.setError(error);
    }

    public void showCurrentPasswordError(String error) {
        currentPasswordPanel.setError(error);
    }

    public void showNewPasswordError(String error) {
        newPasswordPanel.setError(error);
    }

    public void showConfirmPasswordError(String error) {
        confirmPasswordPanel.setError(error);
    }
}
