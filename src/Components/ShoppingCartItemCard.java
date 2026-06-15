package Components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;

import Model.CartItem;
import Model.Product;
import Util.ColorPalette;

public class ShoppingCartItemCard extends JPanel {

    private static final int SQUARE_SIZE = 40;
    private static final int CORNER_RADIUS = 15;
    private static final int THUMB_SIZE = 120;
    private static final int FIXED_HEIGHT = 195;

    private CartItem cartItem;

    private RoundedButton minusButton;
    private RoundedButton plusButton;
    private RoundedButton removeButton;
    private QuantityIndicator quantityIndicator;
    private JLabel availableLabel;

    private EventListenerList listenerList = new EventListenerList();

    public ShoppingCartItemCard(CartItem cartItem) {
        this.cartItem = cartItem;
        setupUI();
        attachEvents();
    }

    public CartItem getCartItem() {
        return cartItem;
    }

    public void updateQuantity(int newQty) {
        cartItem.setQuantity(newQty);
        quantityIndicator.setQuantity(newQty);
        updateButtons();
    }

    private void updateButtons() {
        minusButton.setEnabled(cartItem.getQuantity() > 1);
    }

    public void setMaxQuantity(int max) {
        int available = Math.max(0, max);
        plusButton.setEnabled(available > 0);
        availableLabel.setText("Available: " + available);
    }

