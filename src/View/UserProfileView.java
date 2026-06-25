package View;

import Components.ProfileMainPanel;
import Components.UserProfileEditPanel;
import Controller.ProfileController;
import Model.UserType;
import Util.ColorPalette;

import javax.swing.JPanel;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class UserProfileView extends JPanel {

    private ProfileMainPanel profileMainView;
    private UserProfileEditPanel userProfileEditPanel;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private ProfileController controller;

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public static final String LOGOUT_PROP = "logout";
    public static final String CHARGE_WALLET_PROP = "chargeWallet";
    public static final String EDIT_PROFILE_PROP = "editProfile";
    public static final String MANAGE_USER_PROP = "manageUser";
    public static final String LOG_SHOP_PROP = "logShop";
    public static final String ADD_PRODUCT_PROP = "addProduct";

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public UserProfileView(ProfileController controller) {
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
        cardPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));

        profileMainView = new ProfileMainPanel();

        userProfileEditPanel = new UserProfileEditPanel();
        userProfileEditPanel.setController(controller);
        cardPanel.add(profileMainView , "MAIN");
        cardPanel.add(userProfileEditPanel , "EDIT_PROFILE");

        this.add(cardPanel);
    }

    public void loadUserData() {
        controller.loadProfile();
    }

    private void attachEvents() {
        //profileMainView handle
        profileMainView.onClickLogout(e -> {
            support.firePropertyChange(LOGOUT_PROP, null, null);});
        profileMainView.onClickChargeWallet(e ->
                support.firePropertyChange(CHARGE_WALLET_PROP, null, null));
        profileMainView.onClickEditProfile(e ->
                support.firePropertyChange(EDIT_PROFILE_PROP, null, null));
        profileMainView.onClickManageUser(e ->
                support.firePropertyChange(MANAGE_USER_PROP, null, null));
        profileMainView.onClickLogShop(e ->
                support.firePropertyChange(LOG_SHOP_PROP, null, null));
        profileMainView.onClickAddProduct(e ->
                support.firePropertyChange(ADD_PRODUCT_PROP, null, null));

        //edit profile handle
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


    public void displayUser(String fullName, String userType, double balance, int cartItems, UserType type) {
        profileMainView.displayUser(fullName, userType, balance, cartItems, type);
    }

    public void displayPrimeUser(double creditAmount, double debitAmount, String memberShipID) {
        profileMainView.displayPrimeUser(creditAmount, debitAmount, memberShipID);
    }

    public void loadEditUserData(String fName, String lName, String phoneNumber) {
        userProfileEditPanel.loadUserData(fName, lName, phoneNumber);
    }
    public void showEditProfile() {
        controller.loadEditProfile();
        cardLayout.show(cardPanel,"EDIT_PROFILE");
    }

    public void showMainProfile(){
        cardLayout.show(cardPanel,"MAIN");
    }

}

