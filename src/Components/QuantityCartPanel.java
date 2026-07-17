package Components;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Util.ColorPalette;

public class QuantityCartPanel extends JPanel {

    private static final int SQUARE_SIZE = 40;
    private static final int CORNER_RADIUS = 15;
    private static final Font CART_FONT = new Font("Segoe UI", Font.BOLD, 16);

    private RoundedButton minusButton;
    private RoundedButton plusButton;
    private QuantityIndicator quantityIndicator;
    private RoundedButton cartButton;

    private int quantity = 1;
    private int maxQuantity = Integer.MAX_VALUE;
    private boolean isOutOfStock = false;

    public QuantityCartPanel() {
        setupUI();
        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            revalidate();
            repaint();
        });
    }

    private void setupUI() {
        removeAll();
        setBackground(ColorPalette.getInstance().getBgSecondary());
        setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 16);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
        leftPanel.setOpaque(false);

        ImageIcon minusIcon = loadAndScaleImage("icons/minus.png", 20, 20);
        ImageIcon plusIcon = loadAndScaleImage("icons/plus.png", 20, 20);

        minusButton = new RoundedButton(null, CORNER_RADIUS);
        minusButton.setIcon(minusIcon);
        minusButton.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
        minusButton.setMaximumSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
        minusButton.setMinimumSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));

        plusButton = new RoundedButton(null, CORNER_RADIUS);
        plusButton.setIcon(plusIcon);
        plusButton.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
        plusButton.setMaximumSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
        plusButton.setMinimumSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));

        quantityIndicator = new QuantityIndicator(CORNER_RADIUS);
        quantityIndicator.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
        quantityIndicator.setMaximumSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
        quantityIndicator.setMinimumSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
        quantityIndicator.setQuantity(quantity);

        leftPanel.add(minusButton);
        leftPanel.add(Box.createHorizontalStrut(8));
        leftPanel.add(quantityIndicator);
        leftPanel.add(Box.createHorizontalStrut(8));
        leftPanel.add(plusButton);

        add(leftPanel, gbc);

        cartButton = new RoundedButton("Add to Cart", CORNER_RADIUS);
        cartButton.setFont(CART_FONT);
        cartButton.setForeground(ColorPalette.getInstance().getTextPrimary());
        cartButton.setBackground(ColorPalette.getInstance().getAccentSuccess());
        cartButton.setPreferredSize(new Dimension(200, SQUARE_SIZE));
        cartButton.setContentAreaFilled(false);
        cartButton.setHasBorder(false);
        ImageIcon cartIcon = loadAndScaleImage("icons/shopping_cart.png", 20, 20);
        cartButton.setIcon(cartIcon);
        cartButton.setIconTextGap(12);
        cartButton.setHorizontalTextPosition(SwingConstants.RIGHT);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        add(cartButton, gbc);
    }

    public void setMaxQuantity(int max) {
        this.isOutOfStock = max <= 0;
        this.maxQuantity = Math.max(1, max);
        if (quantity > maxQuantity) {
            setQuantity(maxQuantity);
        }
        updateButtons();
    }

    private void updateButtons() {
        plusButton.setEnabled(!isOutOfStock && quantity < maxQuantity);
        cartButton.setEnabled(!isOutOfStock);
    }

    public int getQuantity() {
        return quantity;
    }

    public void resetQuantity() {
        setQuantity(1);
    }

    public void setQuantity(int qty) {
        this.quantity = Math.max(1, qty);
        quantityIndicator.setQuantity(quantity);
        updateButtons();
    }

    public void addMinusListener(ActionListener listener) {
        minusButton.addActionListener(e -> {
            if (quantity > 1) {
                setQuantity(quantity - 1);
                e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, String.valueOf(quantity));
                listener.actionPerformed(e);
            }
        });
    }

    public void addPlusListener(ActionListener listener) {
        plusButton.addActionListener(e -> {
            setQuantity(quantity + 1);
            e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, String.valueOf(quantity));
            listener.actionPerformed(e);
        });
    }

    public void addCartListener(ActionListener listener) {
        cartButton.addActionListener(listener);
    }

    private ImageIcon loadAndScaleImage(String path, int maxWidth, int maxHeight) {
        ImageIcon originalIcon = new ImageIcon(path);
        Image originalImage = originalIcon.getImage();

        int originalWidth = originalImage.getWidth(null);
        int originalHeight = originalImage.getHeight(null);

        // Calculate scaling factor to fit within max dimensions while preserving aspect
        // ratio
        double widthRatio = (double) maxWidth / originalWidth;
        double heightRatio = (double) maxHeight / originalHeight;
        double scaleFactor = Math.min(widthRatio, heightRatio);

        int scaledWidth = (int) (originalWidth * scaleFactor);
        int scaledHeight = (int) (originalHeight * scaleFactor);

        Image scaledIcon = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledIcon);
    }

}
