package Components;

import Util.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

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

        Image image = Toolkit.getDefaultToolkit().createImage("icons/SPOMAP_BG_White.png");
        MediaTracker tracker = new MediaTracker(new JPanel());
        tracker.addImage(image, 0);
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ImageIcon imageIcon = new ImageIcon(image);

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
