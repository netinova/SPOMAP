package Controller;

import Components.MultiViewPanel;
import Model.AppState;

public class ProductController {

    private OnChangeViewListener listener;

    public void setOnChangeViewListener(OnChangeViewListener listener) {
        this.listener = listener;
    }

    public void handleAddToCart() {
        System.out.println("Clicked add to cart!");

        if (listener == null) {
            return;
        }

        if (!AppState.getInstance().isUserLoggedIn()) {
            listener.changeView(MultiViewPanel.AUTH_VIEW);
            return;
        }

        

    }

}
