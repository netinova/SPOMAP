package Components;

import java.awt.CardLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import Util.ColorPalette;
import Model.ViewType;
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

        this.add(shopView, ViewType.SHOP.getViewId());
        this.add(authenticationView, ViewType.AUTH.getViewId());
        this.add(productView, ViewType.PRODUCT.getViewId());
        this.add(shoppingCartView, ViewType.SHOPPING_CART.getViewId());

        // Show shop view by default
        cardLayout.show(this, ViewType.SHOP.getViewId());
    }

    public AuthenticationView getAuthenticationView() {
        return authenticationView;
    }

    /**
     * Switch to a specific view
     */
    public void switchView(String viewId) {

        ViewType viewType = ViewType.fromViewId(viewId);

        if (viewType == ViewType.AUTH) {
            if (authenticationView != null)
                authenticationView.showLoginPanel();
        }

        if (viewType == ViewType.SHOPPING_CART) {
            shoppingCartView.loadCartItems();
        }

        cardLayout.show(this, viewId);
    }
}
