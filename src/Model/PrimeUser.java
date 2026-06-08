package Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PrimeUser extends User {
    private String  memberShipID;
    private double creditAmount;//bestancari
    private double debitAmount;//bedehcari

    public PrimeUser() {
        super();
    }

    //add new Prime user
    public PrimeUser(String phoneNumber, String firstName, String lastName, String password, double creditAmount, double debitAmount) {
        super(phoneNumber, firstName, lastName, password);
        this.memberShipID ="";
        this.creditAmount = creditAmount;
        this.debitAmount = debitAmount;
    }

    public PrimeUser(String userId, String phoneNumber, String firstName, String lastName, LocalDateTime registerDate, double balance, String  memberShipID, double creditAmount, double debitAmount) {
        super(userId, phoneNumber, firstName, lastName, registerDate, balance);
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
    public String getTypeUser() {
        return "PRIME";
    }

    @Override
    public boolean canPurchase(double totalAmount) {
        return creditAmount - debitAmount + balance >= totalAmount;
    }

}