    private void setupUI() {
        Product product = cartItem.getProduct();

        this.setBackground(ColorPalette.BG_SECONDARY);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.BORDER, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        this.setPreferredSize(new Dimension(0, FIXED_HEIGHT));
        this.setMinimumSize(new Dimension(0, FIXED_HEIGHT));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, FIXED_HEIGHT));

        this.setLayout(new BorderLayout());

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(ColorPalette.BG_SECONDARY);
        imagePanel.setPreferredSize(new Dimension(THUMB_SIZE, THUMB_SIZE));

        String location = product.getThumbnail();
        String imagePath = (location == null || location.isEmpty())
                ? "icons/Product_placeholder.png"
                : location;

        ImageIcon icon = new ImageIcon(imagePath);
        Image scaled = icon.getImage().getScaledInstance(THUMB_SIZE, THUMB_SIZE, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaled), SwingConstants.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        add(imagePanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nameLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        nameLabel.setAlignmentX(LEFT_ALIGNMENT);
        textPanel.add(nameLabel);

        textPanel.add(Box.createVerticalStrut(4));

        JTextArea descArea = new JTextArea(product.getDescription());
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setOpaque(false);
        descArea.setForeground(ColorPalette.TEXT_MUTED);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descArea.setBorder(null);
        descArea.setFocusable(false);
        descArea.setAlignmentX(LEFT_ALIGNMENT);
        textPanel.add(descArea);

        rightPanel.add(textPanel, BorderLayout.CENTER);

        JPanel controlsBar = new JPanel();
        controlsBar.setLayout(new BoxLayout(controlsBar, BoxLayout.X_AXIS));
        controlsBar.setOpaque(false);

        ImageIcon minusIcon = loadAndScaleImage("icons/minus.png", 20, 20);
        ImageIcon plusIcon = loadAndScaleImage("icons/plus.png", 20, 20);
        ImageIcon trashIcon = loadAndScaleImage("icons/trash.png", 20, 20);

        minusButton = new RoundedButton(null, CORNER_RADIUS);
        minusButton.setIcon(minusIcon);
        minusButton.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
        minusButton.setMaximumSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
        minusButton.setMinimumSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));

        quantityIndicator = new QuantityIndicator(CORNER_RADIUS);
        quantityIndicator.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
        quantityIndicator.setMaximumSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
        quantityIndicator.setMinimumSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
        quantityIndicator.setQuantity(cartItem.getQuantity());

        plusButton = new RoundedButton(null, CORNER_RADIUS);
        plusButton.setIcon(plusIcon);
        plusButton.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
        plusButton.setMaximumSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
        plusButton.setMinimumSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));

        controlsBar.add(minusButton);
        controlsBar.add(Box.createHorizontalStrut(8));
        controlsBar.add(quantityIndicator);
        controlsBar.add(Box.createHorizontalStrut(8));
        controlsBar.add(plusButton);

        controlsBar.add(Box.createHorizontalStrut(20));

        JPanel priceSection = new JPanel();
        priceSection.setLayout(new BoxLayout(priceSection, BoxLayout.Y_AXIS));
        priceSection.setOpaque(false);
        priceSection.setAlignmentY(CENTER_ALIGNMENT);

        JPanel availableSection = new JPanel();
        availableSection.setLayout(new BoxLayout(availableSection, BoxLayout.X_AXIS));
        availableSection.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        availableSection.setOpaque(false);
        availableSection.setAlignmentY(CENTER_ALIGNMENT);
        availableLabel = new JLabel();
        availableLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        availableLabel.setForeground(ColorPalette.TEXT_MUTED);
        availableSection.add(availableLabel);

        int qty = cartItem.getQuantity();
        double originalSubtotal = product.getPrice() * qty;

        if (product.getDiscount() > 0) {
            double discountedUnit = product.getPrice() * (1 - product.getDiscount() / 100);
            double discountedSubtotal = discountedUnit * qty;

            JLabel originalLabel = new JLabel(
                    String.format("<html><strike>$%.2f</strike></html>", originalSubtotal));
            originalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            originalLabel.setForeground(ColorPalette.TEXT_MUTED);
            originalLabel.setAlignmentX(LEFT_ALIGNMENT);
            priceSection.add(originalLabel);

            JPanel discountedRow = new JPanel();
            discountedRow.setLayout(new BoxLayout(discountedRow, BoxLayout.X_AXIS));
            discountedRow.setOpaque(false);
            discountedRow.setAlignmentX(LEFT_ALIGNMENT);

            JLabel discountedLabel = new JLabel(String.format("$%.2f", discountedSubtotal));
            discountedLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            discountedLabel.setForeground(ColorPalette.ACCENT_PRIMARY);
            discountedRow.add(discountedLabel);

            discountedRow.add(Box.createHorizontalStrut(6));

            JLabel badge = new JLabel(String.format("-%d%%", (int) product.getDiscount()));
            badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
            badge.setForeground(ColorPalette.ACCENT_WARNING);
            discountedRow.add(badge);

            priceSection.add(discountedRow);
        } else {
            JLabel singleLabel = new JLabel(String.format("$%.2f", originalSubtotal));
            singleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            singleLabel.setForeground(ColorPalette.ACCENT_PRIMARY);
            singleLabel.setAlignmentX(LEFT_ALIGNMENT);
            priceSection.add(singleLabel);
        }

        controlsBar.add(priceSection);

        controlsBar.add(Box.createHorizontalStrut(16));

        controlsBar.add(Box.createHorizontalGlue());

        controlsBar.add(availableSection);

        removeButton = new RoundedButton(null, CORNER_RADIUS);
        removeButton.setHasBorder(false);
        removeButton.setIcon(trashIcon);
        removeButton.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
        removeButton.setMaximumSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
        removeButton.setMinimumSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
        removeButton.setBackground(ColorPalette.ACCENT_WARNING);
        controlsBar.add(removeButton);

        rightPanel.add(controlsBar, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.CENTER);

        updateButtons();
    }

    private void attachEvents() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fireActionEvent("cardClick");
            }
        });

        plusButton.addActionListener(e -> {
            fireActionEvent("plus");
        });

        minusButton.addActionListener(e -> {
            fireActionEvent("minus");
        });

        removeButton.addActionListener(e -> {
            fireActionEvent("remove");
        });
    }

    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    private void fireActionEvent(String command) {
        ActionListener[] listeners = listenerList.getListeners(ActionListener.class);
        if (listeners.length > 0) {
            ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);
            for (ActionListener listener : listeners) {
                listener.actionPerformed(event);
            }
        }
    }

    private ImageIcon loadAndScaleImage(String path, int maxWidth, int maxHeight) {
        ImageIcon originalIcon = new ImageIcon(path);
        Image originalImage = originalIcon.getImage();

        int originalWidth = originalImage.getWidth(null);
        int originalHeight = originalImage.getHeight(null);

        double widthRatio = (double) maxWidth / originalWidth;
        double heightRatio = (double) maxHeight / originalHeight;
        double scaleFactor = Math.min(widthRatio, heightRatio);

        int scaledWidth = (int) (originalWidth * scaleFactor);
        int scaledHeight = (int) (originalHeight * scaleFactor);

        Image scaledIcon = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledIcon);
    }
}
