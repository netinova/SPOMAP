package Components;

import Util.ColorPalette;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.File;

public class SplashScreen extends JWindow {

    private JLabel statusLabel;
    private JProgressBar progressBar;

    public SplashScreen() {
        setupUI();

        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            revalidate();
            repaint();
        });
    }

    private void setupUI() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(ColorPalette.getInstance().getBgMain());
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        File svgFile = new File("icons/logo_svg/SPOMAP_BG_White.svg");
        FlatSVGIcon imageIcon = new FlatSVGIcon(svgFile).derive(300, 300);
        if (!imageIcon.hasFound()) {
            System.err.println("SVG not found: " + svgFile.getAbsolutePath());
        }
        imageIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> ColorPalette.getInstance().getAccentPrimary()));

        JLabel gifLabel = new JLabel(imageIcon);
        gifLabel.setHorizontalAlignment(SwingConstants.CENTER);
        content.add(gifLabel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        statusLabel = new JLabel("Starting...", SwingConstants.CENTER);
        statusLabel.setForeground(ColorPalette.getInstance().getTextPrimary());
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(statusLabel);

        bottomPanel.add(Box.createVerticalStrut(8));

        progressBar = new RoundedProgressBar(20);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressBar.setMaximumSize(new Dimension(imageIcon.getIconWidth(), 20));
        bottomPanel.add(progressBar);
        bottomPanel.add(Box.createVerticalStrut(30));

        content.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(content);
        int gifW = imageIcon.getIconWidth();
        int gifH = imageIcon.getIconHeight();
        setSize(gifW + 100, gifH + 180);
        setLocationRelativeTo(null);
    }

    public void setStatus(String text) {
        statusLabel.setText(text);
    }

    public void setProgress(int percent) {
        progressBar.setValue(percent);
    }
}
