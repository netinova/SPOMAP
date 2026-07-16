package Components;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Util.ColorPalette;
import Util.UIUtils;
import Controller.AuthenticationController;

public class SingUpPanel extends JPanel {

    private RoundedInputText firstName;
    private RoundedInputText lastName;
    private RoundedInputText phoneNumber;
    private RoundedInputPassword passwordFiled;
    private RoundedInputPassword confirmPasswordFiled;
    private RoundedComboBox<String> foundUSComboBox;
    private RoundedButton singUpButton;
    private RoundedButton logInButton;

    // FormTextFiledPanel
    private FormTextFiledPanel phonePanel;
    private FormTextFiledPanel firstNamePanel;
    private FormTextFiledPanel lastNamePanel;
    private FormTextFiledPanel passwordPanel;
    private FormTextFiledPanel confirmPasswordPanel;
    private FormTextFiledPanel foundUsPanel;

    // Controller for validation
    private AuthenticationController authController;

    public int cornerRadius = 20;

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void setAuthenticationController(AuthenticationController controller) {
        this.authController = controller;
    }

    public static final String PHONE_NUMBER_PROP = "phoneNumber";
    public static final String PASSWORD_PROP = "passwordFiled";
    public static final String PASSWORD_CONFIRM_PROP = "confirmPasswordFiled";
    public static final String F_NAME_PROP = "firstName";
    public static final String L_NAME_PROP = "lastName";
    public static final String FOUND_US_PROP = "foundUSComboBox";
    public static final String SING_UP_PROP = "singUp";
    public static final String SWITCH_LOGIN_PROP = "logIn";

