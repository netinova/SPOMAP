package Components;

import java.awt.CardLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import Util.ColorPalette;
import Model.Invoice;
import Model.ViewType;
import View.*;

public class MultiViewPanel extends JPanel {

    private CardLayout cardLayout;
    private ShopView shopView;
    private AuthenticationView authenticationView;
    private UserProfileView userProfileView;
    private ProductView productView;
    private ShoppingCartView shoppingCartView;
    private InvoiceView invoiceView;
    private InvoiceDetailView invoiceDetailView;
    private SettingView settingView;

    public MultiViewPanel(ShopView shopView, AuthenticationView authenticationView, UserProfileView userProfileView,
            ProductView productView,
            ShoppingCartView shoppingCartView, InvoiceView invoiceView, InvoiceDetailView invoiceDetailView,
            SettingView settingView) {
        this.shopView = shopView;
        this.authenticationView = authenticationView;
        this.userProfileView = userProfileView;
        this.productView = productView;
        this.shoppingCartView = shoppingCartView;
        this.invoiceView = invoiceView;
        this.invoiceDetailView = invoiceDetailView;
        this.settingView = settingView;
        setupUI();

        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            revalidate();
            repaint();
        });
    }

    private void setupUI() {
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);
        this.setBackground(ColorPalette.getInstance().getBgMain());

        // border
        Border line = BorderFactory.createLineBorder(ColorPalette.getInstance().getBorder());
        Border etched = BorderFactory.createEtchedBorder();
        this.setBorder(BorderFactory.createCompoundBorder(line, etched));

        this.add(shopView, ViewType.SHOP.getViewId());
        this.add(authenticationView, ViewType.AUTH.getViewId());
        this.add(userProfileView, ViewType.USER.getViewId());
        this.add(productView, ViewType.PRODUCT.getViewId());
        this.add(shoppingCartView, ViewType.SHOPPING_CART.getViewId());
        this.add(invoiceView, ViewType.INVOICE.getViewId());
        this.add(invoiceDetailView, ViewType.INVOICE_DETAIL.getViewId());
        this.add(settingView, ViewType.SETTING.getViewId());
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

        if (viewType == ViewType.USER) {
            userProfileView.showMainProfile();
            userProfileView.loadUserData();
        }

        if (viewType == ViewType.INVOICE) {
            invoiceView.changeLayout();
        }

        cardLayout.show(this, viewId);
    }
}
