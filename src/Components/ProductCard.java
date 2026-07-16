package Components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.EventListenerList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Model.Product;
import Util.ColorPalette;
import Util.UIUtils;

public class ProductCard extends JPanel {
    private ImageIcon productImage;
    private JLabel productName;
    private JLabel productPrice;
    private JLabel productDiscount;
    private JPanel imagePanel;
    private JPanel detailsPanel;
    private JPanel pricePanel;
    private Product product;
    private EventListenerList listenerList = new EventListenerList();

    public ProductCard(Product product) {
        this.product = product;

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
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ColorPalette.getInstance().getAccentPrimary(), 2),
                        BorderFactory.createEmptyBorder(0, 0, 0, 0)));
            }

            public void mouseExited(MouseEvent evt) {
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ColorPalette.getInstance().getBorder(), 1),
                        BorderFactory.createEmptyBorder(0, 0, 0, 0)));
            }

            public void mouseClicked(MouseEvent evt) {
                fireActionEvent();
            }
        });
    }

    private void setupUI() {
        removeAll();
        this.setLayout(new BorderLayout());
        this.setBackground(ColorPalette.getInstance().getBgSecondary());
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.getInstance().getBorder(), 1),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        this.setPreferredSize(new Dimension(220, 320));
        this.setMaximumSize(new Dimension(220, 320));

        imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(ColorPalette.getInstance().getBgSecondary());
        imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String location = product.getThumbnail();

        String imagePath = (location == null || location.isEmpty())
                ? "icons/Product_placeholder.png"
                : location;

        productImage = new ImageIcon(imagePath);
        Image scaledImage = productImage.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        productImage = new ImageIcon(scaledImage);

        JLabel imageLabel = new JLabel(productImage, SwingConstants.CENTER);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        add(imagePanel, BorderLayout.CENTER);

        detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(ColorPalette.getInstance().getBgSecondary());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(5, 12, 15, 12));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 0, 5, 0);

        this.productName = new JLabel(product.getName());
        this.productName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        this.productName.setForeground(ColorPalette.getInstance().getTextPrimary());
        this.productName.setHorizontalAlignment(SwingConstants.CENTER);
        detailsPanel.add(this.productName, gbc);

        gbc.gridy = 1;

        this.pricePanel = new JPanel();
        this.pricePanel.setBackground(ColorPalette.getInstance().getBgSecondary());
        this.pricePanel.setLayout(new BorderLayout());
        detailsPanel.add(this.pricePanel, gbc);

        this.productPrice = new JLabel(String.format("$%.2f", product.getPrice()));
        this.productPrice.setFont(new Font("Segoe UI", Font.BOLD, 16));
        this.productPrice.setForeground(ColorPalette.getInstance().getAccentPrimary());
        this.productPrice.setHorizontalAlignment(SwingConstants.CENTER);
        this.pricePanel.add(productPrice, BorderLayout.CENTER);

        this.productDiscount = new JLabel(String.format("%%%.0f", product.getDiscount()));
        this.productDiscount.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        this.productDiscount.setForeground(ColorPalette.getInstance().getAccentSuccess());
        this.productDiscount.setHorizontalAlignment(SwingConstants.CENTER);
        if (product.getDiscount() == 0.0) {
            this.productDiscount.setVisible(false);
        }
        this.pricePanel.add(productDiscount, BorderLayout.EAST);

        add(detailsPanel, BorderLayout.SOUTH);
    }

    // Add standard ActionListener support
    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    private void fireActionEvent() {
        ActionListener[] listeners = listenerList.getListeners(ActionListener.class);
        if (listeners.length > 0) {
            ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                    product.getId() != null ? product.getId() : "");
            for (ActionListener listener : listeners) {
                listener.actionPerformed(event);
            }
        }
    }

    public Product getProduct() {
        return product;
    }
}
