package Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import Util.LocalDateTimeSerializer;
import Util.LocalDateTimeDeserializer;


public class ShopAnalytics {

    // General Shop Information
    private String shopName;
    private String currency;
    private LocalDateTime lastUpdated;
    private LocalDateTime analyticsStartDate;

    // Overall Metrics
    private double totalRevenue;
    private double totalProfit;
    private int totalOrders;
    private int totalItemsSold;
    private double averageOrderValue;

    // Time-based metrics
    private List<MonthlyAnalytics> monthlyAnalytics;
    private List<DailyAnalytics> dailyAnalytics;

    // Customer metrics
    private int totalCustomers;
    private int returningCustomers;
    private int totalPrimeUsers;

    // Product metrics
    private List<ProductAnalytics> productAnalytics;

    public ShopAnalytics() {
        this.shopName = "Spomap Shop";
        this.currency = "USD";
        this.lastUpdated = LocalDateTime.now();
        this.analyticsStartDate = LocalDateTime.now();
        this.totalRevenue = 0.0;
        this.totalProfit = 0.0;
        this.totalOrders = 0;
        this.totalItemsSold = 0;
        this.averageOrderValue = 0.0;
        this.monthlyAnalytics = new ArrayList<>();
        this.dailyAnalytics = new ArrayList<>();
        this.totalCustomers = 0;
        this.returningCustomers = 0;
        this.totalPrimeUsers = 0;
        this.productAnalytics = new ArrayList<>();
    }

    // Getters and Setters

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public LocalDateTime getAnalyticsStartDate() {
        return analyticsStartDate;
    }

    public void setAnalyticsStartDate(LocalDateTime analyticsStartDate) {
        this.analyticsStartDate = analyticsStartDate;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public int getTotalItemsSold() {
        return totalItemsSold;
    }

    public void setTotalItemsSold(int totalItemsSold) {
        this.totalItemsSold = totalItemsSold;
    }

    public double getAverageOrderValue() {
        return averageOrderValue;
    }

    public void setAverageOrderValue(double averageOrderValue) {
        this.averageOrderValue = averageOrderValue;
    }

    public List<MonthlyAnalytics> getMonthlyAnalytics() {
        return monthlyAnalytics;
    }

    public void setMonthlyAnalytics(List<MonthlyAnalytics> monthlyAnalytics) {
        this.monthlyAnalytics = monthlyAnalytics;
    }

    public List<DailyAnalytics> getDailyAnalytics() {
        return dailyAnalytics;
    }

    public void setDailyAnalytics(List<DailyAnalytics> dailyAnalytics) {
        this.dailyAnalytics = dailyAnalytics;
    }

    public int getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(int totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public int getReturningCustomers() {
        return returningCustomers;
    }

    public void setReturningCustomers(int returningCustomers) {
        this.returningCustomers = returningCustomers;
    }

    public int getTotalPrimeUsers() {
        return totalPrimeUsers;
    }

    public void setTotalPrimeUsers(int totalPrimeUsers) {
        this.totalPrimeUsers = totalPrimeUsers;
    }

    public List<ProductAnalytics> getProductAnalytics() {
        return productAnalytics;
    }

    public void setProductAnalytics(List<ProductAnalytics> productAnalytics) {
        this.productAnalytics = productAnalytics;
    }

    public void addOrUpdateMonthlyAnalytics(String month, double revenue, double profit, int orders) {
        for (MonthlyAnalytics ma : monthlyAnalytics) {
            if (ma.getMonth().equals(month)) {
                ma.setRevenue(revenue);
                ma.setProfit(profit);
                ma.setOrders(orders);
                return;
            }
        }
        monthlyAnalytics.add(new MonthlyAnalytics(month, revenue, profit, orders));
    }


    public void addOrUpdateDailyAnalytics(String date, double revenue, int orders) {
        for (DailyAnalytics da : dailyAnalytics) {
            if (da.getDate().equals(date)) {
                da.setRevenue(revenue);
                da.setOrders(orders);
                return;
            }
        }
        dailyAnalytics.add(new DailyAnalytics(date, revenue, orders));
    }

    public static class ProductAnalytics {
        private String productId;
        private String productName;
        private int totalQuantitySold;
        private double totalRevenue;
        private int totalOrdersContaining;

        public ProductAnalytics() {
        }

        public ProductAnalytics(String productId, String productName, int totalQuantitySold, double totalRevenue, int totalOrdersContaining) {
            this.productId = productId;
            this.productName = productName;
            this.totalQuantitySold = totalQuantitySold;
            this.totalRevenue = totalRevenue;
            this.totalOrdersContaining = totalOrdersContaining;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getTotalQuantitySold() {
            return totalQuantitySold;
        }

        public void setTotalQuantitySold(int totalQuantitySold) {
            this.totalQuantitySold = totalQuantitySold;
        }

        public double getTotalRevenue() {
            return totalRevenue;
        }

        public void setTotalRevenue(double totalRevenue) {
            this.totalRevenue = totalRevenue;
        }

        public int getTotalOrdersContaining() {
            return totalOrdersContaining;
        }

        public void setTotalOrdersContaining(int totalOrdersContaining) {
            this.totalOrdersContaining = totalOrdersContaining;
        }
    }

    public static class MonthlyAnalytics {
        private String month; // Format: "YYYY-MM"
        private double revenue;
        private double profit;
        private int orders;

        public MonthlyAnalytics() {
        }

        public MonthlyAnalytics(String month, double revenue, double profit, int orders) {
            this.month = month;
            this.revenue = revenue;
            this.profit = profit;
            this.orders = orders;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public double getRevenue() {
            return revenue;
        }

        public void setRevenue(double revenue) {
            this.revenue = revenue;
        }

        public double getProfit() {
            return profit;
        }

        public void setProfit(double profit) {
            this.profit = profit;
        }

        public int getOrders() {
            return orders;
        }

        public void setOrders(int orders) {
            this.orders = orders;
        }
    }

    public static class DailyAnalytics {
        private String date; // Format: "YYYY-MM-DD"
        private double revenue;
        private int orders;

        public DailyAnalytics() {
        }

        public DailyAnalytics(String date, double revenue, int orders) {
            this.date = date;
            this.revenue = revenue;
            this.orders = orders;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public double getRevenue() {
            return revenue;
        }

        public void setRevenue(double revenue) {
            this.revenue = revenue;
        }

        public int getOrders() {
            return orders;
        }

        public void setOrders(int orders) {
            this.orders = orders;
        }
    }

}
