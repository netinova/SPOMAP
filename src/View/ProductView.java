package View;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Components.ColorSelectorPanel;
import Components.ImageGallery;
import Components.PricePanel;
import Components.ProductInfoPanel;
import Components.QuantityCartPanel;
import Components.StockLabel;
import Components.TechnicalSpecsPanel;
import Model.Product;
import Model.ProductCatalog;
import Util.ColorPalette;

public class ProductView extends JPanel implements PropertyChangeListener {

    private JPanel contentPanel;
    private ImageGallery imageGallery;
    private ProductInfoPanel productInfoPanel;
    private TechnicalSpecsPanel specsPanel;
    private QuantityCartPanel quantityCartPanel;
    private PricePanel pricePanel;
    private ColorSelectorPanel colorSelectorPanel;
    private StockLabel stockLabel;

    public ProductView(ProductCatalog model) {

        model.addListener(this);

        setupUI();
        attachEvents();
    }

    private void attachEvents() {
        quantityCartPanel.addCartListener(e -> {
            System.out.println("Clicked add to cart!");
        });

        quantityCartPanel.addMinusListener(e -> {
            int quantity = Integer.parseInt(e.getActionCommand());
            System.out.println("Clicked - ! quantity: " + quantity);
            pricePanel.updateQuantity(quantity);
        });

        quantityCartPanel.addPlusListener(e -> {
            int quantity = Integer.parseInt(e.getActionCommand());
            System.out.println("Clicked + ! quantity: " + quantity);
            pricePanel.updateQuantity(quantity);
        });

        colorSelectorPanel.addColorSelectionListener(color -> {
            System.out.println("Selected color: " + color);
        });

    }

    private void setupUI() {
        this.setBackground(ColorPalette.BG_MAIN);
        this.setLayout(new BorderLayout());

        contentPanel = new JPanel();
        contentPanel.setBackground(ColorPalette.BG_MAIN);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(ColorPalette.BORDER)));

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
        scrollPane.setBackground(ColorPalette.BG_MAIN);

        // Custom scrollbar styling
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        styleScrollBar(verticalBar);

        this.add(scrollPane, BorderLayout.CENTER);

    }

    private void styleScrollBar(JScrollBar bar) {

        bar.setUI(new BasicScrollBarUI() {
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
        bar.setPreferredSize(new Dimension(8, 0));
        bar.setUnitIncrement(16);

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ProductCatalog.PROP_SELECTED)) {
            Product product = (Product) evt.getNewValue();
            imageGallery.setImages(product.getProductImages());
            productInfoPanel.updateInfo(product.getName(), product.getDescription());
            specsPanel.setProduct(product);
            pricePanel.setPrice(product.getPrice(), product.getDiscount(), 1);
            quantityCartPanel.setQuantity(1);
            quantityCartPanel.setMaxQuantity(product.getStockQuantity());
            colorSelectorPanel.setColors(product.getColors());
            stockLabel.setStockQuantity(product.getStockQuantity());
        }
    }
}
