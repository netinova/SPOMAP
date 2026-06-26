package Components;

import Model.UserType;
import Util.ColorPalette;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.EventListenerList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfileMainPanel extends JPanel {

    private final int borderRadius = 20;

    private RoundedPanel userHeader;
    private RoundedPanel statsMiddle;

    private RoundedButton chargeWallet;
    private RoundedButton logOut;
    private RoundedButton editProfile;
    private RoundedButton manageUserBtn;
    private RoundedButton logShopBtn;
    private RoundedButton addProductBtn;// TODO: fuhh

    private JLabel nameLabel;
    private JLabel typeLabel;
    private JLabel cartItemsValueLabel;
    private JLabel balanceValueLabel;
    private JLabel totalPurchasesLabel;
    private JLabel tempLabel;
    private JLabel membershipIdLabel;
    private JLabel creditLabel;
    private JLabel debitLabel;

    private JPanel primePanel;
    private JPanel adminPanel;
    private JPanel logoutPanel;

    private EventListenerList listenerList = new EventListenerList();

    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    private void fireActionEvent(String command) {
        ActionListener[] listeners = listenerList.getListeners(ActionListener.class);
        if (listeners.length > 0) {
            ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);
            for (ActionListener listener : listeners) {
                listener.actionPerformed(event);
            }
        }
    }

    public static final String LOGOUT_PROP = "logout";
    public static final String CHARGE_WALLET_PROP = "chargeWallet";
    public static final String EDIT_PROFILE_PROP = "editProfile";
    public static final String MANAGE_USER_PROP = "manageUser";
    public static final String LOG_SHOP_PROP = "logShop";
    public static final String ADD_PRODUCT_PROP = "addProduct";


    public ProfileMainPanel() {
        setupUI();
        createComponents();
        attachEvents();
    }

    private void setupUI() {
        setBackground(ColorPalette.BG_MAIN);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(30, 30, 30, 30));
    }

    private void createComponents() {
        userHeader = createHeader();
        userHeader.setAlignmentX(LEFT_ALIGNMENT);
        this.add(userHeader);
        this.add(Box.createVerticalStrut(25));

        statsMiddle = createStatsMiddle();
        statsMiddle.setAlignmentX(LEFT_ALIGNMENT);
        this.add(statsMiddle);
        this.add(Box.createVerticalStrut(25));

        primePanel = createPrimePanel();
        primePanel.setAlignmentX(LEFT_ALIGNMENT);
        primePanel.setVisible(false);
        this.add(primePanel);

        adminPanel = createAdminPanel();
        adminPanel.setAlignmentX(LEFT_ALIGNMENT);
        adminPanel.setVisible(false);
        this.add(adminPanel);

        this.add(Box.createVerticalStrut(25));
        logoutPanel = crateLogoutPanel();
        logoutPanel.setAlignmentX(LEFT_ALIGNMENT);
        this.add(Box.createVerticalGlue());
        this.add(logoutPanel);
    }

    private RoundedPanel createHeader() {
        RoundedPanel panel = new RoundedPanel(borderRadius, ColorPalette.BG_SECONDARY, ColorPalette.BORDER);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

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
        nameLabel = new JLabel(String.format("Hi, %s", "UserFullName"));
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
        RoundedPanel panel = new RoundedPanel(borderRadius, ColorPalette.BG_SECONDARY, ColorPalette.BORDER);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(25, 30, 25, 40));

        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.insets = new Insets(5, 10, 5, 10);

        gbc.gridx = 0;
        gbc.weightx = 1;
        panel.add(createStatCard("Balance", "$0.00"), gbc);
        balanceValueLabel = tempLabel;

        gbc.gridx = 1;
        panel.add(createStatCard("Total purchases", "0"), gbc);// TODO: add number of purchases
        totalPurchasesLabel = tempLabel;

        gbc.gridx = 2;
        panel.add(createStatCard("Cart items", "0"), gbc);// TODO: add number of products that now to list(ShapingCart)
        cartItemsValueLabel = tempLabel;

        gbc.gridx = 3;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 12, 0, 0);
        JPanel buttonPanel = createButtonsPanel();
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private RoundedPanel createStatCard(String label, String value) {
        RoundedPanel card = new RoundedPanel(borderRadius, ColorPalette.BG_SECONDARY, ColorPalette.SELECTION_BG);
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
        this.tempLabel = valueComp;

        return card;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());

        chargeWallet = new RoundedButton("Charge Wallet", borderRadius);
        chargeWallet.setBackground(ColorPalette.ACCENT_SUCCESS);
        chargeWallet.setHoverColor(new Color(0xB36FCF97, true));
        chargeWallet.setForeground(ColorPalette.TEXT_PRIMARY);
        chargeWallet.setFont(new Font("Arial", Font.PLAIN, 13));
        chargeWallet.setMaximumSize(new Dimension(150, 40));
        chargeWallet.setPreferredSize(new Dimension(150, 40));

        panel.add(chargeWallet);
        return panel;
    }

    private JPanel crateLogoutPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        logOut = new RoundedButton("Logout", borderRadius);
        logOut.setBackground(new Color(0xde3c2f));
        logOut.setHoverColor(new Color(0xAD3225));
        logOut.setForeground(ColorPalette.TEXT_PRIMARY);
        logOut.setFont(new Font("Arial", Font.PLAIN, 12));
        logOut.setMaximumSize(new Dimension(90, 30));
        logOut.setPreferredSize(new Dimension(90, 30));
        logOut.setAlignmentX(RIGHT_ALIGNMENT);

        panel.add(Box.createHorizontalGlue());
        panel.add(logOut);
        return panel;
    }

    private JPanel createPrimePanel() {
        RoundedPanel panel = new RoundedPanel(borderRadius, ColorPalette.BG_SECONDARY, ColorPalette.ACCENT_PRIMARY);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(5, 10, 5, 10);

        gbc.gridx = 0;
        panel.add(createStatCard("Membership ID", "-"), gbc);
        membershipIdLabel = tempLabel;

        gbc.gridx = 1;
        panel.add(createStatCard("Credit", "$0.00"), gbc);
        creditLabel = tempLabel;

        gbc.gridx = 2;
        panel.add(createStatCard("Debit", "$0.00"), gbc);
        debitLabel = tempLabel;

        return panel;
    }

    private JPanel createAdminPanel() {
        RoundedPanel panel = new RoundedPanel(borderRadius, ColorPalette.BG_SECONDARY, ColorPalette.ACCENT_WARNING);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel title = new JLabel("Admin Controls");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setForeground(ColorPalette.TEXT_PRIMARY);
        panel.add(title);

        panel.add(Box.createHorizontalGlue());

        manageUserBtn = new RoundedButton("Manage Users", borderRadius);
        manageUserBtn.setPreferredSize(new Dimension(150, 40));
        manageUserBtn.setBackground(ColorPalette.ACCENT_WARNING);
        manageUserBtn.setForeground(ColorPalette.TEXT_PRIMARY);
        panel.add(manageUserBtn);
        panel.add(Box.createHorizontalStrut(10));

        logShopBtn = new RoundedButton("Status Shop", borderRadius);
        logShopBtn.setPreferredSize(new Dimension(150, 40));
        logShopBtn.setBackground(ColorPalette.ACCENT_WARNING);
        logShopBtn.setForeground(ColorPalette.TEXT_PRIMARY);
        panel.add(logShopBtn);
        panel.add(Box.createHorizontalStrut(10));

        addProductBtn = new RoundedButton("Add Product", borderRadius);
        addProductBtn.setPreferredSize(new Dimension(150, 40));
        addProductBtn.setBackground(ColorPalette.ACCENT_WARNING);
        addProductBtn.setForeground(ColorPalette.TEXT_PRIMARY);
        panel.add(addProductBtn);

        return panel;
    }

    public void displayUser(String fullName, String userType, double balance, int cartItems, UserType type) {
        nameLabel.setText(String.format("Hi, %s", fullName));
        typeLabel.setText(userType);
        balanceValueLabel.setText(String.format("$%.2f", balance));
        cartItemsValueLabel.setText(String.valueOf(cartItems));
        totalPurchasesLabel.setText("0");// TODO: if need this property handle for side of user

        switch (type) {
            case ADMIN:
                adminPanel.setVisible(true);
                statsMiddle.setVisible(false);
                break;
            case PRIME:
                primePanel.setVisible(true);
                break;
            case NORMAL:
                adminPanel.setVisible(false);
                primePanel.setVisible(false);
                statsMiddle.setVisible(true);
                break;
        }
        switch (type) {
            case ADMIN -> adminPanel.setVisible(true);
            case PRIME -> primePanel.setVisible(true);
        }

        revalidate();
        repaint();
    }

    public void displayPrimeUser(double creditAmount, double debitAmount, String memberShipID) {
        creditLabel.setText(String.format("$%.2f", creditAmount));
        debitLabel.setText(String.format("$%.2f", debitAmount));
        membershipIdLabel.setText(memberShipID);

        revalidate();
        repaint();
    }


    private void attachEvents() {
        logOut.addActionListener(e -> fireActionEvent(LOGOUT_PROP));
        chargeWallet.addActionListener(e -> fireActionEvent(CHARGE_WALLET_PROP));
        editProfile.addActionListener(e -> fireActionEvent(EDIT_PROFILE_PROP));
        manageUserBtn.addActionListener(e -> fireActionEvent(MANAGE_USER_PROP));
        logShopBtn.addActionListener(e -> fireActionEvent(LOG_SHOP_PROP));
        addProductBtn.addActionListener(e -> fireActionEvent(ADD_PRODUCT_PROP));
    }
}