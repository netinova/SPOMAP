package Controller;

import View.AuthenticationView;
import Util.Validator;
import Util.Validator.ValidationResult;

public class AuthenticationController {

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
        return Validator.validatePassword(password);
    }

    public ValidationResult validateConfirmPassword(String password, String confirmPassword) {
        return Validator.validateConfirmPassword(password, confirmPassword);
    }

    public ValidationResult validateFoundUs(String foundUs) {
        return Validator.validateFindUs(foundUs);
    }

    // input change handlers

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
}
