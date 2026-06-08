package Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

public class NormalUser extends User {

    public NormalUser() {
        super();
    }

    // new User
    public NormalUser(String phoneNumber, String firstName, String lastName, String password) {
        super(phoneNumber, firstName, lastName, password, null, 0.0);
    }

    // read User
    public NormalUser(String userId, String phoneNumber, String firstName, String lastName, LocalDateTime registerDate,
            double balance) {
        super(userId, phoneNumber, firstName, lastName, registerDate, balance);
    }

    @Override
    public String getTypeUser() {
        return "NORMAL";
    }

    @Override
    public boolean canPurchase(double totalAmount) {
        return totalAmount <= balance;
    }

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
        System.out.println("can't do this");
        return false;
    }

}
