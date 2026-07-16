package View;

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
import javax.swing.JScrollPane;
import Util.UIUtils;

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

        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            updateRefundButtonState();
            revalidate();
            repaint();
        });
    }

    public void updateRefundButtonState() {
        User currentUser = AppState.getInstance().getLoggedInUser();
        boolean isPrime = currentUser != null && currentUser.getUserType() == UserType.PRIME;
        refundButton.setVisible(isPrime);
        if (selectedInvoice != null)
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
        setBackground(ColorPalette.getInstance().getBgMain());
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(ColorPalette.getInstance().getBgMain());
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titleLabel = new JLabel("No invoice selected");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(ColorPalette.getInstance().getTextPrimary());
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
        previewScrollPane.setBackground(ColorPalette.getInstance().getBgMain());

        UIUtils.styleScrollBar(previewScrollPane.getVerticalScrollBar());
        UIUtils.styleScrollBar(previewScrollPane.getHorizontalScrollBar(), new Dimension(0, 8));

        contentPanel.add(previewScrollPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(ColorPalette.getInstance().getBgMain());
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        savePdfButton = new RoundedButton("save PDF", 15);
        savePdfButton.setPreferredSize(new Dimension(150, 40));
        savePdfButton.setMaximumSize(new Dimension(150, 40));
        savePdfButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        savePdfButton.setBackground(ColorPalette.getInstance().getAccentSuccess());
        savePdfButton.setHasBorder(false);

        refundButton = new RoundedButton("Refund", 15);
        refundButton.setPreferredSize(new Dimension(150, 40));
        refundButton.setMaximumSize(new Dimension(150, 40));
        refundButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        refundButton.setBackground(ColorPalette.getInstance().getAccentWarning());
        refundButton.setHasBorder(false);

        buttonPanel.add(savePdfButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(refundButton);

        contentPanel.add(buttonPanel);

        JScrollPane mainScroll = new JScrollPane(contentPanel);
        mainScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainScroll.setBorder(null);
        mainScroll.setBackground(ColorPalette.getInstance().getBgMain());

        UIUtils.styleScrollBar(mainScroll.getVerticalScrollBar());
        UIUtils.styleScrollBar(mainScroll.getHorizontalScrollBar(), new Dimension(0, 8));

        add(mainScroll, BorderLayout.CENTER);
    }
}
