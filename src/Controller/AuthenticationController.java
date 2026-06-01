package Controller;

import java.awt.event.ActionEvent;

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

}
