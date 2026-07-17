package Components;

import Controller.AuthenticationController;
import Util.ColorPalette;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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

public class LoginPanel extends JPanel {
    private int cornerRadius = 10;

    // Components
    private RoundedInputText usernameInput;
    private RoundedInputPassword passwordInput;
    private RoundedButton loginButton;
    private RoundedButton signUpButton;

    // Form panels for error display
    private FormTextFiledPanel usernamePanel;
    private FormTextFiledPanel passwordPanel;

    // Controller and property support
    private AuthenticationController authController;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public static final String LOGIN_PROP = "login";
    public static final String SHOW_SIGNUP_PROP = "showSignUp";
    public static final String USERNAME_PROP = "username";
    public static final String PASSWORD_PROP = "password";

    public LoginPanel() {
        setupUI();
        createComponents();
        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            revalidate();
            repaint();
        });
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void setAuthenticationController(AuthenticationController controller) {
        this.authController = controller;
    }

    private void setupUI() {
        setOpaque(false);
        this.setPreferredSize(new Dimension(350, 480));
        this.setLayout(new GridBagLayout());
    }

    private void createComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 20, 5, 20);

        // logo Image
        ImageIcon logo = new ImageIcon("icons/SPOMAP_Default_White color.png");
        Image resizeLogo = logo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        logo = new ImageIcon(resizeLogo);
        JLabel logoLabel = new JLabel("Log In");
        logoLabel.setForeground(ColorPalette.getInstance().getTextPrimary());
        logoLabel.setFont(new Font("Calibri (Body)", Font.BOLD, 50));
        logoLabel.setIcon(logo);
        logoLabel.setHorizontalAlignment(JLabel.CENTER);

        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        this.add(logoLabel, gbc);

        // Username Field
        gbc.gridy = 1;
        usernameInput = new RoundedInputText("Username / Phone number", 5);
        usernameInput.setPreferredSize(new Dimension(300, 40));
        usernamePanel = new FormTextFiledPanel("Username", usernameInput, "username");
        usernameInput.addActionListener(e -> {
            String username = usernameInput.getText();
            if (authController != null) {
                var result = authController.validatePhoneNumber(username);
                if (!result.isValid()) {
                    usernamePanel.setError(result.getErrorMessage());
                } else {
                    usernamePanel.clearError();
                }
                support.firePropertyChange(USERNAME_PROP, null, username);
            }
        });
        this.add(usernamePanel, gbc);

        // Password Field
        gbc.gridy = 2;
        passwordInput = new RoundedInputPassword("Password", 5);
        passwordInput.setPreferredSize(new Dimension(300, 40));
        passwordPanel = new FormTextFiledPanel("Password", passwordInput, "password");
        passwordInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validPassword();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validPassword();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validPassword();
            }

            private void validPassword() {
                String password = new String(passwordInput.getPassword());
                if (authController != null) {
                    var result = authController.validateLoginPassword(password);
                    if (!result.isValid()) {
                        passwordPanel.setError(result.getErrorMessage());
                    } else {
                        passwordPanel.clearError();
                    }
                    support.firePropertyChange(PASSWORD_PROP, null, password);
                }
            }
        });
        this.add(passwordPanel, gbc);

        // submit Button
        loginButton = new RoundedButton("Log in", 25);
        loginButton.setPreferredSize(new Dimension(280, 45));

        gbc.gridy = 3;
        gbc.insets = new Insets(5, 20, 5, 20);
        this.add(loginButton, gbc);

        gbc.gridy = 4;
        JLabel orTextLabel = new JLabel("or");
        orTextLabel.setForeground(ColorPalette.getInstance().getTextMuted());
        orTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        orTextLabel.setFont(new Font("Arial", Font.PLAIN, 17));
        this.add(orTextLabel, gbc);

        gbc.gridy = 5;
        signUpButton = new RoundedButton("Sing up", 25);

        signUpButton.setPreferredSize(new Dimension(280, 45));
        this.add(signUpButton, gbc);

        loginButton.addActionListener(e -> {
            if (authController != null) {
                String username = usernameInput.getText();
                String password = new String(passwordInput.getPassword());

                boolean isValid = authController.validateFullLogin(username, password);
                if (isValid) {
                    support.firePropertyChange(LOGIN_PROP, null, null);
                }
            }
        });

        signUpButton.addActionListener(e -> {
            support.firePropertyChange(SHOW_SIGNUP_PROP, null, null);
        });
    }

    // getters

    public String getUsernameInput() {
        return usernameInput.getText();
    }

    public String getPassword() {
        return new String(passwordInput.getPassword());
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

    // Reset form
    public void resetForm() {
        usernameInput.setActivePlaceHolder(true);
        passwordInput.setActivePlaceHolder(true);
        usernamePanel.clearError();
        passwordPanel.clearError();
    }

    public void showUsernameError(String error) {
        usernamePanel.setError(error);
    }

    public void showPasswordError(String error) {
    }
}
