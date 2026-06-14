package Service;

import Model.AdminUser;
import Model.NormalUser;
import Model.PrimeUser;
import Model.User;
import Model.UserLists;
import Model.UserLists.UserAdminList;
import Model.UserLists.UserNormalList;
import Model.UserLists.UserPrimeList;
import Util.PasswordHasher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<NormalUser> normalUsers;
    private List<PrimeUser> primeUsers;
    private List<AdminUser> adminUsers;
    private ObjectMapper mapper;

    private User loggedInUser;

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        System.out.println("logged in user set: " + loggedInUser.getFirstName());
    }

    public UserService() {
        this.normalUsers = new ArrayList<>();
        this.primeUsers = new ArrayList<>();
        this.adminUsers = new ArrayList<>();

        this.mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        loadNormalUser();
        loadPrimeUser();
        loadAdminUser();
    }

    // loader
    private void loadNormalUser() {
        File file = new File("database/normal_users.json");
        System.out.println("Loading normal users from: " + file.getAbsolutePath());
        if (!file.exists())
            return;
        try {
            UserNormalList users = mapper.readValue(file, UserNormalList.class);
            if (users != null && users.getUsers() != null) {
                this.normalUsers = users.getUsers();
                System.out.println("loaded normal user");
            } else {
                System.out.println("can't load normal user");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPrimeUser() {
        File file = new File("database/prime_users.json");
        if (!file.exists())
            return;
        try {
            UserPrimeList users = mapper.readValue(file, UserPrimeList.class);
            if (users != null && users.getUsers() != null) {
                this.primeUsers = users.getUsers();
                System.out.println("loaded prime user");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAdminUser() {
        File file = new File("database/admin_users.json");
        if (!file.exists())
            return;
        try {
            UserAdminList users = mapper.readValue(file, UserAdminList.class);
            if (users != null && users.getUsers() != null) {
                this.adminUsers = users.getUsers();
                System.out.println("loaded admin user");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // save JSON
    public void saveNormalUser() {
        File file = new File("database/normal_users.json");
        file.getParentFile().mkdirs();

        UserNormalList user = new UserNormalList(normalUsers);
        try {
            mapper.writeValue(file, user);
            System.out.println("save info normalUser");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void savePrimeUser() {
        File file = new File("database/prime_users.json");
        file.getParentFile().mkdirs();

        UserPrimeList user = new UserPrimeList(primeUsers);
        try {
            mapper.writeValue(file, user);
            System.out.println("save info PrimeUser");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // generate new id
    public String getNewId() {
        int maxId = 0;

        // check normal user
        for (NormalUser user : normalUsers) {
            String id = user.getUserId();
            if (user != null) {
                int idNumber = Integer.parseInt(id.substring(4));
                if (idNumber > maxId)
                    maxId = idNumber;
            }
        }

        // check prime user
        for (PrimeUser user : primeUsers) {
            String id = user.getUserId();
            if (user != null) {
                int idNumber = Integer.parseInt(id.substring(4));
                if (idNumber > maxId)
                    maxId = idNumber;
            }
        }

        int nextId = maxId + 1;
        return "USR_" + String.format("%06d", nextId);
    }

    private String getNewMemberShipCode() {
        int maxId = 0;

        for (PrimeUser user : primeUsers) {
            String id = user.getMemberShipID();
            if (user != null) {
                int idNumber = Integer.parseInt(id.substring(4));
                if (idNumber > maxId)
                    maxId = idNumber;
            }
        }

        int nextCode = maxId + 1;
        return "PRM_" + String.format("%06d", nextCode);
    }

    // operation login signUP
    public boolean registerNormalUser(String firstName, String lastName, String phoneNumber, String hashedPassword) {
        if (searchUserByPhoneNumber(phoneNumber) != null)
            return false;

        NormalUser user = new NormalUser(null,phoneNumber,hashedPassword,firstName,lastName,null,0.0);

        String newId = getNewId();
        user.setUserId(newId);

        normalUsers.add(user);
        saveNormalUser();
        return true;
    }

    public User login(String phoneNumber, String password) {
        User user = searchUserByPhoneNumber(phoneNumber);
        if (user == null) {
            System.out.println("user not founded");
            return null;
        }

        String userType = "";
        String mainPassword = "";
        if (user instanceof NormalUser) {
            userType = "NORMAL";
            mainPassword = ((NormalUser) user).getPassword();
        } else if (user instanceof PrimeUser) {
            userType = "PRIME";
            mainPassword = ((PrimeUser) user).getPassword();
        } else if (user instanceof AdminUser) {
            userType = "ADMIN";
            mainPassword = ((AdminUser) user).getPassword();
        }

        if (PasswordHasher.checkerPassword(password, mainPassword)) {
            System.out.println("successfully login as " + userType);
            return user;
        } else {
            System.out.println("Unsuccessful login");
            return null;
        }
    }

    private User searchUserByPhoneNumber(String phoneNumber) {
        for (NormalUser normal : normalUsers)
            if (normal.getPhoneNumber().equals(phoneNumber))
                return normal;

        for (PrimeUser prime : primeUsers)
            if (prime.getPhoneNumber().equals(phoneNumber))
                return prime;

        for (AdminUser admin : adminUsers)
            if (admin.getPhoneNumber().equals(phoneNumber))
                return admin;
        return null;
    }

    public boolean convertNormalUserToPrime(String phonNumber) {
        Object user = searchUserByPhoneNumber(phonNumber);
        if (user == null || !(user instanceof NormalUser)) {
            System.out.println("user normal not found");
            return false;
        }
        NormalUser normalUser = (NormalUser) user;
        PrimeUser primeUser = new PrimeUser(
                null,
                normalUser.getPhoneNumber(),
                normalUser.getPassword(),
                normalUser.getFirstName(),
                normalUser.getLastName(),
                null,
                normalUser.getBalance(),
                null,
                0,0);

        primeUser.setRegisterDate(normalUser.getRegisterDate());
        primeUser.setUserId(normalUser.getUserId());

        String newId = getNewMemberShipCode();
        primeUser.setMemberShipID(newId);

        this.normalUsers.remove(normalUser);
        this.primeUsers.add(primeUser);

        savePrimeUser();
        saveNormalUser();

        return true;
    }

    // get all normal users
    public List<NormalUser> getAllNormalUsers() {
        return normalUsers;
    }

    // get all prime users
    public List<PrimeUser> getAllPrimeUsers() {
        return primeUsers;
    }
}
