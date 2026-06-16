package Controller;

import Model.ViewType;

public class SidebarController {

    private OnChangeViewListener listener;

    public void setOnChangeViewListener(OnChangeViewListener listener) {
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
        listener.changeView(ViewType.SHOP.getViewId());
    }
}
