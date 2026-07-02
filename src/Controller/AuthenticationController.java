package Controller;

import Model.AppState;
import Model.PrimeUser;
import Model.ShoppingCart;
import Model.User;
import Model.ViewType;
import Service.UserService;
import Util.PasswordHasher;
import View.AuthenticationView;
import Util.Validator;
import Util.Validator.ValidationResult;

import static Util.PasswordHasher.hashingPassword;

import javax.swing.plaf.multi.MultiPanelUI;

public class AuthenticationController {

    private OnChangeViewListener listener;

    public void setOnChangeViewListener(OnChangeViewListener listener) {
        this.listener = listener;
    }

    @SuppressWarnings("unused")
    private AuthenticationView view;

    public void setView(AuthenticationView view) {
        this.view = view;
    }

    // validation methods
    // View sends user input to controller, controller validates and returns result

    public ValidationResult validatePhoneNumber(String phone) {
        return Validator.validatePhone(phone);
    }

    public ValidationResult validateFirstName(String firstName) {
        return Validator.validateFirstName(firstName);
    }

    public ValidationResult validateLastName(String lastName) {
        return Validator.validateLastName(lastName);
    }

    public ValidationResult validatePassword(String password) {
        return Validator.validatePassword(password.trim());
    }

    public ValidationResult validateConfirmPassword(String password, String confirmPassword) {
        return Validator.validateConfirmPassword(password, confirmPassword);
    }

    public ValidationResult validateLoginPassword(String password) {
        return Validator.validateLoginPassword(password);
    }

    public ValidationResult validateFoundUs(String foundUs) {
        return Validator.validateFindUs(foundUs);
    }

    // input singUp change handlers

    public void onPhoneNumberChange(String value) {
        System.out.println("Phone changed: " + value);
    }

    public void onFirstNameChange(String value) {
        System.out.println("First name changed: " + value);
    }

    public void onLastNameChange(String value) {
        System.out.println("Last name changed: " + value);
    }

    public void onPasswordChange(String value) {
        System.out.println("Password changed");
    }

    public void onConfirmPasswordChange(String value) {
        System.out.println("Confirm password changed");
    }

    public void onFoundUsChange(String value) {
        System.out.println("Found us changed: " + value);
    }

    // input logIn change handlers

    public void onUsernameLoginChange(String value) {
        System.out.println("user changed: " + value);
    }

    public void onPasswordLoginChange(String value) {
        System.out.println("Password changed" + value);
    }

    // check full input of SingUp panel
    public boolean validateFullSingUpForm(String phone, String fName, String lName, String pass, String passConfirm,
            String findUs) {
        ValidationResult phoneResult = validatePhoneNumber(phone);
        int tempNum = 0;
        if (!phoneResult.isValid()) {
            if (view != null)
                view.showPhoneError(phoneResult.getErrorMessage());
            tempNum++;
        }

        ValidationResult firstNameResult = validateFirstName(fName);
        if (!firstNameResult.isValid()) {
            if (view != null)
                view.showFirstNameError(firstNameResult.getErrorMessage());
            tempNum++;
        }

        ValidationResult lastNameResult = validateLastName(lName);
        if (!lastNameResult.isValid()) {
            if (view != null)
                view.showLastNameError(lastNameResult.getErrorMessage());
            tempNum++;
        }
        ValidationResult passwordResult = validatePassword(pass);
        if (!passwordResult.isValid()) {
            if (view != null)
                view.showPasswordError(passwordResult.getErrorMessage());
            tempNum++;
        }

        ValidationResult confirmResult = validateConfirmPassword(pass, passConfirm);
        if (!confirmResult.isValid()) {
            if (view != null)
                view.showConfirmPasswordError(confirmResult.getErrorMessage());
            tempNum++;
        }

        ValidationResult findUsResult = validateFoundUs(findUs);
        if (!findUsResult.isValid()) {
            if (view != null)
                view.showFindUsError(findUsResult.getErrorMessage());
            tempNum++;
        }
        if (tempNum != 0)
            return false;

        return true;
    }

    // check full input in logIn
    public boolean validateFullLogin(String username, String password) {
        ValidationResult usernameResult = validatePhoneNumber(username);
        int tempNum = 0;
        if (!usernameResult.isValid()) {
            if (view != null)
                view.showLoginUsernameError(usernameResult.getErrorMessage());
            tempNum++;
        }
        ValidationResult passwordResult = validateLoginPassword(password);
        if (!passwordResult.isValid()) {
            if (view != null)
                view.showLoginPasswordError(passwordResult.getErrorMessage());
            tempNum++;
        }
        if (tempNum != 0)
            return false;

        return true;
    }

    // buttons

    public void onSingUp() {
        if (view != null && view.validateAndSignUp()) {
            String passwordHashed = hashingPassword(view.getPassword());
            boolean statusSingUp = UserService.registerNormalUser(
                    view.getSingUpPanel().getFirstName(),
                    view.getSingUpPanel().getLastName(),
                    view.getSingUpPanel().getPhoneNumber(),
                    passwordHashed,
                    AppState.getInstance().normalUsersList,
                    AppState.getInstance().primeUsersList,
                    AppState.getInstance().adminUsersList);
            if (statusSingUp) {
                System.out.println("successfully singUp");
                User user = UserService.login(view.getSingUpPanel().getPhoneNumber(),view.getPassword(),
                        AppState.getInstance().normalUsersList,
                        AppState.getInstance().primeUsersList,
                        AppState.getInstance().adminUsersList);
                AppState.getInstance().setLoggedInUser(user);
                AppState.getInstance().setCart(new ShoppingCart());
                listener.changeView(ViewType.USER.getViewId());
            }
        }

    }

    public void onShowLogIn() {
        System.out.println("switch LogIn");
        if (view != null)
            view.showLoginPanel();

    }

    public void onLogin() {
        if (view != null) {
            String username = view.getLoginUsername();
            String password = view.getLoginPassword();
            System.out.println(PasswordHasher.hashingPassword(password));
            boolean isValid = validateFullLogin(username, password);
            if (isValid) {
                User user = UserService.login(username, password,
                        AppState.getInstance().normalUsersList,
                        AppState.getInstance().primeUsersList,
                        AppState.getInstance().adminUsersList);
                if (user != null) {
                    System.out.println("successfully logged in");
                    AppState.getInstance().setLoggedInUser(user);
                    AppState.getInstance().setCart(new ShoppingCart());
                    listener.changeView(ViewType.USER.getViewId());
                } else
                    System.out.println("failed to login");
            }
        }
    }

    public void onShowSingUp() {
        System.out.println("switch SingUp");
        if (view != null)
            view.showSingUpPanel();

    }
}