    public SingUpPanel() {
        setupUI();
        createComponents();
        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            revalidate();
            repaint();
        });
    }

    private void setupUI() {
        setOpaque(false);
        this.setPreferredSize(new Dimension(700, 480));
        this.setMinimumSize(new Dimension(700, 480));
        this.setLayout(new GridBagLayout());
    }

    private void createComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(3, 20, 3, 20);

        // logo Image
        ImageIcon logo = new ImageIcon("icons/SPOMAP_Default_White color.png");
        Image resizeLogo = logo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        logo = new ImageIcon(resizeLogo);
        JLabel logoLabel = new JLabel("Sing Up");
        logoLabel.setForeground(ColorPalette.getInstance().getTextPrimary());
        logoLabel.setFont(new Font("Calibri (Body)", Font.BOLD, 40));
        logoLabel.setIcon(logo);
        logoLabel.setHorizontalAlignment(JLabel.CENTER);

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        this.add(logoLabel, gbc);
        gbc.gridwidth = 1;

        // Row 1
        gbc.gridy = 1;

        // left
        gbc.gridx = 0;
        phoneNumber = new RoundedInputText("09xxxxxxxxx", 5);
        phoneNumber.setMinimumSize(new Dimension(300, 40));
        phonePanel = new FormTextFiledPanel("Username(Phone number)", phoneNumber, "phoneNumber");
        phoneNumber.addActionListener(e -> {
            // View is dumb: just pass to controller for validation
            if (authController != null) {
                var result = authController.validatePhoneNumber(phoneNumber.getText());
                if (result.isValid())
                    phonePanel.clearError();
                else
                    phonePanel.setError(result.getErrorMessage());
            }
            support.firePropertyChange(PHONE_NUMBER_PROP, null, phoneNumber.getText());
        });
        this.add(phonePanel, gbc);

        // right
        gbc.gridx = 1;
        firstName = new RoundedInputText("First Name", 5);
        firstName.setMinimumSize(new Dimension(300, 40));
        firstNamePanel = new FormTextFiledPanel("First name", firstName, "firstName");
        firstName.addActionListener(e -> {
            // View is dumb: just pass to controller for validation
            if (authController != null) {
                var result = authController.validateFirstName(firstName.getText());
                if (result.isValid())
                    firstNamePanel.clearError();
                else
                    firstNamePanel.setError(result.getErrorMessage());
            }
            support.firePropertyChange(F_NAME_PROP, null, firstName.getText());
        });
        this.add(firstNamePanel, gbc);

        gbc.gridy = 2;

        gbc.gridx = 0;
        passwordFiled = new RoundedInputPassword("Password", 5);
        passwordFiled.setMinimumSize(new Dimension(300, 40));
        passwordPanel = new FormTextFiledPanel("Password", passwordFiled, "password");
        passwordFiled.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validatePassword();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validatePassword();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validatePassword();
            }

            private void validatePassword() {
                String password = new String(passwordFiled.getPassword());
                if (authController != null) {
                    var result = authController.validatePassword(password);
                    if (result.isValid())
                        passwordPanel.clearError();
                    else
                        passwordPanel.setError(result.getErrorMessage());
                }
                support.firePropertyChange(PASSWORD_PROP, null, password);
            }
        });
        this.add(passwordPanel, gbc);

        gbc.gridx = 1;
        lastName = new RoundedInputText("Last Name", 5);
        lastName.setMinimumSize(new Dimension(300, 40));
        lastNamePanel = new FormTextFiledPanel("Last Name", lastName, "lastName");
        lastName.addActionListener(e -> {
            // View is dumb: just pass to controller for validation
            if (authController != null) {
                var result = authController.validateLastName(lastName.getText());
                if (result.isValid())
                    lastNamePanel.clearError();
                else
                    lastNamePanel.setError(result.getErrorMessage());
            }
            support.firePropertyChange(L_NAME_PROP, null, lastName.getText());
        });
        this.add(lastNamePanel, gbc);

        gbc.gridy = 3;

        gbc.gridx = 0;
        confirmPasswordFiled = new RoundedInputPassword("Repeat Password", 5);
        confirmPasswordFiled.setMinimumSize(new Dimension(300, 40));
        confirmPasswordPanel = new FormTextFiledPanel("Confirm Password", confirmPasswordFiled, "confirmPassword");
        confirmPasswordFiled.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateConfirmPassword();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateConfirmPassword();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateConfirmPassword();
            }

            private void validateConfirmPassword() {
                String password = new String(passwordFiled.getPassword());
                String ConfirmPassword = new String(confirmPasswordFiled.getPassword());

                if (authController != null) {
                    var result = authController.validateConfirmPassword(password, ConfirmPassword);
                    if (result.isValid())
                        confirmPasswordPanel.clearError();
                    else
                        confirmPasswordPanel.setError(result.getErrorMessage());
                }
                support.firePropertyChange(PASSWORD_CONFIRM_PROP, null, ConfirmPassword);

            }
        });
        confirmPasswordFiled.addActionListener(e -> {
            String confirmPassword = new String(confirmPasswordFiled.getPassword());
            support.firePropertyChange(PASSWORD_CONFIRM_PROP, null, confirmPassword);
        });
        this.add(confirmPasswordPanel, gbc);

        gbc.gridx = 1;
        String[] optionFoundUS = { "Select an option", "Google", "Social Media", "Friend", "Advertisement", "Other" };
        foundUSComboBox = new RoundedComboBox<String>(optionFoundUS);
        foundUSComboBox.setMinimumSize(new Dimension(300, 40));
        foundUsPanel = new FormTextFiledPanel("How did you find us?", foundUSComboBox, "findUs");
        foundUSComboBox.addActionListener(e -> {
            String findUs = (String) foundUSComboBox.getSelectedItem();
            // View is dumb: just pass to controller for validation
            if (authController != null) {
                var result = authController.validateFoundUs(findUs);
                if (result.isValid())
                    foundUsPanel.clearError();
                else
                    foundUsPanel.setError(result.getErrorMessage());
            }
            support.firePropertyChange(FOUND_US_PROP, null, findUs);
        });
        this.add(foundUsPanel, gbc);

        gbc.gridy = 4;

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 20, 8, 20);
        gbc.fill = GridBagConstraints.BOTH;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setMinimumSize(new Dimension(640, 100));

        singUpButton = new RoundedButton("Sing Up", 20);
        singUpButton.setPreferredSize(new Dimension(300, 40));
        singUpButton.setMinimumSize(new Dimension(300, 40));
        singUpButton.setMaximumSize(new Dimension(300, 40));

        JLabel orLabel = new JLabel("or");
        orLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        orLabel.setForeground(ColorPalette.getInstance().getTextMuted());
        orLabel.setHorizontalAlignment(SwingConstants.CENTER);
        orLabel.setPreferredSize(new Dimension(40, 45));

        logInButton = new RoundedButton("Log in", 20);
        logInButton.setMinimumSize(new Dimension(300, 40));
        logInButton.setMaximumSize(new Dimension(300, 40));
        logInButton.setPreferredSize(new Dimension(300, 40));

        singUpButton.addActionListener(e -> {
            if (authController != null) {
                String phone = phoneNumber.getText();
                String fName = firstName.getText();
                String lName = lastName.getText();
                String pass = new String(passwordFiled.getPassword());
                String passConfirm = new String(confirmPasswordFiled.getPassword());
                String findUs = (String) foundUSComboBox.getSelectedItem();

                boolean isValid = authController.validateFullSingUpForm(phone, fName, lName, pass, passConfirm, findUs);

                if (isValid)
                    support.firePropertyChange(SING_UP_PROP, null, null);
            }
        });

        logInButton.addActionListener(e -> {
            support.firePropertyChange(SWITCH_LOGIN_PROP, null, null);
        });

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(singUpButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(orLabel);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(logInButton);
        buttonPanel.add(Box.createHorizontalGlue());

        this.add(buttonPanel, gbc);
    }

    public void showPhoneError(String error) {
        phonePanel.setError(error);
    }

    public void clearPhoneError() {
        phonePanel.clearError();
    }

    public void showFirstNameError(String error) {
        firstNamePanel.setError(error);
    }

    public void clearFirstNameError() {
        firstNamePanel.clearError();
    }

    public void showLastNameError(String error) {
        lastNamePanel.setError(error);
    }

    public void clearLastNameError() {
        lastNamePanel.clearError();
    }

    public void showPasswordError(String error) {
        passwordPanel.setError(error);
    }

    public void clearPasswordError() {
        passwordPanel.clearError();
    }

    public void showConfirmPasswordError(String error) {
        confirmPasswordPanel.setError(error);
    }

    public void clearConfirmPasswordError() {
        confirmPasswordPanel.clearError();
    }

    public void showFindUsError(String error) {
        foundUsPanel.setError(error);
    }

    public void clearFindUsError() {
        foundUsPanel.clearError();
    }

    // Getters

    public String getPhoneNumber() {
        return phoneNumber.getText();
    }

    public String getFirstName() {
        return firstName.getText();
    }

    public String getLastName() {
        return lastName.getText();
    }

    public String getPassword() {
        return new String(passwordFiled.getPassword());
    }

    public String getConfirmPassword() {
        return new String(confirmPasswordFiled.getPassword());
    }

    public String getFindUs() {
        return (String) foundUSComboBox.getSelectedItem();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(ColorPalette.getInstance().getBgMain());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Draw border
        g2.setColor(ColorPalette.getInstance().getBorder());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        g2.dispose();

        // Paint the text
        super.paintComponent(g);
    }

    public void resetForm() {
        phoneNumber.setActivePlaceHolder(true);
        firstName.setActivePlaceHolder(true);
        lastName.setActivePlaceHolder(true);
        passwordFiled.setActivePlaceHolder(true);
        confirmPasswordFiled.setActivePlaceHolder(true);
        foundUSComboBox.setSelectedIndex(0);
        clearPhoneError();
        clearFirstNameError();
        clearLastNameError();
        clearPasswordError();
        clearConfirmPasswordError();
        clearFindUsError();
    }
}
