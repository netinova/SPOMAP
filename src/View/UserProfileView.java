package View;

import Components.*;
import Controller.UserProfileController;
import Model.UserType;
import Util.ColorPalette;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.*;

public class UserProfileView extends JPanel {

    private ProfileMainPanel profileMainView;
    private UserProfileEditPanel userProfileEditPanel;
    private ChargeWalletPanel chargeWalletPanel;
    private MangeUserProfilePanel mangeUserProfilePanel;
    private AddProductPanel productPanel;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private UserProfileController controller;

    public UserProfileView(UserProfileController controller) {
        this.controller = controller;

        setupUI();
        attachEvents();
    }

    private void setupUI() {
        setBackground(ColorPalette.BG_MAIN);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        cardPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        profileMainView = new ProfileMainPanel();
        userProfileEditPanel = new UserProfileEditPanel();
        chargeWalletPanel = new ChargeWalletPanel();
        mangeUserProfilePanel = new MangeUserProfilePanel();
        productPanel = new AddProductPanel();

        userProfileEditPanel.setController(controller);
        chargeWalletPanel.setController(controller);
        mangeUserProfilePanel.setController(controller);
        productPanel.setController(controller);

        // scroll
        JScrollPane profileScroll = new JScrollPane(profileMainView);
        profileScroll.setBorder(null);
        profileScroll.setOpaque(false);
        profileScroll.getViewport().setOpaque(false);
        profileScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        profileScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        styleScrollBar(profileScroll.getVerticalScrollBar());

        cardPanel.add(profileScroll, "MAIN");
        cardPanel.add(userProfileEditPanel, "EDIT_PROFILE");
        cardPanel.add(chargeWalletPanel, "CHARGE_WALLET");
        cardPanel.add(mangeUserProfilePanel, "MANAGE_USERS");
        cardPanel.add(productPanel,"ADD_PRODUCT");

        this.add(cardPanel, BorderLayout.CENTER);
    }

    public void loadUserData() {
        controller.loadProfile();
    }

