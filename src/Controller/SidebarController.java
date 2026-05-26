package Controller;

import Components.MultiViewPanel;

public class SidebarController {

    public interface onChangeViewListener {

        void changeView(String viewId);
    }

    private onChangeViewListener listener;

    public void setOnChangeViewListener(onChangeViewListener listener) {
        this.listener = listener;
    }

    public void HandelButtonOptionalClick(int status) {
        switch (status) {
            case 0:
                System.out.println("clicked on Factors");
                break;
            case 2:
                System.out.println("clicked on settings");
                break;
            default:
                break;
        }
    }

    public void HandelLogoSidebar() {
        System.out.println("clicked on logo sidebar");
        listener.changeView(MultiViewPanel.SHOP_VIEW);
    }
}
