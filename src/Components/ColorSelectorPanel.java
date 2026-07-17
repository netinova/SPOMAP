package Components;

import Model.ProductColor;
import Util.ColorPalette;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ColorSelectorPanel extends JPanel {

    public enum SelectionMode {
        SINGLE,
        MULTI
    }

    private SelectionMode selectionMode = SelectionMode.SINGLE;

    private final Set<ProductColor> selectedColors = new LinkedHashSet<>();
    private final List<ColorCircle> circles = new ArrayList<>();
    private final List<ColorSelectionListener> listeners = new ArrayList<>();

    private static final int CIRCLE_DIAMETER = 35;
    private static final int LABEL_HEIGHT = 18;
    private static final int ITEM_WIDTH = 75;
    private static final int ITEM_HEIGHT = CIRCLE_DIAMETER + LABEL_HEIGHT + 6;

    private static final int CIRCLE_PADDING = 10;
    private static final Color BORDER_UNSELECTED = ColorPalette.getInstance().getBorder();
    private static final Color BORDER_SELECTED = ColorPalette.getInstance().getAccentPrimary();
    private static final int LABEL_VISIBLE_MS = 2500;

    public interface ColorSelectionListener {
        void colorSelectionChanged(Set<ProductColor> selectedColors);
    }

    public ColorSelectorPanel() {
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
        setLayout(new WrapLayout(FlowLayout.LEFT, CIRCLE_PADDING, 6));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setOpaque(false);
    }

    public void setSelectionMode(SelectionMode mode) {
        this.selectionMode = mode;
        if (mode == SelectionMode.SINGLE && selectedColors.size() > 1) {
            // Keep only the first selected color
            ProductColor first = selectedColors.iterator().next();
            selectedColors.clear();
            selectedColors.add(first);
            updateCircles();
            notifyListeners();
        }
    }

    public void setColors(ProductColor[] colors) {
        removeAll();
        circles.clear();
        selectedColors.clear();

        if (colors != null && colors.length > 0) {
            for (ProductColor pc : colors) {
                ColorCircle circle = new ColorCircle(pc);
                circle.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        toggleSelection(pc);
                    }
                });
                circles.add(circle);
                add(circle);
            }
        }

        if (selectionMode == SelectionMode.SINGLE)
            setSelectedColors(Objects.requireNonNull(colors)[0]);

        revalidate();
        repaint();
    }

    private void toggleSelection(ProductColor color) {
        if (selectionMode == SelectionMode.SINGLE) {
            if (selectedColors.size() == 1 && selectedColors.contains(color)) {
                return;
            }
            selectedColors.clear();
            selectedColors.add(color);
        } else { // multi
            if (selectedColors.contains(color)) {
                selectedColors.remove(color);
            } else {
                selectedColors.add(color);
            }
        }

        updateCircles();
        notifyListeners();
    }

    private void updateCircles() {
        for (ColorCircle circle : circles) {
            boolean isSelected = selectedColors.contains(circle.productColor);
            circle.setSelected(isSelected);
            if (isSelected) {
                circle.showLabelTemporarily(LABEL_VISIBLE_MS);
            } else {
                circle.fadeOutImmediately();
            }
        }
        repaint();
    }

    private void notifyListeners() {
        Set<ProductColor> setColor = new LinkedHashSet<>(selectedColors);
        for (ColorSelectionListener l : listeners) {
            l.colorSelectionChanged(setColor);
        }
    }

    public void addColorSelectionListener(ColorSelectionListener listener) {
        listeners.add(listener);
    }

    public void removeColorSelectionListener(ColorSelectionListener listener) {
        listeners.remove(listener);
    }

    public ProductColor[] getSelectedColors() {
        return selectedColors.toArray(new ProductColor[0]);
    }

    public void clearSelection() {
        selectedColors.clear();
        updateCircles();
        notifyListeners();
    }

    public void setSelectedColors(ProductColor... colors) {
        selectedColors.clear();
        if (selectionMode == SelectionMode.SINGLE && colors.length > 0) {
            selectedColors.add(colors[0]);
        } else {
            for (ProductColor c : colors) {
                selectedColors.add(c);
            }
        }
        updateCircles();
        notifyListeners();
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
            setCursor(new Cursor(Cursor.HAND_CURSOR));
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
            });
            animationTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int circleX = (getWidth() - CIRCLE_DIAMETER) / 2;
            int circleY = 2;

            // Draw circle
            g2.setColor(productColor.getProductColor());
            g2.fillOval(circleX, circleY, CIRCLE_DIAMETER - 1, CIRCLE_DIAMETER - 1);

            // Border
            g2.setColor(selected ? BORDER_SELECTED : BORDER_UNSELECTED);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawOval(circleX, circleY, CIRCLE_DIAMETER - 1, CIRCLE_DIAMETER - 1);

            // Label under circle (fading)
            if (labelAlpha > 0.01f) {
                String text = productColor.name();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, labelAlpha));
                g2.setColor(ColorPalette.getInstance().getTextPrimary());
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(text);
                int textX = (getWidth() - textWidth) / 2;
                int textY = CIRCLE_DIAMETER + fm.getAscent() + 4;
                g2.drawString(text, textX, textY);
            }

            g2.dispose();
        }
    }
}
