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
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

import Util.ColorPalette;

public class RoundedComboBox<T> extends JComboBox<T> {

    private boolean mouseOver;
    private boolean mousePressed;

    public RoundedComboBox(T[] items) {
        super(items);
        this.setBackground(ColorPalette.BG_TERTIARY);
        this.setForeground(ColorPalette.TEXT_PRIMARY);
        this.setPreferredSize(new Dimension(130, 30));
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.setFocusable(false);
        this.setOpaque(false);
        this.setBorder(new EmptyBorder(5, 8, 5, 8));
        this.setUI(new RoundedComboUI());

        this.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                label.setForeground(ColorPalette.TEXT_PRIMARY);
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

                            g2.setColor(ColorPalette.BUTTON_HOVER);
                            int arc = 15;
                            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                            g2.dispose();
                            super.paintComponent(g);
                        }
                    };
                    label.setForeground(ColorPalette.TEXT_PRIMARY);
                    label.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                    label.setOpaque(false);
                } else {
                    label.setBackground(null);
                    label.setOpaque(false);
                }
                return label;
            }
        });

    }

    private class ArrowButton extends JButton {
        private int cornerRadius;

        public ArrowButton(int cornerRadius) {
            super("\u25BC");
            this.cornerRadius = cornerRadius;
            this.setContentAreaFilled(false); // Make background transparent
            this.setFocusPainted(false);
            this.setFont(this.getFont().deriveFont(10f));
            this.setBorder(new EmptyBorder(10, 20, 10, 20));
            this.setPreferredSize(new Dimension(20, 20));
            this.setForeground(ColorPalette.TEXT_PRIMARY);
            this.setBackground(ColorPalette.BG_TERTIARY);
            this.setBorder(null);

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    mouseOver = true;
                    RoundedComboBox.this.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    mouseOver = false;
                    RoundedComboBox.this.repaint();
                }

                @Override
                public void mousePressed(MouseEvent evt) {
                    mousePressed = true;
                    RoundedComboBox.this.repaint();
                }

                @Override
                public void mouseReleased(MouseEvent evt) {
                    mousePressed = false;
                    RoundedComboBox.this.repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw rounded background
            if (mousePressed) {
                g2.setColor(ColorPalette.BUTTON_PRESSED);
            } else if (mouseOver) {
                g2.setColor(ColorPalette.BUTTON_HOVER);
            } else {
                g2.setColor(getBackground());
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

            g2.dispose();

            // Paint the text
            super.paintComponent(g);
        }
    }

    private class RoundedComboUI extends BasicComboBoxUI {

        @Override
        protected JButton createArrowButton() {
            return new ArrowButton(20); // ▼
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup popup = new BasicComboPopup(comboBox) {
                // Roundness of the popup corners
                private final int popupArc = 20;
                private final int gap = 8;

                @Override
                public void setBorder(Border border) {
                    super.setBorder(null); // reject every external border
                }

                @Override
                protected JScrollPane createScroller() {
                    JScrollPane scroller = super.createScroller();
                    scroller.setBorder(null);
                    scroller.setViewportBorder(null);
                    scroller.setOpaque(false);
                    scroller.getViewport().setOpaque(false);
                    scroller.getViewport().setBackground(ColorPalette.BG_TERTIARY);
                    return scroller;
                }

                @Override
                protected JList<Object> createList() {
                    JList<Object> list = super.createList();
                    list.setOpaque(false);
                    list.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                    return list;
                }

                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    int w = getWidth();
                    int h = getHeight();

                    // Fill background
                    g2.setColor(ColorPalette.BG_TERTIARY);
                    g2.fillRoundRect(0, 0, w, h, popupArc, popupArc);

                    // Draw border
                    g2.setColor(ColorPalette.BORDER);
                    g2.drawRoundRect(0, 0, w - 1, h - 1, popupArc, popupArc);

                    g2.dispose();

                    // Paint the children (JScrollPane + JList) on top
                    super.paintChildren(g);
                }

                // Add gap below the combo box
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

            // Paint background & border manually to achieve rounded rectangle
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = c.getWidth();
            int h = c.getHeight();
            int arc = 20; // corner radius

            if (mousePressed) {
                g2.setColor(ColorPalette.BUTTON_PRESSED);
            } else if (mouseOver) {
                g2.setColor(ColorPalette.BUTTON_HOVER);
            } else {
                g2.setColor(ColorPalette.BG_TERTIARY);
            }

            g2.fillRoundRect(0, 0, w, h, arc, arc);

            // Border
            g2.setColor(ColorPalette.BORDER);
            g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);

            g2.dispose();

            super.paint(g, c);
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            Color t = g.getColor();

            if (mousePressed) {
                g.setColor(ColorPalette.BUTTON_PRESSED);
            } else if (mouseOver) {
                g.setColor(ColorPalette.BUTTON_HOVER);
            } else {
                g.setColor(ColorPalette.BG_TERTIARY);
            }

            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            g.setColor(t);
        }

        @Override
        public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            Object selected = comboBox.getSelectedItem();
            if (selected != null) {
                String text = selected.toString();
                g2.setFont(comboBox.getFont());
                g2.setColor(ColorPalette.TEXT_PRIMARY);

                FontMetrics fm = g2.getFontMetrics();
                int y = bounds.y + ((bounds.height - fm.getHeight()) / 2) + fm.getAscent();
                int x = bounds.x + 5;

                g2.drawString(text, x, y);
            }

            g2.dispose();
        }
    }

}
