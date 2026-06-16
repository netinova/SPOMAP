package Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

public class AdminUser extends User {

    public AdminUser() {
        super();
    }

    public AdminUser(String userId, String phoneNumber, String hashedPassword, String firstName, String lastName, String  registerDate,
            double balance) {
        super(userId, phoneNumber, hashedPassword,firstName, lastName, registerDate, balance);
    }

    @Override
    @JsonIgnore
    public boolean canPurchase(double amount) {
        return true;
    }
}
