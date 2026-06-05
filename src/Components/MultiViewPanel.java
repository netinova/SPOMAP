package Components;

import java.awt.CardLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import Util.ColorPalette;
import View.ProductView;
import View.ShopView;
import View.AuthenticationView;

public class MultiViewPanel extends JPanel {

    private ShopView shopView;
    private CardLayout cardLayout;
    private AuthenticationView authenticationView;
    private ProductView productView;

    public static final String USER_VIEW = "userView";
    public static final String SHOP_VIEW = "shopView";
    public static final String PRODUCT_VIEW = "productView";

    public MultiViewPanel(ShopView shopView, AuthenticationView authenticationView, ProductView productView) {
        this.shopView = shopView;
        this.authenticationView = authenticationView;
        this.productView = productView;
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
        this.add(authenticationView, USER_VIEW);
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
