package Components;

import Util.ColorPalette;
import Util.UIUtils;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

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

        this.add(Box.createVerticalStrut(15));

        // importing and scaling icon
        File svgFile = new File("icons/logo_svg/SPOMAP_BG_White.svg");
        FlatSVGIcon logoIcon = new FlatSVGIcon(svgFile).derive(75, 75);
        if (!logoIcon.hasFound()) {
            System.err.println("SVG not found: " + svgFile.getAbsolutePath());
        }
        logoIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> ColorPalette.getInstance().getAccentPrimary()));

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
