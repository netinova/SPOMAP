package Model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import static Util.PasswordHasher.hashingPassword;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class User {
    protected String userId;
    protected String phoneNumber;
    protected String firstName;
    protected String lastName;
    protected String password;
    protected String registerDate;

    protected double balance;
    protected double totalAmount;

    protected UserType userType;

    // for load user
    public User(String userId, String phoneNumber, String hashedPassword, String firstName, String lastName,
            String registerDate,
            double balance) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registerDate = (registerDate == null) ? LocalDateTime.now().toString() : registerDate;
        this.totalAmount = 0;
        password = hashedPassword;
        this.balance = balance;
    }

    // for JSON
    public User() {
    }

    // getters

    public String getUserId() {
        return userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public String getPassword() {
        return password;
    }

    @JsonIgnore
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public double getBalance() {
        return balance;
    }

    public UserType getUserType() {
        return userType;
    }

    // setter
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public void editProfile(String fName, String lName, String phoneNumber, String password){
        this.firstName=fName;
        this.lastName=lName;
        this.phoneNumber=phoneNumber;//TODO: check num duplicate
        this.password=(password.isEmpty())? this.password : hashingPassword(password);
    }

    @JsonIgnore
    public abstract boolean canPurchase(double totalAmount);

    // other methods
    public void addBalance(double amount) {
        this.balance += amount;
    }

    public void deductBalance(double amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
        }
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

}
