package Components;

import Util.ColorPalette;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class IconNavbarLabel extends JPanel {

    private JLabel shoppingCartIcon;
    private ImageIcon notificationLogo;
    private JLabel userLabel;

    public interface OnIconClickListener {

        void onUserIconClick();

        void onShoppingCartClick();
    }

    private OnIconClickListener listener;

    public void setOnIconClickListenerListener(OnIconClickListener listener) {
        this.listener = listener;
    }

    public IconNavbarLabel() {

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
        shoppingCartIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onShoppingCartClick();
            }
        });

        userLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onUserIconClick();
            }
        });
    }

    private void setupUI() {
        this.setOpaque(false);
        this.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));

        // add notification logo icon
        shoppingCartIcon = new JLabel();
        notificationLogo = new ImageIcon("icons/shopping_cart.png");
        Image scaledNotificationIcon = notificationLogo.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        notificationLogo = new ImageIcon(scaledNotificationIcon);
        shoppingCartIcon.setIcon(notificationLogo);
        shoppingCartIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        shoppingCartIcon.setBorder(new EmptyBorder(0, 0, 0, 12));
        this.add(shoppingCartIcon);

        // add user logo icon
        userLabel = new JLabel();
        ImageIcon userLogo = new ImageIcon("icons/male_user.png");
        Image scaledUserIcon = userLogo.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        notificationLogo = new ImageIcon(scaledUserIcon);
        userLabel.setIcon(notificationLogo);
        userLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        userLabel.setBorder(new EmptyBorder(0, 0, 0, 12));
        this.add(userLabel);
    }
}
