package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import Components.InvoiceCard;
import Components.InvoiceSearchPanel;
import Controller.InvoiceController;
import Model.AppState;
import Model.Invoice;
import Model.UserType;
import Util.ColorPalette;
import Util.UIUtils;

public class InvoiceView extends JPanel {

    private JPanel resultsGrid;
    private InvoiceSearchPanel invoiceSearchPanel;

    private InvoiceController controller;

    public InvoiceView(InvoiceController controller) {

        this.controller = controller;

        setupUI();
        attachEvents();

        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            attachEvents();
            revalidate();
            repaint();
        });
    }

    private void attachEvents() {
        invoiceSearchPanel.addValidationListener(value -> controller.validationDate(value));
        invoiceSearchPanel.addActionListener(e -> controller.handleSearch());
    }

    private void setupUI() {
        this.setBackground(ColorPalette.getInstance().getBgMain());
        this.setLayout(new BorderLayout());

        var contentPanel = new JPanel();
        contentPanel.setBackground(ColorPalette.getInstance().getBgMain());
        // Use BorderLayout instead of BoxLayout
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(ColorPalette.getInstance().getBorder())));

        invoiceSearchPanel = new InvoiceSearchPanel();
        contentPanel.add(invoiceSearchPanel, BorderLayout.NORTH); // fixed height

        resultsGrid = new JPanel();
        resultsGrid.setBackground(ColorPalette.getInstance().getBgMain());
        resultsGrid.setLayout(new GridBagLayout());
        resultsGrid.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(resultsGrid);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.setBackground(ColorPalette.getInstance().getBgMain());

        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        UIUtils.styleScrollBar(verticalBar);

        contentPanel.add(scrollPane, BorderLayout.CENTER); // fills remaining space

        this.add(contentPanel);
    }

    public void changeLayout() {
        boolean isAdmin = AppState.getInstance().getLoggedInUser().getUserType() == UserType.ADMIN;
        invoiceSearchPanel.setAdminMode(isAdmin);
        controller.loadInvoicesForCurrentUser();
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
            emptyLabel.setForeground(ColorPalette.getInstance().getTextMuted());
            emptyPanel.add(emptyLabel);

            resultsGrid.add(emptyPanel, gbc);
        } else {
            boolean isAdmin = AppState.getInstance().getLoggedInUser() != null
                    && AppState.getInstance().getLoggedInUser().getUserType() == UserType.ADMIN;

            for (int i = 0; i < invoices.size(); i++) {
                Invoice invoice = invoices.get(i);
                InvoiceCard card = new InvoiceCard(invoice, isAdmin);
                card.addActionListener(e -> {
                    if ("cardClick".equals(e.getActionCommand())) {
                        controller.onInvoiceCardClick(invoice);
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

}
