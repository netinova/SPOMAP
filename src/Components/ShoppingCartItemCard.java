package Components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventListener;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import Model.CartItem;

public class ShoppingCartItemCard extends JPanel {

    private CartItem cartItem;

    private ItemCardClickListener listener;

    public interface ItemCardClickListener {
        void onItemCardClick(CartItem cartItem);
    }

    public void setItemCardClickListener(ItemCardClickListener listener) {
        this.listener = listener;
    }

    public ShoppingCartItemCard() {

        attachEvents();
        setupUI();
    }

    private void setupUI() {

    }

    void attachEvents() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onItemCardClick(cartItem);
            }
        });
    }

    public ShoppingCartItemCard(CartItem cartItem) {
        this.cartItem = cartItem;
    }
}
