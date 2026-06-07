package Model;

import java.time.LocalDateTime;

public class AdminUser extends User{
    public AdminUser(String userId, String phoneNumber, String firstName, String lastName, LocalDateTime registerDate ,String password) {
        super(firstName, lastName, phoneNumber, password);
    }

    @Override
    public String getTypeUser() {
        return "ADMIN";
    }

    @Override
    public boolean canPurchase(double amount) {
        return true;
    }

    @Override
    public double getAvailableBalance() {
        return 0;
    }
}
