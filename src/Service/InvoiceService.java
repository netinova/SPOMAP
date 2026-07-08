package Service;

import Controller.AppController;
import Model.AppState;
import Model.Invoice;
import Model.InvoiceItem;
import Model.InvoiceStatus;
import Model.Product;
import Model.ProductCatalog;
import Util.LocalDateTimeDeserializer;
import Util.LocalDateTimeSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class InvoiceService {
    private ObjectMapper mapper;
    private static final String INVOICE_FILE = "database/invoices.json";

    public InvoiceService() {
        this.mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        mapper.registerModule(module);

        initializeDatabase();
    }

    // being sure that database file exists and create if not
    private void initializeDatabase() {
        File file = new File(INVOICE_FILE);
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            try {
                mapper.writeValue(file, new ArrayList<>());
            } catch (IOException e) {
                throw new RuntimeException("Failed to initialize invoice database", e);
            }
        }
    }

    // Read all invoices from disk
    private List<Invoice> readAllInvoices() {
        File file = new File(INVOICE_FILE);
        try {
            return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, Invoice.class));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read invoices", e);
        }
    }

    // Write all invoices to disk
    private void writeAllInvoices(List<Invoice> invoices) {
        File file = new File(INVOICE_FILE);
        file.getParentFile().mkdirs();
        try {
            mapper.writeValue(file, invoices);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write invoices", e);
        }
    }

    private String generateNextId(List<Invoice> currentInvoices) {
        int maxId = 0;

        for (Invoice invoice : currentInvoices) {
            if (invoice != null && invoice.getInvoiceId() != null && invoice.getInvoiceId().startsWith("INV_")) {
                try {
                    int idNumber = Integer.parseInt(invoice.getInvoiceId().substring(4));
                    if (idNumber > maxId)
                        maxId = idNumber;
                } catch (NumberFormatException e) {
                    // Skip invalid IDs
                }
            }
        }

        int nextId = maxId + 1;
        return "INV_" + String.format("%06d", nextId);
    }

    // Add a new invoice - assigns ID if not present and saves
    public String addInvoice(Invoice invoice) {
        if (invoice == null)
            return null;

        List<Invoice> currentInvoices = readAllInvoices();

        if (invoice.getInvoiceId() == null) {
            invoice.setInvoiceId(generateNextId(currentInvoices));
        }

        if (getInvoiceById(invoice.getInvoiceId()) != null)
            return null;

        currentInvoices.add(invoice);
        writeAllInvoices(currentInvoices);// TODO: use def updateAfterNewInvoice for analytic
        return invoice.getInvoiceId();
    }

    public Invoice getInvoiceById(String invoiceId) {
        List<Invoice> currentInvoices = readAllInvoices();
        for (Invoice invoice : currentInvoices) {
            if (invoice != null && invoice.getInvoiceId() != null &&
                    invoice.getInvoiceId().equals(invoiceId)) {
                return invoice;
            }
        }
        return null;
    }

    public List<Invoice> getInvoicesByUserId(String userId) {
        List<Invoice> currentInvoices = readAllInvoices();
        return currentInvoices.stream()
                .filter(inv -> inv != null && userId.equals(inv.getUserId()))
                .collect(Collectors.toList());
    }

    public List<Invoice> getInvoicesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Invoice> currentInvoices = readAllInvoices();
        return currentInvoices.stream()
                .filter(inv -> inv != null && inv.getInvoiceDate() != null)
                .filter(inv -> !inv.getInvoiceDate().isBefore(startDate) &&
                        !inv.getInvoiceDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    public List<Invoice> getInvoicesByStatus(InvoiceStatus status) {
        List<Invoice> currentInvoices = readAllInvoices();
        return currentInvoices.stream()
                .filter(inv -> inv != null && inv.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Invoice> getInvoicesByUserAndDateRange(String userId,
            LocalDateTime startDate,
            LocalDateTime endDate) {
        List<Invoice> currentInvoices = readAllInvoices();
        return currentInvoices.stream()
                .filter(inv -> inv != null && userId.equals(inv.getUserId()))
                .filter(inv -> inv.getInvoiceDate() != null)
                .filter(inv -> !inv.getInvoiceDate().isBefore(startDate) &&
                        !inv.getInvoiceDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    public List<Invoice> getAllInvoices() {
        return readAllInvoices();
    }

    public boolean updateInvoiceStatus(String invoiceId, InvoiceStatus newStatus) {
        List<Invoice> currentInvoices = readAllInvoices();
        Invoice invoice = null;

        for (Invoice inv : currentInvoices) {
            if (inv != null && inv.getInvoiceId() != null && inv.getInvoiceId().equals(invoiceId)) {
                invoice = inv;
                break;
            }
        }

        if (invoice == null)
            return false;

        invoice.setStatus(newStatus);
        writeAllInvoices(currentInvoices);
        return true;
    }

    public List<Invoice> searchInvoices(String invoiceId, String userId,
            LocalDateTime dateFrom, LocalDateTime dateTo) {
        List<Invoice> allInvoices = readAllInvoices();
        return allInvoices.stream()
                .filter(inv -> inv != null)
                .filter(inv -> invoiceId == null || invoiceId.isEmpty() ||
                        inv.getInvoiceId().toLowerCase().contains(invoiceId.toLowerCase()))
                .filter(inv -> userId == null || userId.isEmpty() ||
                        userId.equals(inv.getUserId()))
                .filter(inv -> dateFrom == null ||
                        (inv.getInvoiceDate() != null && !inv.getInvoiceDate().isBefore(dateFrom)))
                .filter(inv -> dateTo == null ||
                        (inv.getInvoiceDate() != null && !inv.getInvoiceDate().isAfter(dateTo)))
                .collect(Collectors.toList());
    }

    public int getInvoiceCount() {
        return readAllInvoices().size();
    }

    public Map<String, Double> getTotalPurchaseByUserId(String userId) {
        List<Invoice> invoices = getInvoicesByUserId(userId);
        if (invoices.isEmpty()) {
            return new LinkedHashMap<>();
        }

        invoices.sort(Comparator.comparing(Invoice::getInvoiceDate));

        Map<YearMonth, Double> monthlyTotals = new TreeMap<>();
        for (Invoice inv : invoices) {
            monthlyTotals.merge(YearMonth.from(inv.getInvoiceDate()), inv.getFinalPrice(), Double::sum);
        }

        Map<String, Double> result = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        for (Map.Entry<YearMonth, Double> entry : monthlyTotals.entrySet()) {
            result.put(entry.getKey().format(formatter), entry.getValue());
        }

        return result;
    }

    public List<Map.Entry<Product, Integer>> getTopPurchasedProducts(String userId, int n) {
        List<Invoice> invoices = getInvoicesByUserId(userId);

        Map<String, Integer> productQuantities = new HashMap<>();
        for (Invoice inv : invoices) {
            for (InvoiceItem item : inv.getItems()) {
                productQuantities.merge(item.getProductId(), item.getQuantity(), Integer::sum);
            }
        }

        ProductCatalog catalog = ProductService.loadProducts();
        Map<String, Product> productMap = catalog.getProducts().stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        return productQuantities.entrySet().stream()
                .filter(e -> productMap.containsKey(e.getKey()))
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(n)
                .map(e -> new AbstractMap.SimpleEntry<>(productMap.get(e.getKey()), e.getValue()))
                .collect(Collectors.toList());
    }

    public double getTotalSavingsByUser(String userId) {
        return getInvoicesByUserId(userId).stream()
                .flatMap(inv -> inv.getItems().stream())
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity() * item.getDiscount() / 100.0)
                .sum();
    }

    public int getInvoiceCountForUser(String userId) {
        return (int) getInvoicesByUserId(userId).size();
    }
}
