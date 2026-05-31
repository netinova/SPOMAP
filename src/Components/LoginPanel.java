package Components;

import Util.ColorPalette;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private int cornerRadius = 10;

    public LoginPanel() {
        setupUI();
        createComponents();
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
        gbc.insets = new Insets(5, 20, 10, 20);

        //logo Image
        ImageIcon logo = new ImageIcon("icons/SPOMAP_Default_White color.png");
        Image resizeLogo = logo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        logo = new ImageIcon(resizeLogo);
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(logo);
        logoLabel.setHorizontalAlignment(JLabel.CENTER);

        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        this.add(logoLabel, gbc);

        //set panel of filed username

        JPanel userPanel = new JPanel();
        userPanel.setOpaque(false);
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setPreferredSize(new Dimension(300, 60));

        //set label of Username
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setForeground(ColorPalette.TEXT_MUTED);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        //user Input Filed
        RoundedInputText usernameInput = new RoundedInputText("username", 5);
        usernameInput.setAlignmentX(Component.LEFT_ALIGNMENT);

        userPanel.add(usernameLabel);
        userPanel.add(Box.createVerticalStrut(5));
        userPanel.add(usernameInput);

        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(userPanel, gbc);

        //set panel of filed password

        JPanel passwordPanel = new JPanel();
        passwordPanel.setOpaque(false);
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
        passwordPanel.setPreferredSize(new Dimension(300, 70));

        //set label of Username
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(ColorPalette.TEXT_MUTED);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        //user Input Filed
        RoundedInputText passwordInput = new RoundedInputText("Password", 5);
        passwordInput.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordPanel.add(Box.createVerticalStrut(5));
        passwordPanel.add(passwordLabel);
        passwordPanel.add(Box.createVerticalStrut(5));
        passwordPanel.add(passwordInput);

        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(passwordPanel, gbc);


        gbc.gridy = 4;
        this.add(Box.createVerticalStrut(5), gbc);

        //submit Button
        RoundedButton submitButton = new RoundedButton("Sing in", 25);
        submitButton.setPreferredSize(new Dimension(280, 45));

        gbc.gridy = 5;
        gbc.insets =new Insets(5, 20, 5, 20);
        this.add(submitButton, gbc);

        gbc.gridy=6;
        JLabel orTextLabel = new JLabel("                              or");
        orTextLabel.setForeground(ColorPalette.TEXT_MUTED);
        orTextLabel.setFont(new Font("Arial", Font.PLAIN, 17));
        this.add(orTextLabel,gbc);

        gbc.gridy = 7;
        RoundedButton singupButton = new RoundedButton("Sing up", 25);

        singupButton.setPreferredSize(new Dimension(280, 45));
        this.add(singupButton, gbc);

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(ColorPalette.BG_MAIN);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Draw border
        g2.setColor(ColorPalette.BORDER);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        g2.dispose();

        // Paint the text
        super.paintComponent(g);
    }
}
