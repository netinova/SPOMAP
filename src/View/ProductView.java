package View;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Components.ColorSelectorPanel;
import Components.ImageGallery;
import Components.PricePanel;
import Components.ProductInfoPanel;
import Components.QuantityCartPanel;
import Components.StockLabel;
import Components.TechnicalSpecsPanel;
import Controller.ProductController;
import Model.AppState;
import Model.Product;
import Model.ProductCatalog;
import Model.ShoppingCart;
import Util.ColorPalette;
import Util.UIUtils;

public class ProductView extends JPanel implements PropertyChangeListener {

    private JPanel contentPanel;
    private ImageGallery imageGallery;
    private ProductInfoPanel productInfoPanel;
    private TechnicalSpecsPanel specsPanel;
    private QuantityCartPanel quantityCartPanel;
    private PricePanel pricePanel;
    private ColorSelectorPanel colorSelectorPanel;
    private StockLabel stockLabel;

    ProductController controller;
    private Product currentProduct;
    private int selectedQuantity = 1;

    public void resetSelectedQuantity() {
        selectedQuantity = 1;
    }

    public ProductView(ProductController controller, ProductCatalog model) {

        model.addListener(this);
        this.controller = controller;

        setupUI();
        attachEvents();

        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            attachEvents();
            revalidate();
            repaint();
        });
    }

    private void attachEvents() {
        quantityCartPanel.addCartListener(e -> {
            controller.handleAddToCart(currentProduct, selectedQuantity);
            quantityCartPanel.resetQuantity();

            ShoppingCart cart = AppState.getInstance().getCart();
            if (cart != null) {
                quantityCartPanel.setMaxQuantity(cart.getAvailableQuantity(currentProduct));
            }

            int maxQuantity = (cart != null) ? cart.getAvailableQuantity(currentProduct)
                    : currentProduct.getStockQuantity();
            stockLabel.setStockQuantity(currentProduct.getStockQuantity(), maxQuantity);
        });

        quantityCartPanel.addMinusListener(e -> {
            int quantity = Integer.parseInt(e.getActionCommand());
            System.out.println("Clicked - ! quantity: " + quantity);
            this.selectedQuantity = quantity;

            pricePanel.updateQuantity(quantity);
        });

        quantityCartPanel.addPlusListener(e -> {
            int quantity = Integer.parseInt(e.getActionCommand());
            System.out.println("Clicked + ! quantity: " + quantity);
            this.selectedQuantity = quantity;
            pricePanel.updateQuantity(quantity);
        });

        colorSelectorPanel.addColorSelectionListener(color -> {
            System.out.println("Selected color: " + color);
        });

    }

    private void setupUI() {
        this.setBackground(ColorPalette.getInstance().getBgMain());
        this.setLayout(new BorderLayout());

        contentPanel = new JPanel();
        contentPanel.setBackground(ColorPalette.getInstance().getBgMain());
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(ColorPalette.getInstance().getBorder())));

        imageGallery = new ImageGallery();
        imageGallery.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(imageGallery);

        productInfoPanel = new ProductInfoPanel();
        productInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(productInfoPanel);

        specsPanel = new TechnicalSpecsPanel();
        specsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(specsPanel);

        colorSelectorPanel = new ColorSelectorPanel();
        colorSelectorPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(colorSelectorPanel);

        stockLabel = new StockLabel();
        stockLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(stockLabel);

        pricePanel = new PricePanel();
        pricePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(pricePanel);

        quantityCartPanel = new QuantityCartPanel();
        quantityCartPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(quantityCartPanel);

        // wrapping the grid in a scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.setBackground(ColorPalette.getInstance().getBgMain());

        // Custom scrollbar styling
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        UIUtils.styleScrollBar(verticalBar);

        this.add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ProductCatalog.PROP_SELECTED)) {
            Product product = (Product) evt.getNewValue();
            this.currentProduct = product;

            imageGallery.setImages(product.getProductImages());
            productInfoPanel.updateInfo(product.getName(), product.getDescription());
            specsPanel.setProduct(product);
            pricePanel.setPrice(product.getPrice(), product.getDiscount(), 1);
            quantityCartPanel.setQuantity(1);

            ShoppingCart cart = AppState.getInstance().getCart();
            int maxQuantity = (cart != null) ? cart.getAvailableQuantity(product) : product.getStockQuantity();

            quantityCartPanel.setMaxQuantity(maxQuantity);
            colorSelectorPanel.setColors(product.getColors());
            stockLabel.setStockQuantity(product.getStockQuantity(), maxQuantity);
        }
    }
}
