package View;

import Components.ChargeWalletPanel;
import Components.MangeUserProfilePanel;
import Components.ProfileMainPanel;
import Components.UserProfileEditPanel;
import Controller.UserProfileController;
import Model.UserType;
import Util.ColorPalette;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

public class UserProfileView extends JPanel {

    private ProfileMainPanel profileMainView;
    private UserProfileEditPanel userProfileEditPanel;
    private ChargeWalletPanel chargeWalletPanel;
    private MangeUserProfilePanel mangeUserProfilePanel;

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

        userProfileEditPanel.setController(controller);
        chargeWalletPanel.setController(controller);
        mangeUserProfilePanel.setController(controller);

        cardPanel.add(profileMainView, "MAIN");
        cardPanel.add(userProfileEditPanel, "EDIT_PROFILE");
        cardPanel.add(chargeWalletPanel, "CHARGE_WALLET");
        cardPanel.add(mangeUserProfilePanel, "MANAGE_USERS");

        this.add(cardPanel);
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
                case ProfileMainPanel.ADD_PRODUCT_PROP -> System.out.println("Clicked add product");
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
        mangeUserProfilePanel.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals(MangeUserProfilePanel.SEARCH_FILED_PROP))
                controller.onSearchPhoneChange(evt.getNewValue().toString());
            if (evt.getPropertyName().equals(MangeUserProfilePanel.SEARCH_PROP))
                controller.onSearchClicked(evt.getNewValue().toString());
            if (evt.getPropertyName().equals(MangeUserProfilePanel.CANCEL_PROP))
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
        controller.loadMangeUsers();
        cardLayout.show(cardPanel, "MANAGE_USERS");
    }
}
