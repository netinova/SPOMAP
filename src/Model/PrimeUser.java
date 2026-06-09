package Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

public class PrimeUser extends User {

    private String  memberShipID;
    private double creditAmount;//bestancari
    private double debitAmount;//bedehcari

    public PrimeUser() {
        super();
    }


    public PrimeUser(String userId, String phoneNumber,String hashedPassword ,String firstName, String lastName,
                     String registerDate, double balance, String  memberShipID, double creditAmount, double debitAmount) {
        super(userId, phoneNumber, hashedPassword, firstName, lastName, registerDate, balance);
        this.memberShipID = memberShipID;
        this.creditAmount = creditAmount;
        this.debitAmount = debitAmount;
    }


    // getters
    public String  getMemberShipID() {
        return memberShipID;
    }

    // setter
    public void setMemberShipID(String  memberShipID) {
        this.memberShipID = memberShipID;
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

    public void addCredit(double amount) {
        this.creditAmount += amount;
        System.out.println("Added $" + amount + " to credit");
    }

    public void addDebit(double amount) {
        this.debitAmount += amount;
        System.out.println("Added $" + amount + " to Debit");
    }

    @Override
    @JsonIgnore
    public String getTypeUser() {
        return "PRIME";
    }

    @Override
    @JsonIgnore
    public boolean canPurchase(double totalAmount) {
        return creditAmount - debitAmount + balance >= totalAmount;
    }

}
