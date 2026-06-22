package Controller;

import Model.*;
import Service.UserService;
import View.UserProfileView;

public class ProfileController {

    private UserProfileView view;
    private UserService userService;
    private OnChangeViewListener listener;


    public ProfileController() {
        this.userService = new UserService();
    }

    public void setView(UserProfileView view) {
        this.view = view;
        attachViewEvents();
    }

    public void setOnChangeViewListener(OnChangeViewListener listener) {
        this.listener = listener;
    }

    public void loadProfile() {
        User user = AppState.getInstance().getLoggedInUser();
        if (user == null || view == null) return;

        int cartItems = 0;
        if (AppState.getInstance().getCart() != null)
            cartItems = AppState.getInstance().getCart().getItems().size();

        boolean isNormal = UserType.NORMAL.getDisplayName().equals("Normal User");

        view.displayUser(
                user.getFullName(),
                user.getUserType().getDisplayName(),
                user.getBalance(),
                cartItems,
                isNormal
        );
    }

    public void handleLogout() {
        AppState.getInstance().setLoggedInUser(null);
        AppState.getInstance().setCart(null);
        if (listener != null)
            listener.changeView(ViewType.AUTH.getViewId());
    }

    public void handleUpgradeToPrime() {
        User user = AppState.getInstance().getLoggedInUser();
        if (user == null) return;
        boolean success = userService.convertNormalUserToPrime(user.getPhoneNumber());
        if (success) {
            User updated = userService.searchUserByPhoneNumber(user.getPhoneNumber());
            AppState.getInstance().setLoggedInUser(updated);
            loadProfile(); // refresh view
        }
    }

    private void attachViewEvents() {
        view.addPropertyChangeListener(evt -> {
            switch (evt.getPropertyName()) {
                case "logout" -> handleLogout();
                case "upgradeToPrime" -> handleUpgradeToPrime();
                case "chargeWallet" -> System.out.println("charge wallet Button");// TODO: charge wallet
                case "editProfile" -> System.out.println("edit profile Button"); // TODO: edit profile
            }
        });
    }
}