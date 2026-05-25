package Controller;

import Components.MultiViewPanel;

public class NavigationController {

    public interface onChangeViewListener {

        void changeView(String viewId);
    }

    private onChangeViewListener listener;

    public void setOnChangeViewListener(onChangeViewListener listener) {
        this.listener = listener;
    }

    public void searchProducts(String searchString) {
        System.out.println(searchString);
    }

    public void onUserIconClick() {
        System.out.println("clicked on Icon user");

        if (listener != null) {
            listener.changeView(MultiViewPanel.USER_VIEW_PROPERTY);
        }
    }
}
