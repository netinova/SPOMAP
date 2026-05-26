package Components;

import java.awt.CardLayout;

import javax.swing.JPanel;

import Controller.NavigationController;
import Controller.ShopController;
import Controller.SidebarController;
import Model.ProductCatalog;
import View.ShopView;
import View.UserView;

public class MultiViewPanel extends JPanel {

    private ShopView shopView;
    private CardLayout cardLayout;
    private UserView userView;

    public static final String USER_VIEW = "userView";
    public static final String SHOP_VIEW = "shopView";

    public MultiViewPanel(ShopController shopController, ProductCatalog productCatalog,
            NavigationController navigationController, SidebarController sidebarController) {

        shopView = new ShopView(shopController, productCatalog);
        userView = new UserView();

        setupUI();
        attachEvents(navigationController, sidebarController);
    }

    private void attachEvents(NavigationController navigationController, SidebarController sidebarController) {
        navigationController.setOnChangeViewListener(viewId -> {
            cardLayout.show(this, viewId);
        });

        sidebarController.setOnChangeViewListener(viewId -> {
            cardLayout.show(this, viewId);
        });
    }

    private void setupUI() {
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        this.add(shopView, SHOP_VIEW);
        this.add(userView, USER_VIEW);
    }

}
