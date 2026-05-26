package Controller;

import View.SidebarView;

public class SidebarController {
    private SidebarView view;
    public SidebarController() {
        this.view = new SidebarView(this);
    }

    public void HandelButtonOptionalClick(int status){
        switch (status){
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

    public void HandelLogoSidebar(){
        System.out.println("clicked on logo sidebar");
    }
}
