package Controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import Model.AppState;
import Model.Invoice;
import Model.InvoiceItem;
import Model.UserType;
import Model.ViewType;
import Service.InvoiceService;
import Util.Validator;
import View.InvoiceDetailView;
import View.InvoiceView;

public class InvoiceController {

    private OnChangeViewListener listener;
    private InvoiceView invoiceView;
    private InvoiceDetailView invoiceDetailView;
    private InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public void setOnChangeViewListener(OnChangeViewListener listener) {
        this.listener = listener;
    }

    public void setView(InvoiceView invoiceView, InvoiceDetailView invoiceDetailView) {
        this.invoiceView = invoiceView;
        this.invoiceDetailView = invoiceDetailView;

        if (invoiceDetailView != null) {
            invoiceDetailView.getSavePdfButton().addActionListener(e -> handleSavePdf());
            // Refund button
        }
    }

    public void loadInvoicesForCurrentUser() {
        var loggedUser = AppState.getInstance().getLoggedInUser();
        if (loggedUser == null || invoiceView == null) {
            return;
        }

        String userId = (loggedUser.getUserType() != UserType.ADMIN)
                ? loggedUser.getUserId()
                : null;

        List<Invoice> results = invoiceService.searchInvoices(null, userId, null, null);
        invoiceView.displayInvoices(results);
    }

    public void generatePdfPreviewImage(Invoice invoice) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            generatePdfToStream(invoice, baos);
            byte[] pdfBytes = baos.toByteArray();

            PDDocument pdfDoc = PDDocument.load(pdfBytes);
            PDFRenderer renderer = new PDFRenderer(pdfDoc);
            BufferedImage image = renderer.renderImageWithDPI(0, 150);
            pdfDoc.close();

            invoiceDetailView.setPreviewImage(image);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(invoiceDetailView,
                    "Failed to generate PDF preview: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleSavePdf() {
        if (invoiceDetailView == null || invoiceDetailView.getSelectedInvoice() == null) {
            return;
        }

        Invoice invoice = invoiceDetailView.getSelectedInvoice();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Invoice PDF");
        fileChooser.setSelectedFile(new File("Invoice_" + invoice.getInvoiceId() + ".pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));

        if (fileChooser.showSaveDialog(invoiceDetailView) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".pdf");
            }
            generatePdfFile(invoice, fileToSave);
        }
    }

