package Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

public class NormalUser extends User {

    public NormalUser() {
        super();
    }

    // read User
    public NormalUser(String userId, String phoneNumber, String hashedPassword, String firstName, String lastName, String registerDate,
            double balance) {
        super(userId, phoneNumber, hashedPassword, firstName, lastName, registerDate, balance);
    }

    @Override
    @JsonIgnore
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
