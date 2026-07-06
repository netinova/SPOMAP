package Components;

import Model.UserType;
import Util.ColorPalette;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.XChartPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class ProfileMainPanel extends JPanel {

    private final int borderRadius = 25;

    private RoundedPanel userHeader;
    private RoundedPanel statsMiddle;

    private RoundedButton chargeWallet;
    private RoundedButton logOut;
    private RoundedButton editProfile;
    private RoundedButton manageUserBtn;
    private RoundedButton logShopBtn;
    private RoundedButton addProductBtn;
    private RoundedButton viewInvoiceBtn;
    private RoundedButton shoppingCartBtn;
    private RoundedButton settingBtn;

    private JLabel nameLabel;
    private JLabel typeLabel;
    private JLabel cartItemsValueLabel;
    private JLabel balanceValueLabel;
    private JLabel totalPurchasesLabel;
    private JLabel tempLabel;
    private JLabel membershipIdLabel;
    private JLabel creditLabel;
    private JLabel debitLabel;
    private JLabel rank1Label;
    private JLabel rank2Label;
    private JLabel rank3Label;
    private JLabel moneySaveValueLabel;
    private JLabel ordersNumberLabel;

    private JPanel primePanel;
    private JPanel adminPanel;
    private JPanel logoutPanel;
    private JPanel quickAction;
    private JPanel statusPanel;

    private CategoryChart categoryChartMonthly;
    private List<String> dateForMonthly;
    private List<Double> purchaseForMonthly;

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
    public static final String SHOPPING_CART_PROP = "shoppingCart";
    public static final String INVOICE_PROP = "invoice";
    public static final String SETTING_PROP = "setting";


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
        statusPanel = createRowStatusProductUser();
        statusPanel.setAlignmentX(LEFT_ALIGNMENT);
        this.add(statusPanel);

        this.add(Box.createVerticalStrut(25));
        quickAction = createQuickActionPanel();
        quickAction.setAlignmentX(LEFT_ALIGNMENT);
        this.add(quickAction);

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
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JLabel title = new JLabel("Admin Controls");
        title.setFont(new Font("Arial", Font.BOLD, 23));
        title.setForeground(ColorPalette.TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);

        panel.add(Box.createVerticalStrut(8));

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER,15,30));
        btnPanel.setOpaque(false);

        manageUserBtn = new RoundedButton("Manage Users", borderRadius);
        manageUserBtn.setPreferredSize(new Dimension(250, 70));
        manageUserBtn.setMaximumSize(new Dimension(250, 70));
        manageUserBtn.setFont(new Font("Arial", Font.BOLD, 17));
        manageUserBtn.setBackground(ColorPalette.ACCENT_WARNING);
        manageUserBtn.setForeground(ColorPalette.TEXT_PRIMARY);
        btnPanel.add(manageUserBtn);
        btnPanel.add(Box.createHorizontalStrut(10));
//        panel.add(Box.createHorizontalStrut(10));

        logShopBtn = new RoundedButton("Status Shop", borderRadius);
        logShopBtn.setPreferredSize(new Dimension(250, 70));
        logShopBtn.setMaximumSize(new Dimension(250, 70));
        logShopBtn.setFont(new Font("Arial", Font.BOLD, 17));
        logShopBtn.setBackground(ColorPalette.ACCENT_WARNING);
        logShopBtn.setForeground(ColorPalette.TEXT_PRIMARY);
        btnPanel.add(logShopBtn);
        btnPanel.add(Box.createHorizontalStrut(10));
