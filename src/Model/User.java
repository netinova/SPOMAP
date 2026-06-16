package Model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    // for load user
    public User(String userId, String phoneNumber,String hashedPassword, String firstName, String lastName, String registerDate,
            double balance) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registerDate = (registerDate == null) ? LocalDateTime.now().toString() : registerDate;
        this.totalAmount = 0;
        password=hashedPassword;
        this.balance =balance;
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

    public String  getRegisterDate() {
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

    // setter
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRegisterDate(String  registerDate) {
        this.registerDate = registerDate;
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

}
