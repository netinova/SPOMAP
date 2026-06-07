package Model;

public class AppState {

    private static AppState instance;

    private AppState() {
    }

    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }

        return instance;
    }
}