    public void generatePdfFile(Invoice invoice, File outputFile) {
        try {
            FileOutputStream fos = new FileOutputStream(outputFile);
            generatePdfToStream(invoice, fos);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(invoiceDetailView,
                    "Failed to save PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generatePdfToStream(Invoice invoice, java.io.OutputStream outputStream) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Paragraph company = new Paragraph("Spomap",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new Color(50, 50, 150)));
            company.setAlignment(Element.ALIGN_CENTER);
            document.add(company);

            Paragraph description = new Paragraph("shop project of mani and parsa",
                    FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY));
            description.setAlignment(Element.ALIGN_CENTER);
            document.add(description);

            document.add(Chunk.NEWLINE);

            Paragraph title = new Paragraph("INVOICE",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, new Color(0, 102, 204)));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(Chunk.NEWLINE);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            String dateStr = invoice.getInvoiceDate() != null
                    ? invoice.getInvoiceDate().format(dtf)
                    : "N/A";

            Paragraph details = new Paragraph();
            details.add(new Chunk("Invoice ID: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            details.add(invoice.getInvoiceId() + "\n");
            details.add(new Chunk("Date: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            details.add(dateStr + "\n");
            details.add(new Chunk("User ID: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            details.add(invoice.getUserId() + "\n");
            details.add(new Chunk("Status: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            details.add(invoice.getStatus().toString());
            document.add(details);

            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new int[] { 2, 3, 2, 2, 2, 1, 2 });
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
            Color headerBg = new Color(0, 102, 204);

            addStyledCell(table, "Product ID", headerFont, headerBg, Element.ALIGN_CENTER);
            addStyledCell(table, "Product Name", headerFont, headerBg, Element.ALIGN_LEFT);
            addStyledCell(table, "Unit Price", headerFont, headerBg, Element.ALIGN_CENTER);
            addStyledCell(table, "Discount %", headerFont, headerBg, Element.ALIGN_CENTER);
            addStyledCell(table, "Disc. Price", headerFont, headerBg, Element.ALIGN_CENTER);
            addStyledCell(table, "Qty", headerFont, headerBg, Element.ALIGN_CENTER);
            addStyledCell(table, "Total", headerFont, headerBg, Element.ALIGN_CENTER);

            Color rowColor1 = new Color(240, 240, 255);
            Color rowColor2 = Color.WHITE;
            int i = 0;
            double totalRawPrice = 0.0;
            double totalFinalPrice = 0.0;

            for (InvoiceItem item : invoice.getItems()) {
                double unitPrice = item.getUnitPrice();
                double discount = item.getDiscount();
                double discountedUnitPrice = unitPrice * (100 - discount) / 100.0;
                int qty = item.getQuantity();
                double itemTotal = discountedUnitPrice * qty;
                double rawItemTotal = unitPrice * qty;

                totalRawPrice += rawItemTotal;
                totalFinalPrice += itemTotal;

                Color rowColor = (i++ % 2 == 0) ? rowColor1 : rowColor2;

                addStyledCell(table, item.getProductId(), null, rowColor, Element.ALIGN_CENTER);
                addStyledCell(table, item.getProductName(), null, rowColor, Element.ALIGN_LEFT);
                addStyledCell(table, String.format("$%.2f", unitPrice), null, rowColor, Element.ALIGN_RIGHT);
                addStyledCell(table, String.format("%.0f%%", discount), null, rowColor, Element.ALIGN_CENTER);
                addStyledCell(table, String.format("$%.2f", discountedUnitPrice), null, rowColor, Element.ALIGN_RIGHT);
                addStyledCell(table, String.valueOf(qty), null, rowColor, Element.ALIGN_CENTER);
                addStyledCell(table, String.format("$%.2f", itemTotal), null, rowColor, Element.ALIGN_RIGHT);
            }

            document.add(table);

            double totalSavings = totalRawPrice - totalFinalPrice;

            Paragraph summary = new Paragraph();
            summary.setSpacingBefore(5);
            summary.setSpacingAfter(5);

            Chunk rawLabel = new Chunk("Total (before discount): ",
                    FontFactory.getFont(FontFactory.HELVETICA, 12));
            Chunk rawValue = new Chunk(String.format("$%.2f", totalRawPrice),
                    FontFactory.getFont(FontFactory.HELVETICA, 12));
            summary.add(rawLabel);
            summary.add(rawValue);
            summary.add(Chunk.NEWLINE);

            Chunk savedLabel = new Chunk("You saved: ",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, Color.GREEN.darker()));
            Chunk savedValue = new Chunk(String.format("$%.2f", totalSavings),
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.GREEN.darker()));
            summary.add(savedLabel);
            summary.add(savedValue);
            summary.add(Chunk.NEWLINE);

            Chunk finalLabel = new Chunk("Total Amount: ",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new Color(0, 102, 204)));
            Chunk finalValue = new Chunk(String.format("$%.2f", totalFinalPrice),
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new Color(0, 102, 204)));
            summary.add(finalLabel);
            summary.add(finalValue);

            summary.setAlignment(Element.ALIGN_RIGHT);
            document.add(summary);

            document.add(Chunk.NEWLINE);

            Paragraph footer = new Paragraph("Thank you for your purchase!",
                    FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, Color.GRAY));
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("PDF generation failed", e);
        }
    }

    private void addStyledCell(PdfPTable table, String text, Font font, Color bgColor, int alignment) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, font != null ? font : FontFactory.getFont(FontFactory.HELVETICA)));
        if (bgColor != null) {
            cell.setBackgroundColor(bgColor);
        }
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        table.addCell(cell);
    }

    public Validator.ValidationResult validationDate(String date) {
        return Validator.validationManiDate(date);
    }

    public void handleSearch() {
        if (invoiceView == null || invoiceService == null)
            return;

        String invoiceId = invoiceView.getSearchInvoiceId();
        String userId = invoiceView.getSearchUserId();
        String dateFromStr = invoiceView.getSearchDateFrom();
        String dateToStr = invoiceView.getSearchDateTo();

        var loggedUser = AppState.getInstance().getLoggedInUser();
        if (loggedUser == null)
            return;

        if (loggedUser.getUserType() != UserType.ADMIN) {
            userId = loggedUser.getUserId();
        }

        if (loggedUser.getUserType() == UserType.ADMIN && (userId == null || userId.isEmpty()))
            userId = null;

        if (invoiceId == null || invoiceId.isEmpty())
            invoiceId = null;

        if (userId == null || userId.isEmpty())
            userId = null;

        LocalDateTime dateFrom = parseDate(dateFromStr);
        LocalDateTime dateTo = parseDateEndOfDay(dateToStr);

        List<Invoice> results = invoiceService.searchInvoices(invoiceId, userId, dateFrom, dateTo);
        invoiceView.displayInvoices(results);
    }

    private LocalDateTime parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty())
            return null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            return LocalDate.parse(dateStr, formatter).atStartOfDay();
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private LocalDateTime parseDateEndOfDay(String dateStr) {
        if (dateStr == null || dateStr.isEmpty())
            return null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            return LocalDate.parse(dateStr, formatter).atTime(LocalTime.MAX);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public void onInvoiceCardClick(Invoice invoice) {
        if (invoiceView == null)
            return;
        invoiceDetailView.setInvoice(invoice);
        if (listener != null) {
            listener.changeView(ViewType.INVOICE_DETAIL.getViewId());
        }
    }
}
