package Controller;

import Model.AppState;
import Model.PrimeUser;
import Model.User;
import Model.UserType;
import Model.ViewType;
import Service.UserService;
import Util.PasswordHasher;
import Util.Validator;
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


    // ---------------- EditProfile methods

    // validation methods
    // View sends user input to controller, controller validates and returns result
    public Validator.ValidationResult validatePhoneNumber(String phone) {
        return Validator.validatePhone(phone);
    }

    public Validator.ValidationResult validateFirstName(String firstName) {
        return Validator.validateFirstName(firstName);
    }

    public Validator.ValidationResult validateLastName(String lastName) {
        return Validator.validateLastName(lastName);
    }

    public Validator.ValidationResult validatePassword(String password) {
        return Validator.validatePassword(password.trim());
    }

    public Validator.ValidationResult validateConfirmPassword(String password, String confirmPassword) {
        return Validator.validateConfirmPassword(password, confirmPassword);
    }

    public boolean fullValidator(String fName , String lName , String phoneNumber, String currentPassword, String newPassword, String confirmPassword){
        Validator.ValidationResult result;
        int temp=0;
        User user = AppState.getInstance().getLoggedInUser();

        result= validatePhoneNumber(phoneNumber);
        if (!result.isValid()) {
            if (view != null)
                view.showPhoneError(result.getErrorMessage());
            temp++;
        }

        result=validateFirstName(fName);
        if (!result.isValid()){
            if (view!=null)
                view.showFirstNameError(result.getErrorMessage());
            temp++;
        }

        result=validateLastName(lName);
        if (!result.isValid()){
            if (view!=null)
                view.showLastNameError(result.getErrorMessage());
            temp++;
        }

        //password
        if (currentPassword=="" &&(newPassword!="" || confirmPassword!="")){
            if (view!=null)
                view.showCurrentPasswordError("Your password is incorrect");
            temp++;
        }
        else if (currentPassword!=""){
            boolean passwordResult = PasswordHasher.checkerPassword(currentPassword, user.getPassword());
            result = validatePassword(newPassword);
            if (!result.isValid())
                temp++;
            view.showNewtPasswordError(result.getErrorMessage());
            result = validateConfirmPassword(newPassword , confirmPassword);
            if (!result.isValid())
                temp++;
            view.showConfirmPasswordError(result.getErrorMessage());
            if (!passwordResult) {
                if (view != null)
                    view.showCurrentPasswordError("Your password is incorrect");
                temp++;
            }
        }else if (view!=null){
            view.showCurrentPasswordError("");
            view.showConfirmPasswordError("");
            view.showNewtPasswordError("");
        }


        if (temp!=0)
            return false;
        return true;
    }

    public void loadEditProfile() {
        User user = AppState.getInstance().getLoggedInUser();
        if (user == null || view == null)
            return;
        view.loadEditUserData(
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber()
        );
    }

    private void attachViewEvents() {
        view.addPropertyChangeListener(evt -> {
            switch (evt.getPropertyName()) {
                case UserProfileView.LOGOUT_PROP -> handleLogout();
                case UserProfileView.CHARGE_WALLET_PROP -> System.out.println("Clicked on charge wallet");
                case UserProfileView.EDIT_PROFILE_PROP -> view.showEditProfile();
                case UserProfileView.ADD_PRODUCT_PROP -> System.out.println("Clicked on add product");
                case UserProfileView.MANAGE_USER_PROP -> System.out.println("Clicked on mange profile");
                case UserProfileView.LOG_SHOP_PROP -> System.out.println("Clicked on Status shop");
            }
        });
    }

    public void showMainPage() {
        view.showMainProfile();
    }
}
