package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import Components.RoundedButton;
import Controller.InvoiceController;
import Model.Invoice;
import Model.InvoiceItem;
import Model.InvoiceStatus;
import Util.ColorPalette;

public class InvoiceDetailView extends JPanel {

    private Invoice invoice;
    @SuppressWarnings("unused")
    private InvoiceController controller;

    private JLabel invoiceIdLabel;
    private JLabel invoiceDateLabel;
    private JLabel userIdLabel;
    private JLabel statusLabel;
    private JTable itemsTable;
    private JLabel totalAmountLabel;
    private JButton refundButton;
    private JButton savePdfButton;

    public InvoiceDetailView(InvoiceController controller) {
        this.controller = controller;
        setupUI();
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
        updateDisplay();
    }

    private void setupUI() {
        this.setBackground(ColorPalette.BG_MAIN);
        this.setLayout(new BorderLayout());

        // Main content panel with padding
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(ColorPalette.BG_MAIN);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Top section: Invoice header info
        JPanel headerPanel = createHeaderPanel();
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Middle section: Items table
        JPanel tablePanel = createItemsTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        // Bottom section: Total and buttons
        JPanel bottomPanel = createBottomPanel();
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setBackground(ColorPalette.BG_MAIN);

        this.add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ColorPalette.BG_SECONDARY);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.BORDER, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Title
        JLabel titleLabel = new JLabel("INVOICE DETAILS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Invoice ID
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel idLabelTitle = new JLabel("Invoice ID:");
        idLabelTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        idLabelTitle.setForeground(ColorPalette.TEXT_MUTED);
        panel.add(idLabelTitle, gbc);

        invoiceIdLabel = new JLabel("");
        invoiceIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        invoiceIdLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 1;
        panel.add(invoiceIdLabel, gbc);

        // Date
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel dateLabelTitle = new JLabel("Date:");
        dateLabelTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabelTitle.setForeground(ColorPalette.TEXT_MUTED);
        panel.add(dateLabelTitle, gbc);

        invoiceDateLabel = new JLabel("");
        invoiceDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        invoiceDateLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 1;
        panel.add(invoiceDateLabel, gbc);

        // User ID
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel userLabelTitle = new JLabel("User ID:");
        userLabelTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabelTitle.setForeground(ColorPalette.TEXT_MUTED);
        panel.add(userLabelTitle, gbc);

        userIdLabel = new JLabel("");
        userIdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userIdLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 1;
        panel.add(userIdLabel, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel statusLabelTitle = new JLabel("Status:");
        statusLabelTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabelTitle.setForeground(ColorPalette.TEXT_MUTED);
        panel.add(statusLabelTitle, gbc);

        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 1;
        panel.add(statusLabel, gbc);

        return panel;
    }

    private JPanel createItemsTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorPalette.BG_MAIN);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Table header
        JLabel itemsTitle = new JLabel("ITEMS");
        itemsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        itemsTitle.setForeground(ColorPalette.TEXT_PRIMARY);
        itemsTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(itemsTitle, BorderLayout.NORTH);

        String[] columnNames = { "Product ID", "Product Name", "Unit Price", "Quantity", "Discount", "Total" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        itemsTable = new JTable(tableModel);
        itemsTable.setRowHeight(35);
        itemsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        itemsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        itemsTable.getTableHeader().setBackground(ColorPalette.BG_TERTIARY);
        itemsTable.getTableHeader().setForeground(ColorPalette.TEXT_PRIMARY);
        itemsTable.setBackground(ColorPalette.BG_SECONDARY);
        itemsTable.setForeground(ColorPalette.TEXT_PRIMARY);
        itemsTable.setGridColor(ColorPalette.BORDER);
        itemsTable.setSelectionBackground(ColorPalette.SELECTION_BG);
        itemsTable.setSelectionForeground(ColorPalette.TEXT_PRIMARY);

        // Center align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columnNames.length; i++) {
            itemsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Set column widths
        itemsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        itemsTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        itemsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        itemsTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        itemsTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        itemsTable.getColumnModel().getColumn(5).setPreferredWidth(100);

        JScrollPane tableScrollPane = new JScrollPane(itemsTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER));
        tableScrollPane.setBackground(ColorPalette.BG_MAIN);

