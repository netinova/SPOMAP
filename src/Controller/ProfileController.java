package Controller;

import Model.AppState;
import Model.PrimeUser;
import Model.User;
import Model.UserType;
import Model.ViewType;
import Service.UserService;
import View.UserProfileView;

public class ProfileController {

    private UserProfileView view;
    private OnChangeViewListener listener;

    public void setView(UserProfileView view) {
        this.view = view;
        attachViewEvents();
    }

    public void setOnChangeViewListener(OnChangeViewListener listener) {
        this.listener = listener;
    }

    public void loadProfile() {
        User user = AppState.getInstance().getLoggedInUser();
        if (user == null || view == null)
            return;

        int cartItems = 0;
        if (AppState.getInstance().getCart() != null)
            cartItems = AppState.getInstance().getCart().getItems().size();


        view.displayUser(
                user.getFullName(),
                user.getUserType().getDisplayName(),
                user.getBalance(),
                cartItems,
                user.getUserType());

        if (user.getUserType()==UserType.PRIME){
            PrimeUser userPrime=(PrimeUser) user;
            view.displayPrimeUser(
                    userPrime.getCreditAmount(),
                    userPrime.getCreditAmount(),
                    userPrime.getMemberShipID()
            );

        }
    }

    public void handleLogout() {
        AppState.getInstance().setLoggedInUser(null);
        AppState.getInstance().setCart(null);
        if (listener != null)
            listener.changeView(ViewType.AUTH.getViewId());
    }

    public void handleUpgradeToPrime() {
        User user = AppState.getInstance().getLoggedInUser();
        if (user == null)
            return;
        boolean success = UserService.convertNormalUserToPrime(user.getPhoneNumber(),
                AppState.getInstance().normalUsersList,
                AppState.getInstance().primeUsersList,
                AppState.getInstance().adminUsersList);
        if (success) {
            User updated = UserService.searchUserByPhoneNumber(user.getPhoneNumber(),
                    AppState.getInstance().normalUsersList,
                    AppState.getInstance().primeUsersList,
                    AppState.getInstance().adminUsersList);
            AppState.getInstance().setLoggedInUser(updated);
            loadProfile(); // refresh view
        }
    }

    private void attachViewEvents() {
        view.addPropertyChangeListener(evt -> {
            switch (evt.getPropertyName()) {
                case UserProfileView.LOGOUT_PROP -> handleLogout();
                case UserProfileView.CHARGE_WALLET_PROP -> System.out.println("Clicked on charge wallet");
                case UserProfileView.EDIT_PROFILE_PROP -> System.out.println("Clicked on edit profile");
                case UserProfileView.ADD_PRODUCT_PROP -> System.out.println("Clicked on add product");
                case UserProfileView.MANAGE_USER_PROP -> System.out.println("Clicked on mange profile");
                case UserProfileView.LOG_SHOP_PROP -> System.out.println("Clicked on Status shop");
            }
        });
    }
}
