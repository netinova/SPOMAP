package Components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.ComboPopup;

import Util.ColorPalette;
import Util.UIUtils;

public class RoundedScrollableComboBox<T> extends JComboBox<T> {

    private boolean mouseOver;
    private boolean mousePressed;
    private int maxVisibleItems = 5;

    public RoundedScrollableComboBox(T[] items) {
        super(items);
        this.setBackground(ColorPalette.getInstance().getBgTertiary());
        this.setForeground(ColorPalette.getInstance().getTextPrimary());
        this.setPreferredSize(new Dimension(130, 30));
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.setFocusable(false);
        this.setOpaque(false);
        this.setBorder(new EmptyBorder(5, 8, 5, 8));
        this.setUI(new ScrollableRoundedComboUI());

        this.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                label.setForeground(ColorPalette.getInstance().getTextPrimary());
                label.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

                if (isSelected) {
                    label.setBackground(null);
                    label.setOpaque(false);
                    label = new JLabel(value.toString()) {
                        @Override
                        protected void paintComponent(Graphics g) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);

                            g2.setColor(ColorPalette.getInstance().getButtonHover());
                            int arc = 15;
                            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                            g2.dispose();
                            super.paintComponent(g);
                        }
                    };
                    label.setForeground(ColorPalette.getInstance().getTextPrimary());
                    label.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                    label.setOpaque(false);
                } else {
                    label.setBackground(null);
                    label.setOpaque(false);
                }
                return label;
            }
        });

        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            this.setBackground(ColorPalette.getInstance().getBgTertiary());
            this.setForeground(ColorPalette.getInstance().getTextPrimary());
            revalidate();
            repaint();
        });
    }

    public void setMaxVisibleItems(int max) {
        this.maxVisibleItems = max;
    }

    public int getMaxVisibleItems() {
        return maxVisibleItems;
    }

    private class ArrowButton extends JButton {
        private int cornerRadius;

        public ArrowButton(int cornerRadius) {
            super("\u25BC");
            this.cornerRadius = cornerRadius;
            this.setContentAreaFilled(false);
            this.setFocusPainted(false);
            this.setFont(this.getFont().deriveFont(10f));
            this.setBorder(new EmptyBorder(10, 20, 10, 20));
            this.setPreferredSize(new Dimension(20, 20));
            this.setForeground(ColorPalette.getInstance().getTextPrimary());
            this.setBackground(ColorPalette.getInstance().getBgTertiary());
            this.setBorder(null);

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    mouseOver = true;
                    RoundedScrollableComboBox.this.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    mouseOver = false;
                    RoundedScrollableComboBox.this.repaint();
                }

                @Override
                public void mousePressed(MouseEvent evt) {
                    mousePressed = true;
                    RoundedScrollableComboBox.this.repaint();
                }

                @Override
                public void mouseReleased(MouseEvent evt) {
                    mousePressed = false;
                    RoundedScrollableComboBox.this.repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (mousePressed) {
                g2.setColor(ColorPalette.getInstance().getButtonPressed());
            } else if (mouseOver) {
                g2.setColor(ColorPalette.getInstance().getButtonHover());
            } else {
                g2.setColor(ColorPalette.getInstance().getBgTertiary());
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

            g2.dispose();

            super.paintComponent(g);
        }
    }

    private class ScrollableRoundedComboUI extends BasicComboBoxUI {

        @Override
        protected JButton createArrowButton() {
            return new ArrowButton(20);
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup popup = new BasicComboPopup(comboBox) {
                private final int popupArc = 20;
                private final int gap = 8;

                @Override
                public void setBorder(Border border) {
                    super.setBorder(null);
                }

                @Override
                protected JScrollPane createScroller() {
                    JScrollPane scroller = super.createScroller();
                    scroller.setBorder(null);
                    scroller.setViewportBorder(null);
                    scroller.setOpaque(false);
                    scroller.getViewport().setOpaque(false);
                    scroller.getViewport().setBackground(ColorPalette.getInstance().getBgTertiary());

                    scroller.getVerticalScrollBar().setUnitIncrement(16);

                    styleScrollBar(scroller.getVerticalScrollBar());

                    return scroller;
                }

                @Override
                protected JList<Object> createList() {
                    JList<Object> list = super.createList();
                    list.setOpaque(false);
                    list.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                    list.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    return list;
                }

                @Override
                protected Rectangle computePopupBounds(int px, int py, int pw, int ph) {
                    int itemCount = RoundedScrollableComboBox.this.getItemCount();
                    int visibleItems = Math.min(itemCount, maxVisibleItems);
                    int prefHeight = visibleItems * 26 + 10;
                    return new Rectangle(px, py, pw, prefHeight);
                }

                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    int w = getWidth();
                    int h = getHeight();

                    g2.setColor(ColorPalette.getInstance().getBgTertiary());
                    g2.fillRoundRect(0, 0, w, h, popupArc, popupArc);

                    g2.setColor(ColorPalette.getInstance().getBorder());
                    g2.drawRoundRect(0, 0, w - 1, h - 1, popupArc, popupArc);

                    g2.dispose();

                    super.paintChildren(g);
                }

                @Override
                public void show() {
                    super.show();
                    Point p = getLocationOnScreen();
                    setLocation(p.x, p.y + gap);
                }
            };
            popup.setOpaque(false);
            popup.setBorder(null);
            return popup;
        }

        @Override
        protected void installListeners() {
            super.installListeners();
            comboBox.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    mouseOver = true;
                    comboBox.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    mouseOver = false;
                    comboBox.repaint();
                }

                @Override
                public void mousePressed(MouseEvent evt) {
                    mousePressed = true;
                    comboBox.repaint();
                }

                @Override
                public void mouseReleased(MouseEvent evt) {
                    mousePressed = false;
                    comboBox.repaint();
                }
            });
        }

        @Override
        public void paint(Graphics g, JComponent c) {

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = c.getWidth();
            int h = c.getHeight();
            int arc = 20;

            if (mousePressed) {
                g2.setColor(ColorPalette.getInstance().getButtonPressed());
            } else if (mouseOver) {
                g2.setColor(ColorPalette.getInstance().getButtonHover());
            } else {
                g2.setColor(ColorPalette.getInstance().getBgTertiary());
            }

            g2.fillRoundRect(0, 0, w, h, arc, arc);

            g2.setColor(ColorPalette.getInstance().getBorder());
            g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);

            g2.dispose();

            super.paint(g, c);
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            Color t = g.getColor();
            g.setColor(t);
        }
    }

    public static void styleScrollBar(JScrollBar bar) {

        bar.setOpaque(false);
        bar.setBackground(new Color(0, 0, 0, 0));

        bar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.trackColor = new Color(0, 0, 0, 0);
                this.thumbColor = ColorPalette.getInstance().getAccentPrimary();
            }

            @Override
            protected void layoutVScrollbar(JScrollBar sb) {
                super.layoutVScrollbar(sb);
                int inset = 2;
                Rectangle track = getTrackBounds();
                track.y += inset;
                track.height -= (inset + inset);
                this.trackRect = track;
            }

            @Override
            protected Rectangle getThumbBounds() {
                Rectangle thumbRect = super.getThumbBounds();

                int inset = 2;
                int minY = trackRect.y + inset;
                int maxY = trackRect.y + trackRect.height - thumbRect.height - inset;

                if (thumbRect.y < minY) {
                    thumbRect.y = minY;
                }
                if (thumbRect.y > maxY) {
                    thumbRect.y = maxY;
                }

                return thumbRect;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (thumbBounds.isEmpty() || !scrollbar.isEnabled())
                    return;

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = 8;
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width - 1, thumbBounds.height - 1, arc, arc);

                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int inset = 2;
                int w = trackBounds.width - inset;
                if (w < 4)
                    w = 4;
                Rectangle r = new Rectangle(trackBounds.x, trackBounds.y, w, trackBounds.height);

                int arc = 10;
                g2.setColor(trackColor);
                g2.fillRoundRect(r.x, r.y, r.width, r.height, arc, arc);
                g2.dispose();
            }
        });
        bar.setPreferredSize(new Dimension(8, 0));
        bar.setUnitIncrement(16);
    }
}