        panel.add(tableScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorPalette.BG_SECONDARY);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.BORDER, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.Y_AXIS));
        totalPanel.setBackground(ColorPalette.BG_SECONDARY);
        totalPanel.setOpaque(false);

        JLabel totalLabel = new JLabel("TOTAL AMOUNT:");
        totalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        totalLabel.setForeground(ColorPalette.TEXT_MUTED);
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        totalPanel.add(totalLabel);

        totalPanel.add(Box.createVerticalStrut(5));

        totalAmountLabel = new JLabel("$0.00");
        totalAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        totalAmountLabel.setForeground(ColorPalette.ACCENT_PRIMARY);
        totalAmountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        totalPanel.add(totalAmountLabel);

        panel.add(totalPanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(ColorPalette.BG_SECONDARY);
        buttonPanel.setOpaque(false);

        refundButton = new RoundedButton("Refund Invoice", 8);
        refundButton.setPreferredSize(new Dimension(150, 45));
        refundButton.setMaximumSize(new Dimension(150, 45));
        refundButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refundButton.setForeground(ColorPalette.ACCENT_WARNING);
        refundButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Refund functionality - to be implemented",
                    "Refund Invoice",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        savePdfButton = new RoundedButton("Save as PDF", 8);
        savePdfButton.setPreferredSize(new Dimension(150, 45));
        savePdfButton.setMaximumSize(new Dimension(150, 45));
        savePdfButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        savePdfButton.setForeground(ColorPalette.ACCENT_SUCCESS);
        savePdfButton.addActionListener(e -> saveInvoiceAsPdf());

        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(refundButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(savePdfButton);

        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    private void updateDisplay() {
        if (invoice == null) {
            return;
        }

        // Update header info
        invoiceIdLabel.setText(invoice.getInvoiceId() != null ? invoice.getInvoiceId() : "—");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String dateStr = invoice.getInvoiceDate() != null
                ? invoice.getInvoiceDate().format(dtf)
                : "—";
        invoiceDateLabel.setText(dateStr);

        userIdLabel.setText(invoice.getUserId() != null ? invoice.getUserId() : "—");

        // Update status
        String statusText = invoice.getStatus() == InvoiceStatus.Paid ? "Paid" : "Refunded";
        statusLabel.setText(statusText);
        statusLabel.setForeground(invoice.getStatus() == InvoiceStatus.Paid
                ? ColorPalette.ACCENT_SUCCESS
                : ColorPalette.ACCENT_WARNING);

        // Update items table
        DefaultTableModel model = (DefaultTableModel) itemsTable.getModel();
        model.setRowCount(0);

        if (invoice.getItems() != null) {
            for (InvoiceItem item : invoice.getItems()) {
                Object[] row = {
                        item.getProductId(),
                        item.getProductName(),
                        String.format("$%.2f", item.getUnitPrice()),
                        item.getQuantity(),
                        String.format("%.0f%%", item.getDiscount()),
                        String.format("$%.2f", item.getTotalPrice())
                };
                model.addRow(row);
            }
        }

        // Update total amount
        totalAmountLabel.setText(String.format("$%.2f", invoice.getFinalPrice()));

        // Revalidate and repaint
        revalidate();
        repaint();
    }

    private void saveInvoiceAsPdf() {
        if (invoice == null) {
            JOptionPane.showMessageDialog(this,
                    "No invoice to save",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Invoice PDF");
        fileChooser.setSelectedFile(new File(invoice.getInvoiceId() + ".pdf"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".pdf");
            }

            try {
                generatePdf(fileToSave);
                JOptionPane.showMessageDialog(this,
                        "Invoice saved successfully to:\n" + fileToSave.getAbsolutePath(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Failed to save PDF: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void generatePdf(File file) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        // Title
        Paragraph title = new Paragraph("INVOICE", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24));
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Invoice details
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setWidths(new float[] { 1, 2 });
        infoTable.setSpacingBefore(10);
        infoTable.setSpacingAfter(20);

        addInfoCell(infoTable, "Invoice ID:", invoice.getInvoiceId());
        addInfoCell(infoTable, "Date:", invoice.getInvoiceDate() != null
                ? invoice.getInvoiceDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"))
                : "—");
        addInfoCell(infoTable, "User ID:", invoice.getUserId());
        addInfoCell(infoTable, "Status:", invoice.getStatus() == InvoiceStatus.Paid ? "Paid" : "Refunded");

        document.add(infoTable);

        // Items table
        PdfPTable itemsTable = new PdfPTable(6);
        itemsTable.setWidthPercentage(100);
        itemsTable.setWidths(new float[] { 1, 2, 1, 1, 1, 1 });
        itemsTable.setSpacingBefore(10);
        itemsTable.setSpacingAfter(20);

        // Header row
        String[] headers = { "Product ID", "Product Name", "Unit Price", "Qty", "Discount", "Total" };
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new Color(60, 60, 60));
            cell.setPadding(5);
            itemsTable.addCell(cell);
        }

        // Data rows
        if (invoice.getItems() != null) {
            for (InvoiceItem item : invoice.getItems()) {
                itemsTable.addCell(createCell(item.getProductId(), Element.ALIGN_CENTER));
                itemsTable.addCell(createCell(item.getProductName(), Element.ALIGN_CENTER));
                itemsTable.addCell(createCell(String.format("$%.2f", item.getUnitPrice()), Element.ALIGN_CENTER));
                itemsTable.addCell(createCell(String.valueOf(item.getQuantity()), Element.ALIGN_CENTER));
                itemsTable.addCell(createCell(String.format("%.0f%%", item.getDiscount()), Element.ALIGN_CENTER));
                itemsTable.addCell(createCell(String.format("$%.2f", item.getTotalPrice()), Element.ALIGN_CENTER));
            }
        }

        document.add(itemsTable);

        // Total
        Paragraph totalPara = new Paragraph("TOTAL AMOUNT: " + String.format("$%.2f", invoice.getFinalPrice()),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
        totalPara.setAlignment(Element.ALIGN_RIGHT);
        totalPara.setSpacingBefore(10);
        document.add(totalPara);

        document.close();
    }

    private void addInfoCell(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, FontFactory.getFont(FontFactory.HELVETICA, 10)));
        labelCell.setBorder(PdfPCell.NO_BORDER);
        labelCell.setPadding(5);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(
                new Phrase(value != null ? value : "—", FontFactory.getFont(FontFactory.HELVETICA, 10)));
        valueCell.setBorder(PdfPCell.NO_BORDER);
        valueCell.setPadding(5);
        table.addCell(valueCell);
    }

    private PdfPCell createCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 9)));
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.BOX);
        cell.setPadding(5);
        return cell;
    }
}
