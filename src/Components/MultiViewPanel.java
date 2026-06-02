package Components;

import java.awt.CardLayout;

import javax.swing.JPanel;

import View.ProductView;
import View.ShopView;
import View.UserView;

public class MultiViewPanel extends JPanel {

    private ShopView shopView;
    private CardLayout cardLayout;
    private UserView userView;
    private ProductView productView;

    public static final String USER_VIEW = "userView";
    public static final String SHOP_VIEW = "shopView";
    public static final String PRODUCT_VIEW = "productView";

    public MultiViewPanel(ShopView shopView, UserView userView, ProductView productView) {
        this.shopView = shopView;
        this.userView = userView;
        this.productView = productView;
        setupUI();
    }

    private void setupUI() {
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        this.add(shopView, SHOP_VIEW);
        this.add(userView, USER_VIEW);
        this.add(productView, PRODUCT_VIEW);

        // Show shop view by default
        cardLayout.show(this, SHOP_VIEW);
    }

    /**
     * Switch to a specific view
     */
    public void switchView(String viewId) {
        cardLayout.show(this, viewId);
    }
}
