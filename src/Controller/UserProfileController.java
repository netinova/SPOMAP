package Controller;

import Components.AddProductPanel;
import Model.*;
import Service.AnalyticsService;
import Service.ProductService;
import Service.UserService;
import Util.PasswordHasher;
import Util.Validator;
import View.UserProfileView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfileController {

    private UserProfileView view;
    private OnChangeViewListener listener;
    private ProductCatalog productCatalog;

    private AnalyticsService analyticsService;

    public UserProfileController(ProductCatalog productCatalog, AnalyticsService analyticsService) {
        this.productCatalog = productCatalog;
        this.analyticsService = analyticsService;
    }

    public void setView(UserProfileView view) {
        this.view = view;
    }

    public void setOnChangeViewListener(OnChangeViewListener listener) {
        this.listener = listener;
    }

    public void loadProfile() {
        User user = AppState.getInstance().getLoggedInUser();
        if (user == null || view == null)
            return;

        int cartItems = 0;
        if (AppState.getInstance().getCart() != null)
            cartItems = AppState.getInstance().getCart().getItems().size();

        view.displayUser(
                user.getFullName(),
                user.getUserType().getDisplayName(),
                user.getBalance(),
                cartItems,
                user.getUserType());

        if (user.getUserType() == UserType.PRIME) {
            PrimeUser userPrime = (PrimeUser) user;
            view.displayPrimeUser(
                    userPrime.getCreditAmount(),
                    userPrime.getDebitAmount(),
                    userPrime.getMemberShipID());

        }
    }

    // -------------------- Logout
    public void handleLogout() {
        AppState.getInstance().setLoggedInUser(null);
        AppState.getInstance().setCart(null);
        if (listener != null)
            listener.changeView(ViewType.AUTH.getViewId());
    }

    // ---------------- EditProfile methods --------------------

    // validation methods
    // View sends user input to controller, controller validates and returns result
    public Validator.ValidationResult validatePhoneNumber(String phone) {
        return Validator.validatePhone(phone);
    }

    public Validator.ValidationResult validateFirstName(String firstName) {
        return Validator.validateFirstName(firstName);
    }

    public Validator.ValidationResult validateLastName(String lastName) {
        return Validator.validateLastName(lastName);
    }

    public Validator.ValidationResult validatePassword(String password) {
        return Validator.validatePassword(password.trim());
    }

    public Validator.ValidationResult validateConfirmPassword(String password, String confirmPassword) {
        return Validator.validateConfirmPassword(password, confirmPassword);
    }

    public Validator.ValidationResult validationName(String name) {
        return Validator.validationName(name);
    }

    public Validator.ValidationResult validationQuery(String name, String pattern) {
        return Validator.validationQueryEmpty(name, pattern);
    }

    public Validator.ValidationResult validationFileAddress(String name) {
        return Validator.validationImageAddress(name);
    }

    public Validator.ValidationResult validationDate(String date) {
        return Validator.validationDate(date);
    }

    public boolean fullValidator(String fName, String lName, String phoneNumber, String currentPassword,
            String newPassword, String confirmPassword) {
        Validator.ValidationResult result;
        int temp = 0;
        User user = AppState.getInstance().getLoggedInUser();

        result = validatePhoneNumber(phoneNumber);
        if (!result.isValid()) {
            if (view != null)
                view.showPhoneError(result.getErrorMessage());
            temp++;
        }

        result = validateFirstName(fName);
        if (!result.isValid()) {
            if (view != null)
                view.showFirstNameError(result.getErrorMessage());
            temp++;
        }

        result = validateLastName(lName);
        if (!result.isValid()) {
            if (view != null)
                view.showLastNameError(result.getErrorMessage());
            temp++;
        }

        // password
        if (currentPassword == "" && (newPassword != "" || confirmPassword != "")) {
            if (view != null)
                view.showCurrentPasswordError("Your password is incorrect");
            temp++;
        } else if (currentPassword != "") {
            boolean passwordResult = PasswordHasher.checkerPassword(currentPassword, user.getPassword());
            result = validatePassword(newPassword);
            if (!result.isValid())
                temp++;
            view.showNewtPasswordError(result.getErrorMessage());
            result = validateConfirmPassword(newPassword, confirmPassword);
            if (!result.isValid())
                temp++;
            view.showConfirmPasswordError(result.getErrorMessage());
            if (!passwordResult) {
                if (view != null)
                    view.showCurrentPasswordError("Your password is incorrect");
                temp++;
            }
        } else if (view != null) {
            view.showCurrentPasswordError("");
            view.showConfirmPasswordError("");
            view.showNewtPasswordError("");
        }

        if (temp != 0)
            return false;
        return true;
    }

    public void showShoppingCart() {
        if (listener != null)
            listener.changeView(ViewType.SHOPPING_CART.getViewId());
    }

    public boolean editProfileHandler(String fName, String lName, String phoneNumber, String newPassword) {
        User user = AppState.getInstance().getLoggedInUser();
        user.editProfile(fName, lName, phoneNumber, newPassword);
        if (user.getUserType().isAdmin())
            UserService.saveAdminUser(AppState.getInstance().adminUsersList);
        else if (user.getUserType().isPrime())
            UserService.savePrimeUser(AppState.getInstance().primeUsersList);
        else
            UserService.saveNormalUser(AppState.getInstance().normalUsersList);
        return true;
    }

    public void loadEditProfile() {
        User user = AppState.getInstance().getLoggedInUser();
        if (user == null || view == null)
            return;
        view.loadEditUserData(
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber());
    }
    // listener edit profile

    public void onPhoneNumberChange(String value) {
        System.out.println("Phone changed: " + value);
    }

    public void onFirstNameChange(String value) {
        System.out.println("First name changed: " + value);
    }

    public void onLastNameChange(String value) {
        System.out.println("Last name changed: " + value);
    }

    public void onPasswordCurrentChange(String newValue) {
        System.out.println("Password current change");
    }

    public void onPasswordChange(String value) {
        System.out.println("Password changed");
    }

    public void onConfirmPasswordChange(String value) {
        System.out.println("Confirm password changed");
    }

    // --------------- Charge wallet ------------------

    public void loadChargeWalletData() {
        User user = AppState.getInstance().getLoggedInUser();
        if (user == null || view == null)
            return;
        view.loadChargeWalletData(String.format("%.2f", user.getBalance()));
    }

    public Validator.ValidationResult validateDouble(String amount) {
        return Validator.validationDouble(amount);
    }
    // listener Charging

    public void onAmountChange(String value) {
        System.out.println("Amount changed: " + value);
    }

    public void onChargeButtonClick(String balance) {
        User user = AppState.getInstance().getLoggedInUser();
        user.addBalance(Double.parseDouble(balance));

        if (user.getUserType().isPrime())
            UserService.savePrimeUser(AppState.getInstance().primeUsersList);
        else
            UserService.saveNormalUser(AppState.getInstance().normalUsersList);

        loadProfile();
        showMainPage();
    }

    public void onCancelClick() {
        showMainPage();
    }
    // Mange Users

    public void showSearchUser() {
        view.loadManageUsers();
    }

    public void handelSearchUser(String phoneNumberString) {
        User user = UserService.searchUserByPhoneNumber(phoneNumberString, AppState.getInstance().normalUsersList,
                AppState.getInstance().primeUsersList, AppState.getInstance().adminUsersList);
        if (user == null)
            return;

        String memberShipCode = null;
        double creditAmount = 0;
        double debitAmount = 0;
        if (user.getUserType().isPrime()) {
            PrimeUser primeUser = (PrimeUser) user;
            memberShipCode = primeUser.getMemberShipID();
            creditAmount = primeUser.getCreditAmount();
            debitAmount = primeUser.getDebitAmount();
        }

        view.loadInformationUser(user.getFirstName(), user.getLastName(), user.getPhoneNumber(),
                user.getUserId(), user.getUserType().getDisplayName(), user.getRegisterDate(),
                memberShipCode, creditAmount, debitAmount);

    }

    public boolean statusSearchPhoneNumber(String phoneNumberString) {
        User user = UserService.searchUserByPhoneNumber(phoneNumberString, AppState.getInstance().normalUsersList,
                AppState.getInstance().primeUsersList, AppState.getInstance().adminUsersList);

        return user != null;
    }

    public void handleUpgradeToPrime(String phoneNumber) {
        boolean status = UserService.convertNormalUserToPrime(phoneNumber,
                AppState.getInstance().normalUsersList,
                AppState.getInstance().primeUsersList,
                AppState.getInstance().adminUsersList);
        if (status) {
            UserService.saveNormalUser(AppState.getInstance().normalUsersList);
            UserService.savePrimeUser(AppState.getInstance().primeUsersList);
        }
    }

    public void handleKickUser(String phonNumber) {
        User user = UserService.searchUserByPhoneNumber(phonNumber, AppState.getInstance().normalUsersList,
                AppState.getInstance().primeUsersList,
                AppState.getInstance().adminUsersList);
        if (user == null || user.getUserType().isAdmin())
            return;
        if (user.getUserType().getDisplayName().equals("Normal User")) {
            NormalUser normalUser = (NormalUser) user;
            AppState.getInstance().normalUsersList.removeUser(normalUser);
            UserService.saveNormalUser(AppState.getInstance().normalUsersList);
            return;
        } else {
            PrimeUser primeUser = (PrimeUser) user;
            AppState.getInstance().primeUsersList.removeUser(primeUser);
            UserService.savePrimeUser(AppState.getInstance().primeUsersList);
            return;
        }
    }

    // add product
    public void handleAddProduct(AddProductPanel panel) {
        if (panel == null || productCatalog == null)
            return;

        // validation
        String name = panel.getProductName();
        String priceStr = panel.getPriceText();
        String discountStr = panel.getDiscountText();

        int temp = 0;

        var result = Validator.validationQueryEmpty(name, "Product name");
        if (!result.isValid()) {
            panel.showNameError(result.getErrorMessage());
            temp++;
        }

        var priceResult = Validator.validationDouble(priceStr);
        if (!priceResult.isValid()) {
            panel.showPriceError(priceResult.getErrorMessage());
            temp++;
        }

        double discount = 0;
        result = validationQuery(discountStr, "0 - 100");
        if (result.isValid()) {
            var discResult = Validator.validationDouble(discountStr);
            if (!discResult.isValid()) {
                panel.showDiscountError("Enter a valid discount (0-100)");
                temp++;
            } else {
                discount = Double.parseDouble(discountStr);
                if (discount < 0 || discount > 100) {
                    panel.showDiscountError("Must be between 0 and 100");
                    temp++;
                }
            }
        } else
            panel.showDiscountError(result.getErrorMessage());

        if (temp != 0)
            return;

        // build product
        Product product = new Product();
        product.setId(ProductService.generateProductId(productCatalog));
        product.setName(name);
        product.setPrice(Double.parseDouble(priceStr));
        product.setDiscount(discount);
        product.setManufacturer(
                (panel.getManufacturer().isEmpty() || panel.getManufacturer().equals("Manufacturer")) ? null
                        : panel.getManufacturer());
        product.setThumbnail((panel.getProductImages().length == 0) ? null : panel.getProductImages()[0]);
        product.setProductImages((panel.getProductImages().length == 0) ? null : panel.getProductImages());
        product.setDescription(
                (panel.getDescription().equals("Explain about product") || panel.getDescription().isEmpty()) ? null
                        : panel.getDescription());
        product.setTechnicalSpecs(panel.getTechnicalSpecs());
        if (panel.getSelectedColors().length == 0) {
            ProductColor[] productColor = { ProductColor.Default };
            product.setColors(productColor);
        } else
            product.setColors(panel.getSelectedColors());

        productCatalog.addProduct(product);
        ProductService.saveProducts(productCatalog);

        showMainPage();
    }

    // status Shop
    public int getCountPrimeUser() {
        return analyticsService.getPrimeUserCount();
    }

    public double getRevenue() {
        return analyticsService.getAnalytics().getTotalRevenue();
    }

    public double getProfit() {
        return analyticsService.getAnalytics().getTotalProfit();
    }

    public double getCustomer() {
        return analyticsService.getAnalytics().getTotalCustomers();
    }

    public int getOrders() {
        return analyticsService.getAnalytics().getTotalOrders();
    }

    public int getItemSold() {
        return analyticsService.getAnalytics().getTotalItemsSold();
    }

    public double getAverageOrder() {
        return analyticsService.getAnalytics().getAverageOrderValue();
    }

    public String getLastUpdate() {
        LocalDateTime date = analyticsService.getAnalytics().getLastUpdated();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return date.format(formatter);
    }

    public void handleMonthlyChart() {
        List<ShopAnalytics.MonthlyAnalytics> monthlyAnalytics = analyticsService.getAnalytics().getMonthlyAnalytics();
        List<String> date = new ArrayList<>();
        List<Double> revenue = new ArrayList<>();
        List<Double> profit = new ArrayList<>();
        for (ShopAnalytics.MonthlyAnalytics da : monthlyAnalytics) {
            date.add(da.getMonth());
            profit.add(da.getProfit());
            revenue.add(da.getRevenue());
        }
        view.setInfoMonthlyChart(date, revenue, profit);

    }

    public void handleDailyChart(String from, String to) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate dateFrom = LocalDate.parse(from, formatter);
        LocalDate dateTo = LocalDate.parse(to, formatter);

        ShopAnalytics analytics = analyticsService.getAnalyticsForDateRange(dateFrom.atStartOfDay(),
                dateTo.atTime(23, 59, 59));
        if (analytics == null)
            return;

        List<ShopAnalytics.DailyAnalytics> dailyAnalytics = analytics.getDailyAnalytics();
        List<String> date = new ArrayList<>();
        List<Double> revenue = new ArrayList<>();
        for (ShopAnalytics.DailyAnalytics da : dailyAnalytics) {
            date.add(da.getDate());
            revenue.add(da.getRevenue());
        }
        view.setInfoDailyChart(date, revenue);
    }

    public void handleProductChart() {
        List<ShopAnalytics.ProductAnalytics> productAnalytics = analyticsService.getAnalytics().getProductAnalytics();
        Map<String, Integer> productSales = new HashMap<>();
        for (ShopAnalytics.ProductAnalytics da : productAnalytics) {
            productSales.put(da.getProductName(), da.getTotalQuantitySold());
        }
        view.setProductInfo(productSales);
    }

    // listener
    public void onNameProductChange(String newValue) {
        System.out.println("name product: " + newValue);
    }

    public void onPriceProductChange(String string) {
        System.out.println("Price product: " + string);
    }

    public void onDiscountProductChange(String string) {
        System.out.println("Discount product: " + string);
    }

    public void onManufacturerProductChange(String string) {
        System.out.println("Manufacturer product: " + string);
    }

    public void onDescriptionProductChange(String string) {
        System.out.println("Description product: " + string);
    }

    public void onSaveClicked() {
        System.out.println("On save Clicked");

    }

    public void showMainPage() {
        view.showMainProfile();
    }

}
