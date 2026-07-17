package Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public class PrimeUser extends User {

    private String memberShipID;
    private double creditAmount;// bestancari
    private double debitAmount;// bedehcari

    public PrimeUser() {
        super();
        this.userType = UserType.PRIME;
    }

    public PrimeUser(String userId, String phoneNumber, String hashedPassword, String firstName, String lastName,
            LocalDateTime registerDate, double balance, String memberShipID, double creditAmount, double debitAmount) {
        super(userId, phoneNumber, hashedPassword, firstName, lastName, registerDate, balance);
        this.memberShipID = memberShipID;
        this.creditAmount = creditAmount;
        this.debitAmount = debitAmount;
        this.userType = UserType.PRIME;
    }

    // getters
    public String getMemberShipID() {
        return memberShipID;
    }

    // setter
    public void setMemberShipID(String memberShipID) {
        this.memberShipID = memberShipID;
    }

    // other methods
    public void depositCash(double amount) {
        addBalance(amount);
        System.out.println("add to balance $" + amount);
    }

    public double getDebitAmount() {
        return debitAmount;
    }

    public double getCreditAmount() {
        return creditAmount;
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
    public boolean canPurchase(double totalAmount) {
        return true;
    }

    @Override
    public void addBalance(double amount) {
        if (debitAmount>=0){
            if (debitAmount>=amount)
                debitAmount-=amount;
            else{
                amount-= debitAmount;
                debitAmount=0;
                balance+=amount;
            }
            return;
        }
        super.addBalance(amount);
    }

    @Override
    public void deductBalance(double amount) {
        if (creditAmount>=amount){
            creditAmount-=amount;
            amount=0;
            System.out.println("remove from credit amount $" + amount);
        }
        else{
            amount-=creditAmount;
            creditAmount=0;
            System.out.println("remove from credit amount $" + amount);
        }
        if (amount==0) return;

        if (balance >= amount) {
            deductBalance(amount);
            System.out.println("remove from balance $" + amount);
        } else {
            amount-=balance;
            balance=0;
            debitAmount += amount;
            System.out.println("remove from debitAmount/balance $" + amount);
        }
    }
}