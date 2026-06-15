package Components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
    private ColorCircle selectedCircle;
    private List<ColorSelectionListener> listeners = new ArrayList<>();

    public interface ColorSelectionListener {
        void colorSelected(ProductColor color);
    }

    public void addColorSelectionListener(ColorSelectionListener listener) {
        listeners.add(listener);
    }

    public ColorSelectorPanel() {
        setBackground(ColorPalette.BG_MAIN);
        setLayout(new FlowLayout(FlowLayout.LEFT, CIRCLE_PADDING, 0));
        setBorder(new EmptyBorder(20, 15, 20, 15));
        setOpaque(true);
    }

    public void setColors(ProductColor[] colors) {
        removeAll();
        circles.clear();
        selectedColor = null;
        selectedCircle = null;

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

    private void selectColor(ProductColor color) {
        if (color == selectedColor)
            return;

        selectedColor = color;
        selectedCircle = null;

        for (ColorCircle c : circles) {
            boolean isSelected = c.productColor == color;
            c.setSelected(isSelected);

            if (isSelected) {
                selectedCircle = c;
                c.showLabelTemporarily(LABEL_VISIBLE_MS);
            } else {
                c.fadeOutImmediately();
            }
        }

        repaint();

        for (ColorSelectionListener l : listeners) {
            l.colorSelected(color);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (selectedCircle != null && selectedCircle.labelAlpha > 0.01f) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Rectangle b = selectedCircle.getBounds();

            String text = selectedCircle.getColorName();
            g2.setComposite(java.awt.AlphaComposite.getInstance(
                    java.awt.AlphaComposite.SRC_OVER,
                    selectedCircle.labelAlpha));

            g2.setColor(ColorPalette.TEXT_PRIMARY);

            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(text);

            // Center label under the circle, but allow it to extend beyond the circle width
            int textX = b.x + (CIRCLE_DIAMETER / 2) - (textWidth / 2);
            int textY = b.y + CIRCLE_DIAMETER + fm.getAscent() + 4;

            g2.drawString(text, textX, textY);
            g2.dispose();
        }
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
            setOpaque(false);
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
                if (getParent() != null) {
                    getParent().repaint();
                }
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
                    labelAlpha = elapsed / (float) fadeInMs;
                } else if (elapsed < fadeInMs + visibleMs) {
                    labelAlpha = 1f;
                } else if (elapsed < fadeInMs + visibleMs + fadeOutMs) {
                    float t = (elapsed - fadeInMs - visibleMs) / (float) fadeOutMs;
                    labelAlpha = 1f - t;
                } else {
                    labelAlpha = 0f;
                    ((Timer) e.getSource()).stop();
                }

                repaint();
                if (getParent() != null) {
                    getParent().repaint();
                }
            });

            animationTimer.start();
        }

        public String getColorName() {
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

            g2.setColor(selected ? BORDER_SELECTED : BORDER_UNSELECTED);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawOval(circleX, circleY, CIRCLE_DIAMETER - 1, CIRCLE_DIAMETER - 1);

            g2.dispose();
        }
    }
}
