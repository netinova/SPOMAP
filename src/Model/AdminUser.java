package Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminUser extends User{

    public AdminUser() {
        super();
    }

    public AdminUser(String userId, String phoneNumber, String firstName, String lastName, LocalDateTime registerDate, double balance) {
        super(userId, phoneNumber, firstName, lastName, registerDate, balance);
    }

    @Override
    public String getTypeUser() {
        return "ADMIN";
    }

    @Override
    public boolean canPurchase(double amount) {
        return true;
    }
}
