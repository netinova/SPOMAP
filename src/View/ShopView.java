package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;

import Components.ProductCard;
import Controller.ShopController;
import Model.Product;
import Model.ProductCatalog;
import Util.ColorPalette;

public class ShopView extends JPanel implements PropertyChangeListener {

    private JPanel productGrid;

    private ShopController controller;

    public ShopView(ShopController controller, ProductCatalog model) {

        this.controller = controller;

        setupUI();
        attachEvents();

        model.addListener(this); // subscribing to model
    }

    private void attachEvents() {

    }

    private void setupUI() {
        this.setBackground(ColorPalette.BG_MAIN);
        this.setLayout(new BorderLayout());

        // border
        Border line = BorderFactory.createLineBorder(ColorPalette.BORDER);
        Border etched = BorderFactory.createEtchedBorder();
        this.setBorder(BorderFactory.createCompoundBorder(line, etched));

        // productGrid
        productGrid = new JPanel();
        productGrid.setLayout(new GridLayout(0, 3, 10, 10));
        productGrid.setBackground(ColorPalette.BG_MAIN);
        productGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // wrapping the grid in a scroll pane
        JScrollPane scrollPane = new JScrollPane(productGrid);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        // Custom scrollbar styling
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.trackColor = ColorPalette.BG_MAIN;
                this.thumbColor = ColorPalette.BG_TERTIARY;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (thumbBounds.isEmpty() || !scrollbar.isEnabled())
                    return;

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Round the corners of the thumb
                int arc = 8;
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width - 1, thumbBounds.height - 1, arc, arc);

                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(trackColor);
                g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
                g2.dispose();
            }
        });

        verticalBar.setPreferredSize(new Dimension(8, 0));
        verticalBar.setUnitIncrement(16);

        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void displayProducts(ArrayList<Product> products) {
        productGrid.removeAll();

        for (Product product : products) {
            ProductCard card = new ProductCard(product);
            card.setOnProductClickListener(new ProductCard.OnProductClickListener() {
                @Override
                public void onProductClick(Product product) {
                    controller.handleProductClick(product);
                }
            });
            productGrid.add(card);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ProductCatalog.PROP_PRODUCTS)) {
            ArrayList<Product> newProducts = (ArrayList<Product>) evt.getNewValue();

            displayProducts(newProducts);
        }
    }
}
