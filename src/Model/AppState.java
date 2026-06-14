package Model;

public class AppState {

    private static AppState instance;

    private boolean isUserLoggedIn;

    public boolean isUserLoggedIn() {
        return isUserLoggedIn;
    }

    public void setUserLoggedIn(boolean isUserLoggedIn) {
        this.isUserLoggedIn = isUserLoggedIn;
    }

    private AppState() {
    }

    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }

        return instance;
    }
}
