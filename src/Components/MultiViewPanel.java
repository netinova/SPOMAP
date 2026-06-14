package Components;

import java.awt.CardLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import Util.ColorPalette;
import View.ProductView;
import View.ShopView;
import View.ShoppingCartView;
import View.AuthenticationView;

public class MultiViewPanel extends JPanel {

    private CardLayout cardLayout;
    private ShopView shopView;
    private AuthenticationView authenticationView;
    private ProductView productView;
    private ShoppingCartView shoppingCartView;

    public static final String SHOP_VIEW = "shopView";
    public static final String PRODUCT_VIEW = "productView";
    public static final String AUTH_VIEW = "authView";
    public static final String SHOPPING_CART_VIEW = "shoppingCartView";
    public static final String USER_VIEW = "userView";

    public MultiViewPanel(ShopView shopView, AuthenticationView authenticationView, ProductView productView,
            ShoppingCartView shoppingCartView) {
        this.shopView = shopView;
        this.authenticationView = authenticationView;
        this.productView = productView;
        this.shoppingCartView = shoppingCartView;
        setupUI();
    }

    private void setupUI() {
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);
        this.setBackground(ColorPalette.BG_MAIN);

        // border
        Border line = BorderFactory.createLineBorder(ColorPalette.BORDER);
        Border etched = BorderFactory.createEtchedBorder();
        this.setBorder(BorderFactory.createCompoundBorder(line, etched));

        this.add(shopView, SHOP_VIEW);
        this.add(authenticationView, AUTH_VIEW);
        this.add(productView, PRODUCT_VIEW);
        this.add(shoppingCartView, SHOPPING_CART_VIEW);

        // Show shop view by default
        cardLayout.show(this, SHOP_VIEW);
    }

    public AuthenticationView getAuthenticationView() {
        return authenticationView;
    }

    /**
     * Switch to a specific view
     */
    public void switchView(String viewId) {

        if (viewId.equals(MultiViewPanel.AUTH_VIEW)) {
            if (getAuthenticationView() != null)
                getAuthenticationView().showLoginPanel();
        }

        // if (multiViewPanel != null && multiViewPanel.getAuthenticationView() != null)
        // multiViewPanel.getAuthenticationView().showLoginPanel();

        cardLayout.show(this, viewId);
    }
}
