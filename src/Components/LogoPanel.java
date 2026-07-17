package Components;

import Util.ColorPalette;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class LogoPanel extends JPanel {

    public interface LogoListener {
        void onClickLogo();
    }

    private LogoListener listener;

    public void setListener(LogoListener listener) {
        this.listener = listener;
    }

    RoundedButton buttonTimer = new RoundedButton("", 15);

    public LogoPanel() {
        setupUI();

        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            revalidate();
            repaint();
        });
    }

    private void setupUI() {
        this.setBackground(ColorPalette.getInstance().getBgSecondary());
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        createComponents();
    }

    private void createComponents() {

        this.add(Box.createVerticalStrut(15));

        // button Timer
        buttonTimer.setEnabled(false);
        buttonTimer.add(new LiveClockSidebar());
        buttonTimer.setAlignmentX(CENTER_ALIGNMENT);
        buttonTimer.setPreferredSize(new Dimension(200, 40));
        buttonTimer.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.add(buttonTimer);

        this.add(Box.createVerticalStrut(5));
        // importing and scaling icon
        ImageIcon logoIcon = new ImageIcon("icons/SPOMAP_Default_White color.png");
        Image scaledIcon = logoIcon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(scaledIcon);
        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(logoIcon);
        iconLabel.setAlignmentX(CENTER_ALIGNMENT);

        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (listener != null)
                    listener.onClickLogo();
                // Redirect to Home page
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        this.add(iconLabel);
    }
}
