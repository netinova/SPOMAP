package Components;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Util.ColorPalette;

public class ImageGallery extends JPanel {

    private static final int PREFERRED_HEIGHT = 350;
    private static final Dimension ARROW_SIZE = new Dimension(40, 40);

    private List<String> imagePaths;
    private int currentIndex = 0;

    private JPanel imagePanel;
    private JLabel imageLabel;
    private JButton leftArrow;
    private JButton rightArrow;
    private JPanel dotPanel;
    private JLabel[] dots;

    public ImageGallery() {
        imagePaths = new ArrayList<String>();
        setupUI();
        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            revalidate();
            repaint();
        });
    }

    private void setupUI() {
        removeAll();
        setBackground(ColorPalette.getInstance().getBgMain());
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, PREFERRED_HEIGHT));

        imagePanel = new JPanel(null);
        imagePanel.setBackground(ColorPalette.getInstance().getBgSecondary());
        imagePanel.setPreferredSize(new Dimension(0, PREFERRED_HEIGHT));
        imagePanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repositionArrows();
            }
        });

        imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setBounds(0, 0, 0, 0);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        leftArrow = createArrowButton("");
        rightArrow = createArrowButton("");

        ImageIcon leftArrowIcon = loadAndScaleImage("icons/left_arrow.png", 20, 20);
        ImageIcon rightArrowIcon = loadAndScaleImage("icons/right_arrow.png", 20, 20);

        leftArrow.setIcon(leftArrowIcon);
        rightArrow.setIcon(rightArrowIcon);

        leftArrow.addActionListener(e -> previousImage());
        rightArrow.addActionListener(e -> nextImage());

        imagePanel.add(leftArrow);
        imagePanel.add(rightArrow);

        imagePanel.setComponentZOrder(leftArrow, 0);
        imagePanel.setComponentZOrder(rightArrow, 0);

        add(imagePanel, BorderLayout.CENTER);

        dotPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        dotPanel.setBackground(ColorPalette.getInstance().getBgSecondary());
        add(dotPanel, BorderLayout.SOUTH);

        if (!imagePaths.isEmpty()) {
            updateDisplay();
        }
    }

    private JButton createArrowButton(String text) {
        RoundedButton btn = new RoundedButton(text, 50);
        btn.setFont(btn.getFont().deriveFont(16f));
        btn.setForeground(ColorPalette.getInstance().getTextPrimary());
        btn.setBackground(ColorPalette.getInstance().getButtonNormal());
        btn.setFocusable(false);
        btn.setPreferredSize(ARROW_SIZE);
        btn.setSize(ARROW_SIZE);
        return btn;
    }

    private void repositionArrows() {
        int width = imagePanel.getWidth();
        int height = imagePanel.getHeight();
        if (width == 0 || height == 0)
            return;

        imageLabel.setBounds(0, 0, width, height);

        int arrowY = (height - ARROW_SIZE.height) / 2;
        leftArrow.setLocation(10, arrowY);
        rightArrow.setLocation(width - ARROW_SIZE.width - 10, arrowY);
    }

    public void setImages(String[] paths) {
        imagePaths = (paths != null) ? Arrays.asList(paths) : new ArrayList<String>();
        currentIndex = 0;
        updateDisplay();
    }

    public void nextImage() {
        if (imagePaths.isEmpty())
            return;
        currentIndex = (currentIndex + 1) % imagePaths.size();
        updateDisplay();
    }

    public void previousImage() {
        if (imagePaths.isEmpty())
            return;
        currentIndex = (currentIndex - 1 + imagePaths.size()) % imagePaths.size();
        updateDisplay();
    }

    public void setCurrentIndex(int index) {
        if (imagePaths.isEmpty())
            return;
        currentIndex = Math.max(0, Math.min(index, imagePaths.size() - 1));
        updateDisplay();
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    private void updateDisplay() {
        if (imagePaths.isEmpty()) {
            imageLabel.setIcon(null);
            imageLabel.setText("No image available");
            imageLabel.setForeground(ColorPalette.getInstance().getTextMuted());
            buildDots(0);
            return;
        }

        ImageIcon icon = loadAndScaleImage(imagePaths.get(currentIndex),
                imagePanel.getWidth(), imagePanel.getHeight());
        imageLabel.setIcon(icon);
        imageLabel.setText(null);

        buildDots(imagePaths.size());
        highlightDot(currentIndex);
        repositionArrows();
    }

    private void buildDots(int count) {
        dotPanel.removeAll();
        dots = new JLabel[count];
        for (int i = 0; i < count; i++) {
            JLabel dot = new JLabel("●"); // Unicode filled circle
            dot.setFont(dot.getFont().deriveFont(16f)); // adjust size
            dot.setForeground(ColorPalette.getInstance().getTextMuted());
            dot.setCursor(new Cursor(Cursor.HAND_CURSOR));
            final int index = i;
            dot.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    setCurrentIndex(index);
                }
            });
            dotPanel.add(dot);
            dots[i] = dot;
        }
        // dotPanel.revalidate();
        // dotPanel.repaint();
    }

    private void highlightDot(int idx) {
        for (int i = 0; i < dots.length; i++) {
            dots[i].setForeground(i == idx ? ColorPalette.getInstance().getAccentPrimary()
                    : ColorPalette.getInstance().getTextMuted());
        }
    }

    private ImageIcon loadAndScaleImage(String path, int maxWidth, int maxHeight) {
        ImageIcon originalIcon = new ImageIcon(path);
        Image originalImage = originalIcon.getImage();

        int originalWidth = originalImage.getWidth(null);
        int originalHeight = originalImage.getHeight(null);

        // Calculate scaling factor to fit within max dimensions while preserving aspect
        // ratio
        double widthRatio = (double) maxWidth / originalWidth;
        double heightRatio = (double) maxHeight / originalHeight;
        double scaleFactor = Math.min(widthRatio, heightRatio);

        int scaledWidth = (int) (originalWidth * scaleFactor);
        int scaledHeight = (int) (originalHeight * scaleFactor);

        Image scaledIcon = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledIcon);
    }
}
