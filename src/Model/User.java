package Model;

import java.time.LocalDateTime;

public abstract class User {
    protected String userId;
    protected String phoneNumber;
    protected String firstName;
    protected String lastName;
    protected String password;

    protected LocalDateTime registerDate;

    protected double balance;
    protected double totalAmount;

    //for add new user
    public User(String phoneNumber, String firstName, String lastName, String password) {
//        this.userId = generateId();
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registerDate = LocalDateTime.now();
        this.password = password;
        this.balance = 0;
        this.totalAmount=0;
    }

    //for load user
    public User(String userId, String phoneNumber, String firstName, String lastName, LocalDateTime registerDate, double balance) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registerDate = registerDate;
        this.totalAmount=0;
        this.balance=balance;
    }

    //for JSOM
    public User() {
    }

    //getters

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

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }

    public double getBalance() {
        return balance;
    }

    //setter
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    //abstract method
    public abstract String getTypeUser();
    public abstract boolean canPurchase(double totalAmount);

    //other methods
    public void addBalance(double amount) {
        this.balance += amount;
    }

    public void deductBalance(double amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
        }
    }

}
