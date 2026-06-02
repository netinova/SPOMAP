package Controller;

import View.AuthenticationView;

public class AuthenticationController {

    @SuppressWarnings("unused")
    private AuthenticationView view;

    public void setView(AuthenticationView view) {
        this.view = view;
    }

    public void usernameValueChange(String value) {
        System.out.println(value);
    }

    public void passwordValueChange(String newValue) {
        System.out.println(newValue);
    }

    public void passwordConfirmValueChange(String newValue) {
        System.out.println(newValue);
    }

    public void LNameValueChange(String newValue) {
        System.out.println(newValue);
    }

    public void FoundUSValueChange(String newValue) {
        System.out.println(newValue);
    }

    public void FNameValueChange(String newValue) {
        System.out.println(newValue);
    }
}
