package View;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

import Components.RoundedButton;
import Controller.InvoiceController;
import Model.AppState;
import Model.Invoice;
import Model.User;
import Model.UserType;
import Util.ColorPalette;

public class InvoiceDetailView extends JPanel {

    private InvoiceController controller;
    private Invoice selectedInvoice;

    private JLabel titleLabel;
    private JLabel pdfPreviewLabel;
    private JScrollPane previewScrollPane;

    private RoundedButton savePdfButton;
    private RoundedButton refundButton;

    public InvoiceDetailView(InvoiceController controller) {
        this.controller = controller;

        // In InvoiceDetailView constructor or setupUI, after creating refundButton
        AppState.getInstance().addListener(evt -> {
            if (AppState.PROP_USER.equals(evt.getPropertyName())) {
                updateRefundButtonState();
            }
        });

        setupUI();
    }

    public void updateRefundButtonState() {
        User currentUser = AppState.getInstance().getLoggedInUser();
        boolean isPrime = currentUser != null && currentUser.getUserType() == UserType.PRIME;
        refundButton.setVisible(isPrime);
        if (selectedInvoice!=null)
            refundButton.setEnabled(controller.getStatusInvoice(selectedInvoice));
    }

    public void setInvoice(Invoice invoice) {
        this.selectedInvoice = invoice;
        if (titleLabel != null) {
            titleLabel.setText("Invoice #" + invoice.getInvoiceId());
        }
        controller.generatePdfPreviewImage(invoice);
        updateRefundButtonState();
    }

    public void setPreviewImage(BufferedImage image) {
        if (image == null) {
            pdfPreviewLabel.setIcon(null);
            return;
        }

        int previewWidth = 600;
        double ratio = (double) previewWidth / image.getWidth();
        int previewHeight = (int) (image.getHeight() * ratio);

        BufferedImage scaled = new BufferedImage(previewWidth, previewHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = scaled.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(image, 0, 0, previewWidth, previewHeight, null);
        g2d.dispose();

        pdfPreviewLabel.setIcon(new ImageIcon(scaled));
        pdfPreviewLabel.revalidate();
        pdfPreviewLabel.repaint();
    }

    public JButton getSavePdfButton() {
        return savePdfButton;
    }

    public JButton getRefundButton() {
        return refundButton;
    }

    public Invoice getSelectedInvoice() {
        return selectedInvoice;
    }

    private void setupUI() {
        setBackground(ColorPalette.BG_MAIN);
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(ColorPalette.BG_MAIN);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titleLabel = new JLabel("No invoice selected");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        pdfPreviewLabel = new JLabel();
        pdfPreviewLabel.setBackground(Color.WHITE);
        pdfPreviewLabel.setOpaque(true);
        pdfPreviewLabel.setHorizontalAlignment(JLabel.CENTER);
        pdfPreviewLabel.setVerticalAlignment(JLabel.TOP);

        previewScrollPane = new JScrollPane(pdfPreviewLabel);
        previewScrollPane.setPreferredSize(new Dimension(620, 800));
        previewScrollPane.setMaximumSize(new Dimension(620, 800));
        previewScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        previewScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        previewScrollPane.setBackground(ColorPalette.BG_MAIN);

        styleScrollBar(previewScrollPane.getVerticalScrollBar());
        styleScrollBar(previewScrollPane.getHorizontalScrollBar());

        contentPanel.add(previewScrollPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(ColorPalette.BG_MAIN);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        savePdfButton = new RoundedButton("save PDF", 15);
        savePdfButton.setPreferredSize(new Dimension(150, 40));
        savePdfButton.setMaximumSize(new Dimension(150, 40));
        savePdfButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        savePdfButton.setBackground(ColorPalette.ACCENT_SUCCESS);
        savePdfButton.setHasBorder(false);

        refundButton = new RoundedButton("Refund", 15);
        refundButton.setPreferredSize(new Dimension(150, 40));
        refundButton.setMaximumSize(new Dimension(150, 40));
        refundButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        refundButton.setBackground(ColorPalette.ACCENT_WARNING);
        refundButton.setHasBorder(false);

        buttonPanel.add(savePdfButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(refundButton);

        contentPanel.add(buttonPanel);

        JScrollPane mainScroll = new JScrollPane(contentPanel);
        mainScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainScroll.setBorder(null);
        mainScroll.setBackground(ColorPalette.BG_MAIN);

        styleScrollBar(mainScroll.getVerticalScrollBar());
        styleScrollBar(mainScroll.getHorizontalScrollBar());

        add(mainScroll, BorderLayout.CENTER);
    }

    private void styleScrollBar(JScrollBar bar) {
        if (bar.getOrientation() == Adjustable.VERTICAL) {
            bar.setPreferredSize(new Dimension(8, 0));
        } else {
            bar.setPreferredSize(new Dimension(0, 8));
        }

        bar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.trackColor = ColorPalette.BG_MAIN;
                this.thumbColor = ColorPalette.BG_TERTIARY;
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
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y,
                        thumbBounds.width - 1, thumbBounds.height - 1, arc, arc);
                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(trackColor);
                g2.fillRect(trackBounds.x, trackBounds.y,
                        trackBounds.width, trackBounds.height);
                g2.dispose();
            }
        });
        bar.setUnitIncrement(16);
    }
}
