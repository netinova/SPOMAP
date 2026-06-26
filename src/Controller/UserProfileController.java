package Controller;

import Components.ProfileMainPanel;
import Model.AppState;
import Model.PrimeUser;
import Model.User;
import Model.UserType;
import Model.ViewType;
import Service.UserService;
import Util.PasswordHasher;
import Util.Validator;
import View.UserProfileView;

public class UserProfileController {

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

        if (user.getUserType() == UserType.PRIME) {
            PrimeUser userPrime = (PrimeUser) user;
            view.displayPrimeUser(
                    userPrime.getCreditAmount(),
                    userPrime.getCreditAmount(),
                    userPrime.getMemberShipID()
            );

        }
    }

    // -------------------- Logout
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

    // ---------------- EditProfile methods --------------------

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

    public boolean fullValidator(String fName, String lName, String phoneNumber, String currentPassword, String newPassword, String confirmPassword) {
        Validator.ValidationResult result;
        int temp = 0;
        User user = AppState.getInstance().getLoggedInUser();

        result = validatePhoneNumber(phoneNumber);
        if (!result.isValid()) {
            if (view != null)
                view.showPhoneError(result.getErrorMessage());
            temp++;
        }

        result = validateFirstName(fName);
        if (!result.isValid()) {
            if (view != null)
                view.showFirstNameError(result.getErrorMessage());
            temp++;
        }

        result = validateLastName(lName);
        if (!result.isValid()) {
            if (view != null)
                view.showLastNameError(result.getErrorMessage());
            temp++;
        }

        //password
        if (currentPassword == "" && (newPassword != "" || confirmPassword != "")) {
            if (view != null)
                view.showCurrentPasswordError("Your password is incorrect");
            temp++;
        } else if (currentPassword != "") {
            boolean passwordResult = PasswordHasher.checkerPassword(currentPassword, user.getPassword());
            result = validatePassword(newPassword);
            if (!result.isValid())
                temp++;
            view.showNewtPasswordError(result.getErrorMessage());
            result = validateConfirmPassword(newPassword, confirmPassword);
            if (!result.isValid())
                temp++;
            view.showConfirmPasswordError(result.getErrorMessage());
            if (!passwordResult) {
                if (view != null)
                    view.showCurrentPasswordError("Your password is incorrect");
                temp++;
            }
        } else if (view != null) {
            view.showCurrentPasswordError("");
            view.showConfirmPasswordError("");
            view.showNewtPasswordError("");
        }


        if (temp != 0)
            return false;
        return true;
    }

    public boolean editProfileHandler(String fName, String lName, String phoneNumber, String newPassword) {
        User user = AppState.getInstance().getLoggedInUser();
        user.editProfile(fName,lName,phoneNumber,newPassword);
        if (user.getUserType().isAdmin())
            UserService.saveAdminUser(AppState.getInstance().adminUsersList);
        else if (user.getUserType().isPrime())
            UserService.savePrimeUser(AppState.getInstance().primeUsersList);
        else
            UserService.saveNormalUser(AppState.getInstance().normalUsersList);
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

    // listener edit profile
    public void onPhoneNumberChange(String value) {
        System.out.println("Phone changed: " + value);
    }

    public void onFirstNameChange(String value) {
        System.out.println("First name changed: " + value);
    }

    public void onLastNameChange(String value) {
        System.out.println("Last name changed: " + value);
    }

    public void onPasswordCurrentChange(String newValue) {
        System.out.println("Password current change");
    }

    public void onPasswordChange(String value) {
        System.out.println("Password changed");
    }

    public void onConfirmPasswordChange(String value) {
        System.out.println("Confirm password changed");
    }


    // --------------- Charge wallet ------------------
    public void loadChargeWalletData(){
        User user = AppState.getInstance().getLoggedInUser();
        if (user == null || view == null)
            return;
        view.loadChargeWalletData(String.format("%.2f",user.getBalance()));
    }

    public Validator.ValidationResult validateAmount(String amount) {
        return Validator.validationDouble(amount);
    }

    // listener Charging
    public void onAmountChange(String value) {
        System.out.println("Amount changed: "+ value );
    }
    public void onChargeButtonClick(String balance) {
        User user = AppState.getInstance().getLoggedInUser();
        user.addBalance(Double.parseDouble(balance));

        if (user.getUserType().isPrime())
            UserService.savePrimeUser(AppState.getInstance().primeUsersList);
        else
            UserService.saveNormalUser(AppState.getInstance().normalUsersList);

        loadProfile();
        showMainPage();
    }
    public void onCancelClick() {
        showMainPage();
    }

    // Mange Users
    public void loadMangeUsers(){
        view.loadManageUsers();
    }

    //listener
    public void onSearchPhoneChange(String value) {
        System.out.println("Phone Search change: "+ value);
    }

    public void onSearchClicked(String string) {
        System.out.println("clicked on search");//TODO: search handle
    }

    private void attachViewEvents() {
    }

    public void showMainPage() {
        view.showMainProfile();
    }
}