    private void attachEvents() {
        // profileMainView handle

        profileMainView.addActionListener(e -> {
            switch (e.getActionCommand()) {
                case ProfileMainPanel.LOGOUT_PROP -> controller.handleLogout();
                case ProfileMainPanel.CHARGE_WALLET_PROP -> showChargeWallet();
                case ProfileMainPanel.EDIT_PROFILE_PROP -> showEditProfile();
                case ProfileMainPanel.MANAGE_USER_PROP -> showMangeUsers();
                case ProfileMainPanel.LOG_SHOP_PROP -> System.out.println("Clicked status shop");
                case ProfileMainPanel.ADD_PRODUCT_PROP -> showProductPanel();
                case ProfileMainPanel.SHOPPING_CART_PROP -> controller.showShoppingCart();
            }
        });

        // edit profile handle
        userProfileEditPanel.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals(UserProfileEditPanel.F_NAME_PROP))
                controller.onFirstNameChange((String) evt.getNewValue());
            if (evt.getPropertyName().equals(UserProfileEditPanel.L_NAME_PROP))
                controller.onLastNameChange((String) evt.getNewValue());
            if (evt.getPropertyName().equals(UserProfileEditPanel.PHONE_NUMBER_PROP))
                controller.onPhoneNumberChange((String) evt.getNewValue());
            if (evt.getPropertyName().equals(UserProfileEditPanel.PASSWORD_CURRENT_PROP))
                controller.onPasswordCurrentChange((String) evt.getNewValue());
            if (evt.getPropertyName().equals(UserProfileEditPanel.PASSWORD_PROP))
                controller.onPasswordChange((String) evt.getNewValue());
            if (evt.getPropertyName().equals(UserProfileEditPanel.PASSWORD_CONFIRM_PROP))
                controller.onConfirmPasswordChange((String) evt.getNewValue());
        });

        // charge proses
        chargeWalletPanel.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals(ChargeWalletPanel.AMOUNT_PROP))
                controller.onAmountChange(evt.getNewValue().toString());
            if (evt.getPropertyName().equals(ChargeWalletPanel.CHARGE_PROP))
                controller.onChargeButtonClick(evt.getNewValue().toString());
            if (evt.getPropertyName().equals(ChargeWalletPanel.CANCEL_PROP))
                controller.onCancelClick();
        });

        // Manage User
        mangeUserProfilePanel.addActionListener(e -> {
            switch (e.getActionCommand()){
                case MangeUserProfilePanel.CANCEL_MANAGE_PROP ->mangeUserProfilePanel.showSearchView();
                case MangeUserProfilePanel.KICK_PROP -> System.out.println("On kick click");
                case MangeUserProfilePanel.CONVERT_TO_PRIME_PROP -> System.out.println("On Convert click");
                case MangeUserProfilePanel.CANCEL_PROP -> controller.onCancelClick();
                case MangeUserProfilePanel.SEARCH_PROP -> System.out.println("On search click");
                case MangeUserProfilePanel.SEARCH_FILED_PROP -> System.out.println("user changed");
            }
        });

        //add Product
        productPanel.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals(AddProductPanel.NAME_PROP))
                controller.onNameProductChange(evt.getNewValue().toString());
            if (evt.getPropertyName().equals(AddProductPanel.PRICE_PROP))
                controller.onPriceProductChange(evt.getNewValue().toString());
            if (evt.getPropertyName().equals(AddProductPanel.DISCOUNT_PROP))
                controller.onDiscountProductChange(evt.getNewValue().toString());
            if (evt.getPropertyName().equals(AddProductPanel.MANUFACTURER_PROP))
                controller.onManufacturerProductChange(evt.getNewValue().toString());
            if (evt.getPropertyName().equals(AddProductPanel.DESCRIPTION_PROP))
                controller.onDescriptionProductChange(evt.getNewValue().toString());
            if (evt.getPropertyName().equals(AddProductPanel.SAVE_PROP))
                controller.onSaveClicked();
            if (evt.getPropertyName().equals(AddProductPanel.CANCEL_PROP))
                controller.onCancelClick();
        });
    }

    // show errors edit panel
    public void showPhoneError(String error) {
        if (userProfileEditPanel != null)
            userProfileEditPanel.showPhoneError(error);
    }

    public void showFirstNameError(String error) {
        if (userProfileEditPanel != null)
            userProfileEditPanel.showFirstNameError(error);
    }

    public void showLastNameError(String error) {
        if (userProfileEditPanel != null)
            userProfileEditPanel.showLastNameError(error);
    }

    public void showCurrentPasswordError(String error) {
        if (userProfileEditPanel != null)
            userProfileEditPanel.showCurrentPasswordError(error);
    }

    public void showNewtPasswordError(String error) {
        if (userProfileEditPanel != null)
            userProfileEditPanel.showNewPasswordError(error);
    }

    public void showConfirmPasswordError(String error) {
        if (userProfileEditPanel != null)
            userProfileEditPanel.showConfirmPasswordError(error);
    }

    // load Data
    public void displayUser(String fullName, String userType, double balance, int cartItems, UserType type) {
        profileMainView.displayUser(fullName, userType, balance, cartItems, type);
    }

    public void displayPrimeUser(double creditAmount, double debitAmount, String memberShipID) {
        profileMainView.displayPrimeUser(creditAmount, debitAmount, memberShipID);
    }

    public void loadEditUserData(String fName, String lName, String phoneNumber) {
        userProfileEditPanel.loadUserData(fName, lName, phoneNumber);
    }

    public void loadChargeWalletData(String balance) {
        chargeWalletPanel.loadUserData(balance);
    }

    public void loadManageUsers() {
        mangeUserProfilePanel.loadView();
    }

    public void loadInformationUser(String firstName, String lastName, String phoneNumber, String userId, String  userType, String registerDate,
                                    String memberShipCode, double creditAmount, double debitAmount) {
        mangeUserProfilePanel.loadData(firstName, lastName, phoneNumber, userId, userType , registerDate , memberShipCode , creditAmount , debitAmount);
    }
    // switch view

    public void showEditProfile() {
        controller.loadEditProfile();
        cardLayout.show(cardPanel, "EDIT_PROFILE");
    }

    public void showMainProfile() {
        cardLayout.show(cardPanel, "MAIN");
    }

    public void showChargeWallet() {
        controller.loadChargeWalletData();
        cardLayout.show(cardPanel, "CHARGE_WALLET");
    }

    public void showMangeUsers() {
        controller.showSearchUser();
        cardLayout.show(cardPanel, "MANAGE_USERS");
    }

    public void showProductPanel(){
        cardLayout.show(cardPanel, "ADD_PRODUCT");
    }

    private void styleScrollBar(JScrollBar bar) {
        bar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.trackColor = ColorPalette.BG_MAIN;
                this.thumbColor = ColorPalette.BG_TERTIARY;
            }

            @Override
            protected JButton createDecreaseButton(int o) {
                return zeroBtn();
            }

            @Override
            protected JButton createIncreaseButton(int o) {
                return zeroBtn();
            }

            private JButton zeroBtn() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                b.setMinimumSize(new Dimension(0, 0));
                b.setMaximumSize(new Dimension(0, 0));
                return b;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                if (r.isEmpty() || !scrollbar.isEnabled()) return;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fillRoundRect(r.x, r.y, r.width - 1, r.height - 1, 8, 8);
                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
                g.setColor(trackColor);
                g.fillRect(r.x, r.y, r.width, r.height);
            }
        });
        bar.setPreferredSize(new Dimension(8, 0));
        bar.setUnitIncrement(16);
    }
}
