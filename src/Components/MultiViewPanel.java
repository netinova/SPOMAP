package Components;

import java.awt.CardLayout;

import javax.swing.JPanel;

import View.ShopView;
import View.UserView;

public class MultiViewPanel extends JPanel {

    private ShopView shopView;
    private CardLayout cardLayout;
    private UserView userView;

    public static final String USER_VIEW = "userView";
    public static final String SHOP_VIEW = "shopView";

    public MultiViewPanel(ShopView shopView, UserView userView) {
        this.shopView = shopView;
        this.userView = userView;
        setupUI();
    }

    private void setupUI() {
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        this.add(shopView, SHOP_VIEW);
        this.add(userView, USER_VIEW);
        
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
