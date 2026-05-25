package Components;

import java.awt.CardLayout;

import javax.swing.JPanel;

import Controller.NavigationController;
import Controller.ShopController;
import Model.ProductCatalog;
import View.ShopView;
import View.UserView;

public class MultiViewPanel extends JPanel {

    private ShopView shopView;
    private CardLayout cardLayout;
    private UserView userView;

    public static final String USER_VIEW_PROPERTY = "userView";
    public static final String SHOP_VIEW_PROPERTY = "shopView";

    public MultiViewPanel(ShopController shopController, ProductCatalog productCatalog,
            NavigationController navigationController) {

        shopView = new ShopView(shopController, productCatalog);
        userView = new UserView();

        setupUI();
        attachEvents(navigationController);
    }

    private void attachEvents(NavigationController navigationController) {
        navigationController.setOnChangeViewListener(viewId -> {
            cardLayout.show(this, viewId);
        });
    }

    private void setupUI() {
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        this.add(shopView, SHOP_VIEW_PROPERTY);
        this.add(userView, USER_VIEW_PROPERTY);
    }

}
