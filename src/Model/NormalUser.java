package Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public class NormalUser extends User {

    public NormalUser() {
        super();
        this.userType = UserType.NORMAL;
    }

    // read User
    public NormalUser(String userId, String phoneNumber, String hashedPassword, String firstName, String lastName,
            LocalDateTime registerDate,
            double balance) {
        super(userId, phoneNumber, hashedPassword, firstName, lastName, registerDate, balance);
        this.userType = UserType.NORMAL;
    }

    @Override
    @JsonIgnore
    public boolean canPurchase(double totalAmount) {
        return totalAmount <= balance;
    }

}
