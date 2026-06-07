package Model;

import java.time.LocalDateTime;

public class PrimeUser extends User {
    private int memberShipID;
    private double creditAmount;//bestancari
    private double debitAmount;//bedehcari
    private double balance;

    public PrimeUser(String phoneNumber, String firstName, String lastName, String password, int memberShipID, double creditAmount, double debitAmount) {
        super(phoneNumber, firstName, lastName, password);
        this.memberShipID = memberShipID;
        this.creditAmount = creditAmount;
        this.debitAmount = debitAmount;
        this.balance = 0;
    }

    public PrimeUser(String userId, String phoneNumber, String firstName, String lastName, LocalDateTime registerDate, double balance, int memberShipID, double creditAmount, double debitAmount) {
        super(userId, phoneNumber, firstName, lastName, registerDate, balance);
        this.memberShipID = memberShipID;
        this.creditAmount = creditAmount;
        this.debitAmount = debitAmount;
    }

    // getters
    public int getMemberShipID() {
        return memberShipID;
    }

    public double getBalance() {
        return balance;
    }

    // setter
    public void setMemberShipID(int memberShipID) {
        this.memberShipID = memberShipID;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // other methods
    public void depositCash(double amount) {
        addBalance(amount);
        System.out.println("add to balance $" + amount);
    }

    public boolean withdrawCash(double amount) {
        if (balance >= amount) {
            deductBalance(amount);
            System.out.println("remove from balance $" + amount);
            return true;
        }
        else {
            debitAmount-=amount;
            System.out.println("remove from debitAmount $" + amount);
            return true;
        }
    }


    @Override
    public String getTypeUser() {
        return "PRIME";
    }

    @Override
    public boolean canPurchase(double totalAmount) {
        return creditAmount - debitAmount + balance >= totalAmount;
    }

    @Override
    public double getAvailableBalance() {
        return creditAmount - debitAmount + balance;
    }
}
