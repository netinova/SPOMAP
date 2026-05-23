package Components;

import Util.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class IconNavbarLabel extends JPanel {
    public IconNavbarLabel() {
        this.setOpaque(false);
        this.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));

        //add notification logo icon
        JLabel notificationLabel = new JLabel();
        ImageIcon notificationLogo = new ImageIcon("icons/notification.png");
        Image scaledNotificationIcon = notificationLogo.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        notificationLogo = new ImageIcon(scaledNotificationIcon);
        notificationLabel.setIcon(notificationLogo);
        notificationLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        notificationLabel.setBorder(new EmptyBorder(0, 0, 0, 12));
        notificationLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showPopup(notificationLabel);
            }
        });
        this.add(notificationLabel);

        //add user logo icon
        JLabel userLabel = new JLabel();
        ImageIcon userLogo = new ImageIcon("icons/male_user.png");
        Image scaledUserIcon = userLogo.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        notificationLogo = new ImageIcon(scaledUserIcon);
        userLabel.setIcon(notificationLogo);
        userLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        userLabel.setBorder(new EmptyBorder(0, 0, 0, 12));
        userLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("clicked on Icon user");
            }
        });
        this.add(userLabel);
    }
    private void showPopup(JComponent component)
    {
        int maxWidth=0;
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBackground(ColorPalette.BG_TERTIARY);
        popupMenu.setForeground(ColorPalette.TEXT_PRIMARY);
        popupMenu.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER));

        // add item
        for (int i = 0; i <5; i++) {
            JMenuItem item = new JMenuItem("template text"+i);
            item.setFont(new Font("Arial", Font.PLAIN, 17));
            item.addActionListener(e -> {
                System.out.println("clicked notification");
            });
            popupMenu.add(item);
            int itemWidth = item.getPreferredSize().width;
            maxWidth = Math.max(maxWidth, itemWidth);
        }
        popupMenu.show(component, component.getWidth()-maxWidth, component.getHeight());
    }
}
