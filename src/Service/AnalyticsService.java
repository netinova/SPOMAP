package Service;

import Model.AppState;
import Model.Invoice;
import Model.InvoiceItem;
import Model.InvoiceStatus;
import Model.Product;
import Model.ShopAnalytics;
import Model.ShopAnalytics.MonthlyAnalytics;
import Model.ShopAnalytics.DailyAnalytics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import Util.LocalDateTimeDeserializer;
import Util.LocalDateTimeSerializer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for shop analytics.
 * Calculates profit, revenue, and other metrics from invoices.
 * Maintains a JSON file with analytics data for use in graphs and reporting.
 */
public class AnalyticsService {

    private ObjectMapper mapper;
    private static final String ANALYTICS_FILE = "database/shop_analytics.json";
    private InvoiceService invoiceService;

    // Default cost margin (can be adjusted based on actual product costs)
    // This represents the percentage of revenue that goes to product costs
    private double defaultCostMargin = 0.6; // 60% of revenue is cost

    public AnalyticsService(InvoiceService invoiceService) {
        this.mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        mapper.registerModule(module);

        this.invoiceService = invoiceService;
        initializeAnalyticsFile();
    }

    private void initializeAnalyticsFile() {
        File file = new File(ANALYTICS_FILE);
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            try {
                ShopAnalytics initialAnalytics = new ShopAnalytics();
                mapper.writeValue(file, initialAnalytics);
            } catch (IOException e) {
                throw new RuntimeException("Failed to initialize analytics database", e);
            }
        }
    }

    private ShopAnalytics readAnalytics() {
        File file = new File(ANALYTICS_FILE);
        try {
            return mapper.readValue(file, ShopAnalytics.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read analytics", e);
        }
    }

    private void writeAnalytics(ShopAnalytics analytics) {
        File file = new File(ANALYTICS_FILE);
        file.getParentFile().mkdirs();
        try {
            analytics.setLastUpdated(LocalDateTime.now());
            mapper.writeValue(file, analytics);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write analytics", e);
        }
    }

    public void recalculateAllAnalytics() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        ShopAnalytics analytics = new ShopAnalytics();

        // Filter only paid invoices for revenue calculations
        List<Invoice> paidInvoices = invoices.stream()
                .filter(inv -> inv.getStatus() == InvoiceStatus.Paid)
                .collect(Collectors.toList());

        calculateOverallMetrics(analytics, paidInvoices);

        calculateTimeBasedMetrics(analytics, paidInvoices);

        calculateCustomerMetrics(analytics, paidInvoices);

        writeAnalytics(analytics);
    }

    private void calculateOverallMetrics(ShopAnalytics analytics, List<Invoice> paidInvoices) {
        double totalRevenue = 0.0;
        int totalItemsSold = 0;

        for (Invoice invoice : paidInvoices) {
            totalRevenue += invoice.getFinalPrice();
            for (InvoiceItem item : invoice.getItems()) {
                totalItemsSold += item.getQuantity();
            }
        }

        double totalProfit = calculateProfit(totalRevenue);

        analytics.setTotalRevenue(totalRevenue);
        analytics.setTotalProfit(totalProfit);
        analytics.setTotalOrders(paidInvoices.size());
        analytics.setTotalItemsSold(totalItemsSold);

        if (paidInvoices.size() > 0) {
            analytics.setAverageOrderValue(totalRevenue / paidInvoices.size());
        } else {
            analytics.setAverageOrderValue(0.0);
        }
    }

    private double calculateProfit(double revenue) {
        // Profit = Revenue - Cost
        // Cost = Revenue * costMargin

        double cost = revenue * defaultCostMargin;
        return revenue - cost;
    }

    public void setCostMargin(double margin) {
        if (margin >= 0.0 && margin <= 1.0) {
            this.defaultCostMargin = margin;
        } else {
            throw new IllegalArgumentException("Cost margin must be between 0.0 and 1.0");
        }
    }

    public double getCostMargin() {
        return this.defaultCostMargin;
    }

    private void calculateTimeBasedMetrics(ShopAnalytics analytics, List<Invoice> paidInvoices) {
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Group invoices by month
        Map<String, List<Invoice>> invoicesByMonth = paidInvoices.stream()
                .filter(inv -> inv.getInvoiceDate() != null)
                .collect(Collectors.groupingBy(
                        inv -> inv.getInvoiceDate().format(monthFormatter)));

        // Group invoices by day
        Map<String, List<Invoice>> invoicesByDay = paidInvoices.stream()
                .filter(inv -> inv.getInvoiceDate() != null)
                .collect(Collectors.groupingBy(
                        inv -> inv.getInvoiceDate().format(dayFormatter)));

        // Calculate monthly analytics
        List<MonthlyAnalytics> monthlyList = new ArrayList<>();
        for (Map.Entry<String, List<Invoice>> entry : invoicesByMonth.entrySet()) {
            String month = entry.getKey();
            List<Invoice> monthInvoices = entry.getValue();

            double monthRevenue = monthInvoices.stream()
                    .mapToDouble(Invoice::getFinalPrice)
                    .sum();
            double monthProfit = calculateProfit(monthRevenue);
            int monthOrders = monthInvoices.size();

            monthlyList.add(new MonthlyAnalytics(month, monthRevenue, monthProfit, monthOrders));
        }

        // Sort by month
        monthlyList.sort(Comparator.comparing(MonthlyAnalytics::getMonth));
        analytics.setMonthlyAnalytics(monthlyList);

        // Calculate daily analytics
        List<DailyAnalytics> dailyList = new ArrayList<>();
        for (Map.Entry<String, List<Invoice>> entry : invoicesByDay.entrySet()) {
            String date = entry.getKey();
            List<Invoice> dayInvoices = entry.getValue();

            double dayRevenue = dayInvoices.stream()
                    .mapToDouble(Invoice::getFinalPrice)
                    .sum();
            int dayOrders = dayInvoices.size();

            dailyList.add(new DailyAnalytics(date, dayRevenue, dayOrders));
        }

        // Sort by date
        dailyList.sort(Comparator.comparing(DailyAnalytics::getDate));
        analytics.setDailyAnalytics(dailyList);
    }

    private void calculateCustomerMetrics(ShopAnalytics analytics, List<Invoice> paidInvoices) {
        int totalNormal = AppState.getInstance().normalUsersList.getUsers().size();
        int totalPrime = AppState.getInstance().primeUsersList.getUsers().size();

        int totalCustomers = totalNormal + totalPrime;
        analytics.setTotalCustomers(totalCustomers);

        Map<String, Integer> customerOrderCount = new HashMap<>();
        for (Invoice invoice : paidInvoices) {
            String userId = invoice.getUserId();
            customerOrderCount.put(userId, customerOrderCount.getOrDefault(userId, 0) + 1);
        }

        // Returning customers = those with >= 1 order
        long returningCustomers = customerOrderCount.values().stream()
                .filter(count -> count >= 1)
                .count();
        analytics.setReturningCustomers((int) returningCustomers);
    }

    public ShopAnalytics getAnalytics() {
        return readAnalytics();
    }

    public ShopAnalytics getAnalyticsForDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Invoice> invoices = invoiceService.getInvoicesByDateRange(startDate, endDate);
        ShopAnalytics analytics = new ShopAnalytics();
        analytics.setAnalyticsStartDate(startDate);

        List<Invoice> paidInvoices = invoices.stream()
                .filter(inv -> inv.getStatus() == InvoiceStatus.Paid)
                .collect(Collectors.toList());

        calculateOverallMetrics(analytics, paidInvoices);
        calculateTimeBasedMetrics(analytics, paidInvoices);
        calculateCustomerMetrics(analytics, paidInvoices);

        return analytics;
    }

    public List<Map<String, Object>> getMonthlyRevenueData() {
        ShopAnalytics analytics = readAnalytics();
        List<Map<String, Object>> data = new ArrayList<>();

        for (MonthlyAnalytics ma : analytics.getMonthlyAnalytics()) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("month", ma.getMonth());
            entry.put("revenue", ma.getRevenue());
            entry.put("profit", ma.getProfit());
            entry.put("orders", ma.getOrders());
            data.add(entry);
        }

        return data;
    }

    public List<Map<String, Object>> getDailyRevenueData() {
        ShopAnalytics analytics = readAnalytics();
        List<Map<String, Object>> data = new ArrayList<>();

        for (DailyAnalytics da : analytics.getDailyAnalytics()) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("date", da.getDate());
            entry.put("revenue", da.getRevenue());
            entry.put("orders", da.getOrders());
            data.add(entry);
        }

        return data;
    }

    public void updateAfterNewInvoice(String invoiceId) {

        recalculateAllAnalytics();
    }
}
