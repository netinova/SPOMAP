package View;

import Components.RoundedButton;
import Components.RoundedBorder;
import Components.RoundedPanel;
import Controller.ProfileController;
import Model.AppState;
import Model.User;
import Util.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class UserProfileView extends JPanel {

    private User user;
    private final int borderRadius = 20;

    private RoundedPanel userHeader;
    private RoundedPanel statsMiddle;

    private RoundedButton chargeWallet;
    private RoundedButton logOut;
    private RoundedButton editProfile;

    private JLabel nameLabel;
    private JLabel typeLabel;
    private double balance;

    private ProfileController controller;

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    private static final String EDIT_PROFILE_PROP = "editProfile";
    private static final String CHARGE_VALET_PROP = "chargeValet";
    private static final String LOGOUT_PROP = "logout";


    public void addPropertyChangeListener(PropertyChangeListener listener){
        support.addPropertyChangeListener(listener);
    }

    public void setProfileController(ProfileController profileController){
        controller=profileController;
    }

    public UserProfileView(ProfileController controller) {
        this.controller=controller;

        setupUI();
        attachEvents();
    }

    private void setupUI() {
        setBackground(ColorPalette.BG_MAIN);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(30, 30, 30, 30));

        userHeader = createHeader();
        userHeader.setAlignmentX(LEFT_ALIGNMENT);
        this.add(userHeader);
        this.add(Box.createVerticalStrut(25));

        statsMiddle = createStatsMiddle();
        statsMiddle.setAlignmentX(LEFT_ALIGNMENT);
        this.add(statsMiddle);
        this.add(Box.createVerticalStrut(25));

        this.add(Box.createVerticalGlue());
    }

    private RoundedPanel createHeader() {
        RoundedPanel panel = new RoundedPanel(borderRadius,ColorPalette.BG_SECONDARY,ColorPalette.BORDER);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel userInfo = new JPanel();
        userInfo.setOpaque(false);
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.X_AXIS));

        ImageIcon userIcon = new ImageIcon("icons/male_user.png");
        Image scaledIcon = userIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledIcon));
        userInfo.add(iconLabel);
        userInfo.add(Box.createHorizontalStrut(12));

        JPanel namePanel = new JPanel();
        namePanel.setOpaque(false);
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        nameLabel = new JLabel(String.format("Hi, %s","UserFullName"));
        nameLabel.setFont(new Font("Arial", Font.BOLD, 22));
        nameLabel.setForeground(ColorPalette.TEXT_PRIMARY);

        typeLabel = new JLabel("Normal User");
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        typeLabel.setForeground(ColorPalette.TEXT_MUTED);

        namePanel.add(nameLabel);
        namePanel.add(typeLabel);
        userInfo.add(namePanel);

        panel.add(userInfo, BorderLayout.WEST);

        editProfile = new RoundedButton("Edit Profile", borderRadius);
        editProfile.setPreferredSize(new Dimension(140, 40));
        editProfile.setFont(new Font("Arial", Font.PLAIN, 13));
        panel.add(editProfile, BorderLayout.EAST);

        return panel;
    }

    private RoundedPanel createStatsMiddle() {
        RoundedPanel panel = new RoundedPanel(borderRadius,ColorPalette.BG_SECONDARY,ColorPalette.BORDER);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(25, 30, 25, 40));

        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.insets = new Insets(5, 10, 5, 10);

        balance = (user != null) ? balance : 0.0;

        gbc.gridx = 0;
        gbc.weightx = 1;
        panel.add(createStatCard("Balance", String.format("$%.2f", balance)), gbc);

        gbc.gridx = 1;
        panel.add(createStatCard("Total purchases", "0"), gbc);// TODO: add number of purchases

        gbc.gridx = 2;
        panel.add(createStatCard("Cart items", "0"), gbc);// TODO: add number of products that now to list(ShapingCart)

        gbc.gridx = 3;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 12, 0, 0);
        JPanel btns = createButtonsPanel();
        panel.add(btns, gbc);

        return panel;
    }

    private RoundedPanel createStatCard(String label, String value) {
        RoundedPanel card = new RoundedPanel(borderRadius,ColorPalette.BG_SECONDARY,ColorPalette.SELECTION_BG);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(10, 14, 10, 14));

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Arial", Font.PLAIN, 12));
        labelComp.setForeground(ColorPalette.TEXT_MUTED);
        labelComp.setAlignmentX(LEFT_ALIGNMENT);

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Arial", Font.BOLD, 22));
        valueComp.setForeground(ColorPalette.ACCENT_PRIMARY);
        valueComp.setAlignmentX(LEFT_ALIGNMENT);

        card.add(labelComp);
        card.add(Box.createVerticalStrut(4));
        card.add(valueComp);

        return card;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        chargeWallet = new RoundedButton("Charge Wallet", borderRadius);
        chargeWallet.setBackground(ColorPalette.ACCENT_SUCCESS);
        chargeWallet.setHoverColor(new Color(0xB36FCF97, true));
        chargeWallet.setForeground(ColorPalette.TEXT_PRIMARY);
        chargeWallet.setFont(new Font("Arial", Font.PLAIN, 13));
        chargeWallet.setMaximumSize(new Dimension(150, 40));
        chargeWallet.setPreferredSize(new Dimension(150, 40));
        chargeWallet.setAlignmentX(CENTER_ALIGNMENT);

        logOut = new RoundedButton("Logout", borderRadius);
        logOut.setBackground(new Color(0xde3c2f));
        logOut.setHoverColor(new Color(0xC6DE3C2F, true));
        logOut.setForeground(ColorPalette.TEXT_PRIMARY);
        logOut.setFont(new Font("Arial", Font.PLAIN, 13));
        logOut.setMaximumSize(new Dimension(150, 40));
        logOut.setPreferredSize(new Dimension(150, 40));
        logOut.setAlignmentX(CENTER_ALIGNMENT);

        panel.add(chargeWallet);
        panel.add(Box.createVerticalStrut(8));
        panel.add(logOut);

        return panel;
    }

    public void loadUserData() {
        controller.loadProfile();
    }

    private void attachEvents() {
        this.addPropertyChangeListener(evt -> {

        });
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void displayUser(String fullName, String userType, double balance, int cartItems, boolean isNormal) {
        nameLabel.setText(String.format("Hi, %s",fullName));
        typeLabel.setText(userType);
        //TODO: add balance
        //TODO: add cart Item
    }
}