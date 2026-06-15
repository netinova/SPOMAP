package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.plaf.basic.BasicScrollBarUI;

import Components.RoundedButton;
import Components.ShoppingCartItemCard;
import Controller.ShoppingCartController;
import Model.AppState;
import Model.CartItem;
import Model.ShoppingCart;
import Util.ColorPalette;

public class ShoppingCartView extends JPanel implements PropertyChangeListener {

    private JPanel itemsGrid;
    private JPanel summaryPanel;
    private JLabel rawPriceValue;
    private JLabel discountValue;
    private JLabel finalPriceValue;
    private RoundedButton payButton;

    private ShoppingCartController controller;

    private boolean subscribed = false;

    public ShoppingCartView(ShoppingCartController controller) {
        this.controller = controller;
        setupUI();
    }

    public void subscribeToModel(ShoppingCart cart) {
        cart.addPropertyChangeListener(this);
        subscribed = true;
    }

    public void loadCartItems() {
        ShoppingCart cart = AppState.getInstance().getCart();
        if (cart != null) {
            if (!subscribed) {
                subscribeToModel(cart);
            }
            displayItems(cart.getItems());
        }
    }

    void setupUI() {
        this.setBackground(ColorPalette.BG_MAIN);
        this.setLayout(new BorderLayout());

        itemsGrid = new JPanel();
        itemsGrid.setBackground(ColorPalette.BG_MAIN);
        itemsGrid.setLayout(new GridBagLayout());
        itemsGrid.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        // wrapping the grid in a scroll pane
        JScrollPane scrollPane = new JScrollPane(itemsGrid);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.setBackground(ColorPalette.BG_MAIN);

        // Custom scrollbar styling
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        styleScrollBar(verticalBar);

        this.add(scrollPane, BorderLayout.CENTER);

        summaryPanel = createSummaryPanel();
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ColorPalette.BG_SECONDARY);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, ColorPalette.BORDER),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font finalValueFont = new Font("Segoe UI", Font.BOLD, 18);

        rawPriceValue = new JLabel("$0.00");
        rawPriceValue.setFont(valueFont);
        rawPriceValue.setForeground(ColorPalette.TEXT_MUTED);
        rawPriceValue.setHorizontalAlignment(JLabel.TRAILING);

        discountValue = new JLabel("-$0.00");
        discountValue.setFont(valueFont);
        discountValue.setForeground(ColorPalette.ACCENT_SUCCESS);
        discountValue.setHorizontalAlignment(JLabel.TRAILING);

        finalPriceValue = new JLabel("$0.00");
        finalPriceValue.setFont(finalValueFont);
        finalPriceValue.setForeground(ColorPalette.TEXT_PRIMARY);
        finalPriceValue.setHorizontalAlignment(JLabel.TRAILING);

        JLabel rawLabel = new JLabel("Raw Price");
        rawLabel.setFont(labelFont);
        rawLabel.setForeground(ColorPalette.TEXT_MUTED);

        JLabel discountLabel = new JLabel("Discounts");
        discountLabel.setFont(labelFont);
        discountLabel.setForeground(ColorPalette.TEXT_MUTED);

        JLabel finalLabel = new JLabel("Final Price");
        finalLabel.setFont(finalValueFont);
        finalLabel.setForeground(ColorPalette.TEXT_PRIMARY);

        panel.add(createSummaryRow(rawLabel, rawPriceValue));
        panel.add(Box.createVerticalStrut(4));
        panel.add(createSummaryRow(discountLabel, discountValue));

        JSeparator separator = new JSeparator();
        separator.setForeground(ColorPalette.BORDER);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(Box.createVerticalStrut(6));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(6));

        panel.add(createSummaryRow(finalLabel, finalPriceValue));

        panel.add(Box.createVerticalStrut(12));

        payButton = new RoundedButton("Pay", 12);
        payButton.setHasBorder(false);
        payButton.setBackground(ColorPalette.ACCENT_SUCCESS);
        payButton.setForeground(ColorPalette.TEXT_PRIMARY);
        payButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        payButton.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        payButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        payButton.setPreferredSize(new Dimension(200, 50));
        payButton.addActionListener(e -> controller.handleCheckout());

        JPanel buttonWrapper = new JPanel(new BorderLayout());
        buttonWrapper.setOpaque(false);
        buttonWrapper.add(payButton, BorderLayout.CENTER);
        panel.add(buttonWrapper);

        return panel;
    }

    private JPanel createSummaryRow(JLabel label, JLabel value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        row.add(label, BorderLayout.WEST);
        row.add(value, BorderLayout.EAST);
        return row;
    }

    private void updateSummary(ArrayList<CartItem> items) {
        double rawTotal = 0;
        double discountTotal = 0;

        for (CartItem item : items) {
            double price = item.getProduct().getPrice();
            int qty = item.getQuantity();
            double discount = item.getProduct().getDiscount();

            rawTotal += price * qty;
            if (discount > 0) {
                discountTotal += price * (discount / 100) * qty;
            }
        }

        double finalTotal = rawTotal - discountTotal;

        rawPriceValue.setText(String.format("$%.2f", rawTotal));
        discountValue.setText(String.format("-$%.2f", discountTotal));
        finalPriceValue.setText(String.format("$%.2f", finalTotal));
        payButton.setEnabled(finalTotal > 0);
    }

    public void displayItems(ArrayList<CartItem> items) {
        itemsGrid.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new java.awt.Insets(5, 0, 5, 0);

        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            ShoppingCartItemCard card = new ShoppingCartItemCard(item);

            card.addActionListener(e -> {
                switch (e.getActionCommand()) {
                    case "plus" -> controller.handleIncreaseQuantity(item);
                    case "minus" -> controller.handleDecreaseQuantity(item);
                    case "remove" -> controller.handleRemoveItem(item);
                    case "cardClick" -> controller.handleItemCardClick(item);
                }
            });

            ShoppingCart cart = AppState.getInstance().getCart();
            if (cart != null) {
                card.setMaxQuantity(cart.getAvailableQuantity(item.getProduct()));
            }

            gbc.gridy = i;
            itemsGrid.add(card, gbc);
        }

        // Add a vertical filler at the bottom so cards stay top-aligned
        gbc.gridy = items.size();
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        itemsGrid.add(filler, gbc);

        itemsGrid.revalidate();
        itemsGrid.repaint();

        if (items.size() > 0) {
            if (summaryPanel.getParent() == null) {
                this.add(summaryPanel, BorderLayout.SOUTH);
                this.revalidate();
            }
            updateSummary(items);
        } else {
            if (summaryPanel.getParent() == this) {
                this.remove(summaryPanel);
                this.revalidate();
                this.repaint();
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ShoppingCart.PROP_ITEMS)) {
            ArrayList<CartItem> newItems = (ArrayList<CartItem>) evt.getNewValue();
            displayItems(newItems);
        }
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

}
