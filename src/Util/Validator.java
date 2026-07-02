package Util;

public class Validator {

    // Regex patterns
    public static final String PHONE_REGEX = "^09\\d{9}$";
    public static final String NAME_REGEX = "^[a-zA-Z\\s]{3,50}$";
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    public static final String DOUBLE_REGEX = "^\\d+(\\.\\d{1,2})?$";
    public static final String FILE_REGEX = "^database/pictures/[^/]+\\.(png|jpg|jpeg)$";

    public static class ValidationResult {
        private final boolean valid;

        private final String errorMessage;

        public ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }

        public boolean isValid() {
            return valid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    public static ValidationResult validatePhone(String phone) {
        if (phone == null || phone.isEmpty() || phone.equals("09xxxxxxxxx") || phone.equals("Username / Phone number")) {
            return new ValidationResult(false, "Phone number is required");
        }
        if (!phone.matches(PHONE_REGEX)) {
            return new ValidationResult(false, "Enter a valid phone number (Ex: 09123456789)");
        }
        return new ValidationResult(true, null);
    }

    public static ValidationResult validateFirstName(String name) {
        if (name == null || name.isEmpty() || name.equals("First Name")) {
            return new ValidationResult(false, "First name is required");
        }
        if (!name.matches(NAME_REGEX)) {
            return new ValidationResult(false, "First name should be 2-50 character only");
        }
        return new ValidationResult(true, null);
    }

    public static ValidationResult validateLastName(String name) {
        if (name == null || name.isEmpty() || name.equals("Last Name")) {
            return new ValidationResult(false, "Last name is required");
        }
        if (!name.matches(NAME_REGEX)) {
            return new ValidationResult(false, "Last name should be 2-50 character only");
        }
        return new ValidationResult(true, null);
    }

    public static ValidationResult validatePassword(String password) {
        if (password == null || password.isEmpty() || password.equals("Password")) {
            return new ValidationResult(false, "Password is required");
        }
        if (password.length() < 6) {
            return new ValidationResult(false, "Password must be at least 6 characters");
        }
        if (!password.matches(PASSWORD_REGEX)) {
            return new ValidationResult(false, "Password must contain at least 1 digit, 1 lowercase, 1 uppercase");
        }
        return new ValidationResult(true, null);
    }

    public static ValidationResult validateLoginPassword(String password) {
        if (password == null || password.isEmpty() || password.equals("Password")) {
            return new ValidationResult(false, "Please confirm your password");
        }
        return new ValidationResult(true, null);
    }

    public static ValidationResult validateConfirmPassword(String password, String confirmPassword) {
        if (confirmPassword == null || confirmPassword.isEmpty() || confirmPassword.equals("Repeat Password")) {
            return new ValidationResult(false, "Please confirm your password");
        }
        if (!password.equals(confirmPassword)) {
            return new ValidationResult(false, "Passwords do not match");
        }
        return new ValidationResult(true, null);
    }

    public static ValidationResult validateFindUs(String findUs) {
        if (findUs == null || findUs.isEmpty() || findUs.equals("Select an option")) {
            return new ValidationResult(false, "Please tell us how you found us");
        }
        return new ValidationResult(true, null);
    }

    public static ValidationResult validationDouble(String number) {
        if (number == null || number.isEmpty() || number.equals("Enter amount")) {
            return new ValidationResult(false, "Amount is required");
        }
        if (!number.matches(DOUBLE_REGEX))
            return new ValidationResult(false, "Enter a valid format");
        return new ValidationResult(true, null);
    }

    public static ValidationResult validationName(String name) {
        if (name == null || name.isEmpty() || name.equals("Product name") || name.equals("e.g. Mass")) {
            return new ValidationResult(false, "Query is required");
        }
        if (!name.matches(NAME_REGEX)) {
            return new ValidationResult(false, "Query should be 2-50 character only");
        }
        return new ValidationResult(true, null);
    }

    public static ValidationResult validationQueryEmpty(String name, String pattern) {
        if (name == null || name.isEmpty() || name.equals(pattern)) {
            return new ValidationResult(false, "Query is required");
        }
        return new ValidationResult(true, null);
    }

    public static ValidationResult validationImageAddress(String name) {
        if (name == null || name.isEmpty() || name.equals("database/pictures/...")) {
            return new ValidationResult(false, "Image Address is required");
        }
        if (!name.matches(FILE_REGEX)) {
            return new ValidationResult(false, "Invalid format for Address");
        }
        return new ValidationResult(true, null);
    }
}
