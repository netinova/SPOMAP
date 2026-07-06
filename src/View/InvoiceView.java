package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

import Components.InvoiceCard;
import Components.InvoiceSearchPanel;
import Controller.InvoiceController;
import Model.AppState;
import Model.Invoice;
import Model.UserType;
import Util.ColorPalette;

public class InvoiceView extends JPanel {

    private JPanel resultsGrid;
    private InvoiceSearchPanel invoiceSearchPanel;

    private InvoiceController controller;

    public InvoiceView(InvoiceController controller) {

        this.controller = controller;

        setupUI();
        attachEvents();
    }

    private void attachEvents() {
        invoiceSearchPanel.addValidationListener(value -> controller.validationDate(value));
        invoiceSearchPanel.addActionListener(e -> controller.handleSearch());
    }

    private void setupUI() {
        this.setBackground(ColorPalette.BG_MAIN);
        this.setLayout(new BorderLayout());

        var contentPanel = new JPanel();
        contentPanel.setBackground(ColorPalette.BG_MAIN);
        // Use BorderLayout instead of BoxLayout
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(ColorPalette.BORDER)));

        invoiceSearchPanel = new InvoiceSearchPanel();
        contentPanel.add(invoiceSearchPanel, BorderLayout.NORTH); // fixed height

        resultsGrid = new JPanel();
        resultsGrid.setBackground(ColorPalette.BG_MAIN);
        resultsGrid.setLayout(new GridBagLayout());
        resultsGrid.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(resultsGrid);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.setBackground(ColorPalette.BG_MAIN);

        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        styleScrollBar(verticalBar);

        contentPanel.add(scrollPane, BorderLayout.CENTER); // fills remaining space

        this.add(contentPanel);
    }

    public void changeLayout() {
        boolean isAdmin = AppState.getInstance().getLoggedInUser().getUserType() == UserType.ADMIN;
        invoiceSearchPanel.setAdminMode(isAdmin);
        controller.handleSearch();
    }

    public String getSearchInvoiceId() {
        return invoiceSearchPanel.getInvoiceIdText();
    }

    public String getSearchUserId() {
        return invoiceSearchPanel.getUserIdText();
    }

    public String getSearchDateFrom() {
        return invoiceSearchPanel.getDateFromText();
    }

    public String getSearchDateTo() {
        return invoiceSearchPanel.getDateToText();
    }

    public void displayInvoices(List<Invoice> invoices) {
        resultsGrid.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new java.awt.Insets(5, 0, 5, 0);

        if (invoices == null || invoices.isEmpty()) {
            gbc.gridy = 0;
            gbc.weighty = 1.0;
            gbc.fill = GridBagConstraints.BOTH;

            JPanel emptyPanel = new JPanel(new GridBagLayout());
            emptyPanel.setOpaque(false);

            JLabel emptyLabel = new JLabel("No invoices found");
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            emptyLabel.setForeground(ColorPalette.TEXT_MUTED);
            emptyPanel.add(emptyLabel);

            resultsGrid.add(emptyPanel, gbc);
        } else {
            boolean isAdmin = AppState.getInstance().getLoggedInUser() != null
                    && AppState.getInstance().getLoggedInUser().getUserType() == UserType.ADMIN;

            for (int i = 0; i < invoices.size(); i++) {
                InvoiceCard card = new InvoiceCard(invoices.get(i), isAdmin);
                card.addActionListener(e -> {
                    if ("cardClick".equals(e.getActionCommand())) {
                        // TODO: navigate to invoice detail view
                    }
                });

                gbc.gridy = i;
                resultsGrid.add(card, gbc);
            }

            // Filler at bottom for top-alignment
            gbc.gridy = invoices.size();
            gbc.weighty = 1.0;
            gbc.fill = GridBagConstraints.BOTH;
            JPanel filler = new JPanel();
            filler.setOpaque(false);
            resultsGrid.add(filler, gbc);
        }

        resultsGrid.revalidate();
        resultsGrid.repaint();
    }

    private void styleScrollBar(JScrollBar bar) {

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
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width - 1, thumbBounds.height - 1, arc, arc);

                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(trackColor);
                g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
                g2.dispose();
            }
        });
        bar.setPreferredSize(new Dimension(8, 0));
        bar.setUnitIncrement(16);

    }
}
