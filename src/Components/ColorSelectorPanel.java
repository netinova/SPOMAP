package Components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Model.ProductColor;
import Util.ColorPalette;

public class ColorSelectorPanel extends JPanel {

    private static final int CIRCLE_DIAMETER = 35;
    private static final int CIRCLE_PADDING = 10;
    private static final Color BORDER_UNSELECTED = ColorPalette.BORDER;
    private static final Color BORDER_SELECTED = ColorPalette.ACCENT_PRIMARY;

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
            c.setSelected(c.productColor == color);
        }
        for (ColorSelectionListener l : listeners) {
            l.colorSelected(color);
        }
    }

    public interface ColorSelectionListener {
        void colorSelected(ProductColor color);
    }

    private static class ColorCircle extends JComponent {
        private ProductColor productColor;
        private boolean selected;

        public ColorCircle(ProductColor pc) {
            this.productColor = pc;
            this.selected = false;
            setPreferredSize(new Dimension(CIRCLE_DIAMETER, CIRCLE_DIAMETER));
            setMaximumSize(getPreferredSize());
            setMinimumSize(getPreferredSize());
            setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Fill with product color
            g2.setColor(productColor.getProductColor());
            g2.fillOval(0, 0, w - 1, h - 1);

            // Border
            if (selected) {
                g2.setColor(BORDER_SELECTED);
                g2.setStroke(new BasicStroke(1.5f));
            } else {
                g2.setColor(BORDER_UNSELECTED);
                g2.setStroke(new BasicStroke(1.5f));
            }
            g2.drawOval(0, 0, w - 1, h - 1);

            g2.dispose();
        }
    }
}
