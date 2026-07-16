package View;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import Components.ProductCard;
import Controller.ShopController;
import Model.Product;
import Model.ProductCatalog;
import Util.ColorPalette;
import Util.UIUtils;

public class ShopView extends JPanel implements PropertyChangeListener {

    private JPanel productGrid;

    private ShopController controller;

    public ShopView(ShopController controller, ProductCatalog model) {

        this.controller = controller;

        setupUI();

        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            revalidate();
            repaint();
        });

        model.addListener(this); // subscribing to model
    }

    private void setupUI() {
        this.setBackground(ColorPalette.getInstance().getBgMain());
        this.setLayout(new BorderLayout());
        ;

        // productGrid
        productGrid = new JPanel();
        productGrid.setLayout(new GridLayout(0, 3, 10, 10));
        productGrid.setBackground(ColorPalette.getInstance().getBgMain());
        productGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // wrapping the grid in a scroll pane
        JScrollPane scrollPane = new JScrollPane(productGrid);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        // Custom scrollbar styling
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        UIUtils.styleScrollBar(verticalBar);

        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void displayProducts(ArrayList<Product> products) {
        ArrayList<Product> productsCopy = new ArrayList<>(products);

        SwingUtilities.invokeLater(() -> {
            productGrid.removeAll();

            for (Product product : productsCopy) {
                ProductCard card = new ProductCard(product);
                // Use standard ActionListener instead of custom callback
                card.addActionListener(e -> {
                    controller.handleProductClick(product);
                });
                productGrid.add(card);
            }

            productGrid.revalidate();
            productGrid.repaint();
        });

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ProductCatalog.PROP_PRODUCTS)) {
            ArrayList<Product> newProducts = (ArrayList<Product>) evt.getNewValue();

            displayProducts(newProducts);
        }
    }
}