//        panel.add(Box.createHorizontalStrut(10));

        addProductBtn = new RoundedButton("Add Product", borderRadius);
        addProductBtn.setPreferredSize(new Dimension(250, 70));
        addProductBtn.setMaximumSize(new Dimension(250, 70));
        addProductBtn.setFont(new Font("Arial", Font.BOLD, 17));
        addProductBtn.setBackground(ColorPalette.ACCENT_WARNING);
        addProductBtn.setForeground(ColorPalette.TEXT_PRIMARY);
        btnPanel.add(addProductBtn);

        panel.add(btnPanel);

        return panel;
    }

    private JPanel createQuickActionPanel() {
        RoundedPanel panel = new RoundedPanel(borderRadius, ColorPalette.BG_SECONDARY, ColorPalette.ACCENT_WARNING);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));
        panel.setPreferredSize(new Dimension(400,0));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JLabel title = new JLabel("Quick Actions");
        title.setFont(new Font("Arial", Font.BOLD, 23));
        title.setForeground(ColorPalette.TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);

        panel.add(Box.createVerticalStrut(8));

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER,15,30));
        btnPanel.setOpaque(false);

        viewInvoiceBtn = new RoundedButton("View Invoices", borderRadius);
        viewInvoiceBtn.setPreferredSize(new Dimension(250, 70));
        viewInvoiceBtn.setMaximumSize(new Dimension(250, 70));
        viewInvoiceBtn.setFont(new Font("Arial", Font.BOLD, 17));
        viewInvoiceBtn.setBackground(ColorPalette.ACCENT_WARNING);
        viewInvoiceBtn.setForeground(ColorPalette.TEXT_PRIMARY);
        btnPanel.add(viewInvoiceBtn);
        btnPanel.add(Box.createHorizontalStrut(10));

        shoppingCartBtn = new RoundedButton("Shopping Cart", borderRadius);
        shoppingCartBtn.setPreferredSize(new Dimension(250, 70));
        shoppingCartBtn.setMaximumSize(new Dimension(250, 70));
        shoppingCartBtn.setFont(new Font("Arial", Font.BOLD, 17));
        shoppingCartBtn.setBackground(ColorPalette.ACCENT_WARNING);
        shoppingCartBtn.setForeground(ColorPalette.TEXT_PRIMARY);
        btnPanel.add(shoppingCartBtn);
        btnPanel.add(Box.createHorizontalStrut(10));

        settingBtn = new RoundedButton("Setting", borderRadius);
        settingBtn.setPreferredSize(new Dimension(250, 70));
        settingBtn.setMaximumSize(new Dimension(250, 70));
        settingBtn.setFont(new Font("Arial", Font.BOLD, 17));
        settingBtn.setBackground(ColorPalette.ACCENT_WARNING);
        settingBtn.setForeground(ColorPalette.TEXT_PRIMARY);
        btnPanel.add(settingBtn);

        panel.add(btnPanel);

        return panel;
    }

    // Status panel
    public JPanel createRowStatusProductUser(){
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.insets = new Insets(0, 0, 0, 10);

        gbc.gridx = 0;
        gbc.weightx = 1;
        panel.add(crateStatusPanel(), gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 10, 0, 0);
        panel.add(createMonthlyBarChart(), gbc);

        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE,
                panel.getPreferredSize().height));

        return panel;
    }

    private JPanel createMonthlyBarChart() {
        RoundedPanel panel = new RoundedPanel(borderRadius, ColorPalette.BG_SECONDARY, ColorPalette.BORDER);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(600, 470));

        java.util.List<String> months = Arrays.asList("");
        List<Double> purchase = Arrays.asList(0.0);

        categoryChartMonthly = new CategoryChartBuilder()
                .width(400)
                .height(340)
                .title("Monthly Revenue")
                .xAxisTitle("Month")
                .yAxisTitle("Revenue ($)")
                .build();

        //custom style
        categoryChartMonthly.getStyler().setChartBackgroundColor(ColorPalette.BG_SECONDARY);
        categoryChartMonthly.getStyler().setPlotBackgroundColor(ColorPalette.BG_SECONDARY);
        categoryChartMonthly.getStyler().setAnnotationTextFontColor(ColorPalette.TEXT_PRIMARY);
        categoryChartMonthly.getStyler().setChartTitleFontColor(ColorPalette.TEXT_PRIMARY);
        categoryChartMonthly.getStyler().setChartFontColor(ColorPalette.TEXT_PRIMARY);
        categoryChartMonthly.getStyler().setXAxisTitleColor(ColorPalette.TEXT_PRIMARY);
        categoryChartMonthly.getStyler().setYAxisTitleColor(ColorPalette.TEXT_PRIMARY);
        categoryChartMonthly.getStyler().setLegendBackgroundColor(ColorPalette.BG_SECONDARY);
        categoryChartMonthly.getStyler().setAxisTickLabelsColor(ColorPalette.TEXT_PRIMARY);
        categoryChartMonthly.getStyler().setSeriesColors(new Color[]{ColorPalette.ACCENT_PRIMARY, ColorPalette.ACCENT_WARNING});
        categoryChartMonthly.getStyler().setDefaultSeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Bar);

        categoryChartMonthly.addSeries("Purchase", months, purchase);

        XChartPanel<CategoryChart> chartPanel = new XChartPanel<>(categoryChartMonthly);
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crateStatusPanel() {
        RoundedPanel panel = new RoundedPanel(borderRadius, ColorPalette.BG_SECONDARY, ColorPalette.BORDER);
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20,20,20,20));

        RoundedPanel ranking = new RoundedPanel(borderRadius, ColorPalette.BG_TERTIARY, ColorPalette.BORDER);
        ranking.setBorder(new EmptyBorder(5,8,5,8));
        ranking.setLayout(new BoxLayout(ranking,BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Ranking");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(ColorPalette.TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        ranking.add(Box.createVerticalStrut(10));
        ranking.add(title);
        ranking.add(Box.createVerticalStrut(25));
        ranking.add(createLineRank("1",ColorPalette.BG_MAIN));
        rank1Label=tempLabel;
        ranking.add(Box.createVerticalStrut(5));
        ranking.add(createLineRank("2",ColorPalette.BG_TERTIARY));
        rank2Label=tempLabel;
        ranking.add(Box.createVerticalStrut(5));
        ranking.add(createLineRank("3",ColorPalette.BG_MAIN));
        rank3Label=tempLabel;
        ranking.add(Box.createVerticalStrut(3));
        panel.add(ranking);
        panel.add(Box.createVerticalStrut(30));

        panel.add(createLineStatus("Money saved"));
        moneySaveValueLabel = tempLabel;
        panel.add(Box.createVerticalStrut(30));
        panel.add(createLineStatus("Order Number"));
        ordersNumberLabel = tempLabel;
        panel.add(Box.createVerticalStrut(30));

        return panel;
    }

    private JPanel createLineRank(String rank , Color backGroundColor){
        RoundedPanel rankPanel = new RoundedPanel(10, backGroundColor, ColorPalette.BORDER);
        rankPanel.setBackground(backGroundColor);
        rankPanel.setBorder(new EmptyBorder(10,15,10,15));
        rankPanel.setPreferredSize(new Dimension(150, 50));
        rankPanel.setLayout(new BoxLayout(rankPanel, BoxLayout.X_AXIS));

        JLabel rankLabel = new JLabel(rank);
        rankLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        rankLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        rankLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel rankValue = new JLabel("----");
        rankValue.setFont(new Font("Arial", Font.PLAIN, 15));
        rankValue.setForeground(ColorPalette.TEXT_PRIMARY);
        rankValue.setAlignmentX(Component.RIGHT_ALIGNMENT);
        tempLabel = rankValue;

        rankPanel.add(rankLabel);
        rankPanel.add(Box.createHorizontalGlue());
        rankPanel.add(rankValue);

        return rankPanel;
    }

    public JPanel createLineStatus(String key){
        RoundedPanel panel = new RoundedPanel(borderRadius, ColorPalette.BG_TERTIARY, ColorPalette.BORDER);
        panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
        panel.setPreferredSize(new Dimension(150, 50));
        panel.setBorder(new EmptyBorder(20,20,20,20));

        JLabel keyLabel = new JLabel(key);
        keyLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        keyLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        keyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(keyLabel);
        panel.add(Box.createHorizontalGlue());

        JLabel valueLabel = new JLabel("----");
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        valueLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        valueLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panel.add(valueLabel);
        tempLabel = valueLabel;

        return panel;
    }

    //setter for chart

    public void setPurchaseForMonthly(List<Double> purchaseForMonthly) {
        this.purchaseForMonthly = purchaseForMonthly;
    }

    public void setDateForMonthly(List<String > dateForMonthly) {
        this.dateForMonthly = dateForMonthly;
    }

    public void refreshViewChartMonthly() {
        categoryChartMonthly.updateCategorySeries("Purchase", dateForMonthly, purchaseForMonthly,null);
        repaint();
        revalidate();
    }

    public void loadStatusCard(){

    }

    public void displayUser(String fullName, String userType, double balance, int cartItems, UserType type) {
        nameLabel.setText(String.format("Hi, %s", fullName));
        typeLabel.setText(userType);
        balanceValueLabel.setText(String.format("$%.2f", balance));
        cartItemsValueLabel.setText(String.valueOf(cartItems));
        totalPurchasesLabel.setText("0");// TODO: if need this property handle for side of user
        statusPanel.setVisible(true);

        switch (type) {
            case ADMIN:
                adminPanel.setVisible(true);
                statsMiddle.setVisible(false);
                primePanel.setVisible(false);
                quickAction.setVisible(false);
                statusPanel.setVisible(false);
                break;
            case PRIME:
                primePanel.setVisible(true);
                statsMiddle.setVisible(true);
                adminPanel.setVisible(false);
                quickAction.setVisible(true);
                break;
            case NORMAL:
                adminPanel.setVisible(false);
                primePanel.setVisible(false);
                statsMiddle.setVisible(true);
                quickAction.setVisible(true);
                break;
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
        shoppingCartBtn.addActionListener(e -> fireActionEvent(SHOPPING_CART_PROP));
        settingBtn.addActionListener(e -> fireActionEvent(SETTING_PROP));
        viewInvoiceBtn.addActionListener(e -> fireActionEvent(INVOICE_PROP));
    }
}