package Components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import Model.ProductColor;
import Util.ColorPalette;

public class ColorSelectorPanel extends JPanel {

    private static final int CIRCLE_DIAMETER = 35;
    private static final int LABEL_HEIGHT = 18;
    private static final int ITEM_WIDTH = 35;
    private static final int ITEM_HEIGHT = CIRCLE_DIAMETER + LABEL_HEIGHT + 6;

    private static final int CIRCLE_PADDING = 10;
    private static final Color BORDER_UNSELECTED = ColorPalette.BORDER;
    private static final Color BORDER_SELECTED = ColorPalette.ACCENT_PRIMARY;
    private static final int LABEL_VISIBLE_MS = 2500;

    private List<ColorCircle> circles = new ArrayList<>();
    private ProductColor selectedColor;
    private List<ColorSelectionListener> listeners = new ArrayList<>();

    public ColorSelectorPanel() {
        setBackground(ColorPalette.BG_MAIN);
        setLayout(new FlowLayout(FlowLayout.LEFT, CIRCLE_PADDING, 0));
        setBorder(new EmptyBorder(20, 15, 20, 15));
    }

    public void setColors(ProductColor[] colors) {
        removeAll();
        circles.clear();
        selectedColor = null;

        if (colors != null && colors.length > 0) {
            for (ProductColor pc : colors) {
                ColorCircle circle = new ColorCircle(pc);
                circle.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        selectColor(pc);
                    }
                });
                circles.add(circle);
                add(circle);
            }
            selectColor(colors[0]);
        }

        revalidate();
        repaint();
    }

    public ProductColor getSelectedColor() {
        return selectedColor;
    }

    public void addColorSelectionListener(ColorSelectionListener listener) {
        listeners.add(listener);
    }

    private void selectColor(ProductColor color) {
        if (color == selectedColor)
            return;

        selectedColor = color;

        for (ColorCircle c : circles) {

            boolean isSelected = c.productColor == color;

            c.setSelected(isSelected);

            if (isSelected) {
                c.showLabelTemporarily(LABEL_VISIBLE_MS);
            } else {
                c.fadeOutImmediately();
            }
        }

        for (ColorSelectionListener l : listeners) {
            l.colorSelected(color);
        }
    }

    public interface ColorSelectionListener {
        void colorSelected(ProductColor color);
    }

    private static class ColorCircle extends JComponent {
        private final ProductColor productColor;
        private boolean selected;
        private float labelAlpha = 0f;
        private Timer animationTimer;

        public ColorCircle(ProductColor pc) {
            this.productColor = pc;
            this.selected = false;

            setPreferredSize(new Dimension(ITEM_WIDTH, ITEM_HEIGHT));
            setMaximumSize(getPreferredSize());
            setMinimumSize(getPreferredSize());
            setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            setToolTipText(getColorName());
        }

        public void fadeOutImmediately() {

            if (animationTimer != null) {
                animationTimer.stop();
            }

            final float startAlpha = labelAlpha;
            final long startTime = System.currentTimeMillis();
            final int duration = 100;

            animationTimer = new Timer(15, e -> {

                long elapsed = System.currentTimeMillis() - startTime;

                if (elapsed >= duration) {
                    labelAlpha = 0f;
                    ((Timer) e.getSource()).stop();
                } else {
                    float t = elapsed / (float) duration;
                    labelAlpha = startAlpha * (1f - t);
                }

                repaint();
            });

            animationTimer.start();
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            repaint();
        }

        public void showLabelTemporarily(int totalDurationMs) {

            if (animationTimer != null && animationTimer.isRunning()) {
                animationTimer.stop();
            }

            final int fadeInMs = 200;
            final int visibleMs = 1800;
            final int fadeOutMs = 500;

            final long startTime = System.currentTimeMillis();

            animationTimer = new Timer(30, e -> {

                long elapsed = System.currentTimeMillis() - startTime;

                if (elapsed < fadeInMs) {
                    // Fade in
                    labelAlpha = elapsed / (float) fadeInMs;
                } else if (elapsed < fadeInMs + visibleMs) {
                    // Fully visible
                    labelAlpha = 1f;
                } else if (elapsed < fadeInMs + visibleMs + fadeOutMs) {
                    // Fade out
                    float t = (elapsed - fadeInMs - visibleMs) / (float) fadeOutMs;
                    labelAlpha = 1f - t;
                } else {
                    labelAlpha = 0f;
                    ((Timer) e.getSource()).stop();
                }

                repaint();
            });

            animationTimer.start();
        }

        private String getColorName() {
            return productColor.name();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int circleX = (getWidth() - CIRCLE_DIAMETER) / 2;
            int circleY = 0;

            g2.setColor(productColor.getProductColor());
            g2.fillOval(circleX, circleY, CIRCLE_DIAMETER - 1, CIRCLE_DIAMETER - 1);

            // Border
            g2.setColor(selected ? BORDER_SELECTED : BORDER_UNSELECTED);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawOval(circleX, circleY, CIRCLE_DIAMETER - 1, CIRCLE_DIAMETER - 1);

            // Label under circle
            if (labelAlpha > 0.01f) {

                String text = getColorName();

                g2.setComposite(
                        java.awt.AlphaComposite.getInstance(
                                java.awt.AlphaComposite.SRC_OVER,
                                labelAlpha));

                g2.setColor(ColorPalette.TEXT_PRIMARY);

                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(text);
                int textX = (getWidth() - textWidth) / 2;

                int offsetY = (int) ((1f - labelAlpha) * 6);
                int textY = CIRCLE_DIAMETER + fm.getAscent() + 4 - offsetY;

                g2.drawString(text, textX, textY);
            }

            g2.dispose();
        }
    }
}
