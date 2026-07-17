package Components;

import Util.ColorPalette;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class IconNavbarLabel extends JPanel {

    private RoundedButton shoppingCartIcon;
    private RoundedButton userLabel;

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
        this.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        // add notification logo icon

        shoppingCartIcon = new RoundedButton("", 45);
        shoppingCartIcon.setPreferredSize(new Dimension(45, 45));

        File svgFile = new File("icons/shopping_cart.svg");
        FlatSVGIcon shoppingCartImage = new FlatSVGIcon(svgFile).derive(35, 35);

        if (!shoppingCartImage.hasFound()) {
            System.err.println("SVG not found: " + svgFile.getAbsolutePath());
        }
        shoppingCartImage
                .setColorFilter(new FlatSVGIcon.ColorFilter(c -> ColorPalette.getInstance().getAccentPrimary()));

        shoppingCartIcon.setIcon(shoppingCartImage);
        shoppingCartIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.add(shoppingCartIcon);

        // add user logo icon
        userLabel = new RoundedButton("", 45);
        userLabel.setPreferredSize(new Dimension(45, 45));
        svgFile = new File("icons/user.svg");
        FlatSVGIcon userIcon = new FlatSVGIcon(svgFile).derive(35, 35);

        if (!userIcon.hasFound()) {
            System.err.println("SVG not found: " + svgFile.getAbsolutePath());
        }
        userIcon
                .setColorFilter(new FlatSVGIcon.ColorFilter(c -> ColorPalette.getInstance().getAccentPrimary()));

        userLabel.setIcon(userIcon);
        userLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.add(userLabel);
    }
}
