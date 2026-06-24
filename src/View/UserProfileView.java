package View;

import Components.ProfileMainPanel;
import Controller.ProfileController;
import Model.AppState;
import Model.User;
import Model.UserType;
import Util.ColorPalette;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class UserProfileView extends JPanel implements PropertyChangeListener {

    private User user;
    private ProfileMainPanel profileMainView;

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

        profileMainView = new ProfileMainPanel();
        this.add(profileMainView);
    }
    public void loadUserData() {
        controller.loadProfile();
    }

    private void attachEvents() {
        profileMainView.onClickLogout(e -> {
            support.firePropertyChange(LOGOUT_PROP,null,null);});
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

    }

    public void setUser(User user) {
        this.user = user;
    }

    public void displayUser(String fullName, String userType, double balance, int cartItems, UserType type) {
        profileMainView.displayUser(fullName,userType, balance, cartItems, type);
    }

    public void displayPrimeUser(double creditAmount, double debitAmount, String memberShipID) {
        profileMainView.displayPrimeUser(creditAmount, debitAmount, memberShipID);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(AppState.PROP_USER)) {
            // update
        }
    }
}
