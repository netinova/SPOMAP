package Components;

import Util.ColorPalette;

import javax.swing.*;
import java.awt.*;

public class SingUpPanel extends JPanel {

    private RoundedInputText firstName;
    private RoundedInputText lastName;
    private RoundedInputText phoneNumber;
    private RoundedInputPassword passwordFiled;
    private RoundedInputPassword confirmPasswordFiled;
    private RoundedComboBox foundUSComboBox;
    private RoundedButton singUpButton;
    private RoundedButton singInButton;
    public int cornerRadius= 20;

    public SingUpPanel() {
        setupUI();
        createComponents();
    }

    private void setupUI() {
        setOpaque(false);
        this.setPreferredSize(new Dimension(700,480));
        this.setMinimumSize(new Dimension(700,480));
        this.setLayout(new GridBagLayout());
    }

    private void createComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(8, 20, 8, 20);


        //logo Image
        ImageIcon logo = new ImageIcon("icons/SPOMAP_Default_White color.png");
        Image resizeLogo = logo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        logo = new ImageIcon(resizeLogo);
        JLabel logoLabel = new JLabel("Sing Up");
        logoLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        logoLabel.setFont(new Font("Calibri (Body)", Font.BOLD, 40));
        logoLabel.setIcon(logo);
        logoLabel.setHorizontalAlignment(JLabel.CENTER);

        gbc.gridx = 0;
        gbc.gridwidth=2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        this.add(logoLabel, gbc);
        gbc.gridwidth=1;


        // Row 1
        gbc.gridy = 1;

        // left
        gbc.gridx = 0;
        phoneNumber = new RoundedInputText("Phone Number", 5);
        phoneNumber.setMinimumSize(new Dimension(300, 40));
        this.add(new FormTextFiledPanel("Username", phoneNumber), gbc);

        // right
        gbc.gridx = 1;
        firstName = new RoundedInputText("first name",5);
        firstName.setMinimumSize(new Dimension(300,40));
        this.add(new FormTextFiledPanel("First name", firstName), gbc);

        gbc.gridy=2;

        gbc.gridx=0;
        passwordFiled = new RoundedInputPassword("Password",5);
        passwordFiled.setMinimumSize(new Dimension(300,40));
        this.add(new FormTextFiledPanel("Password", passwordFiled), gbc);

        gbc.gridx=1;
        lastName = new RoundedInputText("Last Name",5);
        lastName.setMinimumSize(new Dimension(300,40));
        this.add(new FormTextFiledPanel("Last Name", lastName), gbc);

        gbc.gridy=3;

        gbc.gridx=0;
        confirmPasswordFiled = new RoundedInputPassword("Repeat Password",5);
        confirmPasswordFiled.setMinimumSize(new Dimension(300,40));
        this.add(new FormTextFiledPanel("Confirm Password", confirmPasswordFiled), gbc);

        gbc.gridx=1;
        String[] optionFoundUS= {"Select an option", "Google", "Social Media", "Friend", "Advertisement", "Other"};
        foundUSComboBox = new RoundedComboBox(optionFoundUS);
        foundUSComboBox.setMinimumSize(new Dimension(300,40));
        this.add(new FormTextFiledPanel("How did you find us?", foundUSComboBox), gbc);

        gbc.gridy=4;

        gbc.gridx=0;
        gbc.gridwidth=2;
        gbc.insets = new Insets(20, 20, 8, 20);
        gbc.fill = GridBagConstraints.BOTH;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setMinimumSize(new Dimension(640,100));

        singUpButton = new RoundedButton("Sing Up" , 20);
        singUpButton.setPreferredSize(new Dimension(300,40));
        singUpButton.setMinimumSize(new Dimension(300,40));
        singUpButton.setMaximumSize(new Dimension(300,40));

        JLabel orLabel = new JLabel("or");
        orLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        orLabel.setForeground(ColorPalette.TEXT_MUTED);
        orLabel.setHorizontalAlignment(SwingConstants.CENTER);
        orLabel.setPreferredSize(new Dimension(40, 45));

        singInButton = new RoundedButton("Sing in" , 20);
        singInButton.setMinimumSize(new Dimension(300,40));
        singInButton.setMaximumSize(new Dimension(300,40));
        singInButton.setPreferredSize(new Dimension(300,40));

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(singUpButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(orLabel);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(singInButton);
        buttonPanel.add(Box.createHorizontalGlue());


        this.add(buttonPanel,gbc);
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
