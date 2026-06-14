package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

import Components.ShoppingCartItemCard;
import Controller.ShoppingCartController;
import Model.AppState;
import Model.CartItem;
import Model.ShoppingCart;
import Util.ColorPalette;

public class ShoppingCartView extends JPanel implements PropertyChangeListener {

    private JPanel itemsGrid;

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
