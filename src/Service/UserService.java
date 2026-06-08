package Service;

import Model.AdminUser;
import Model.NormalUser;
import Model.PrimeUser;
import Util.PasswordHasher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<NormalUser> normalUsers;
    private List<PrimeUser> primeUsers;
    private List<AdminUser> adminUsers;
    private ObjectMapper mapper;

    private static final String ADMIN_USERS_FILE = "database/admin_users.json";


    public UserService() {
        this.normalUsers = new ArrayList<>();
        this.primeUsers = new ArrayList<>();
        this.adminUsers = new ArrayList<>();

        this.mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.registerModule(new JavaTimeModule());

        loadNormalUser();
        loadPrimeUser();
        LoadAdminUser();
    }

    // loader
    private void loadNormalUser() {
        File file = new File("database/normal_users.json");
    }

    private void loadPrimeUser() {
        File file = new File("database/prime_users.json");

    }

    private void LoadAdminUser() {
        File file = new File("database/admin_users.json");
    }

    // save JSON
    public void saveNormalUser(){
            File file = new File("database/normal_users.json");
            file.getParentFile().mkdirs();

            UserNormalList user = new UserNormalList(normalUsers);
        try {
            mapper.writeValue(file,user);
            System.out.println("save info normalUser");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void savePrimeUser(){
        File file = new File("database/Prime_users.json");
        file.getParentFile().mkdirs();

        UserPrimeList user = new UserPrimeList(primeUsers);
        try {
            mapper.writeValue(file,user);
            System.out.println("save info PrimeUser");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean registerNormalUser(String firstName, String lastName, String phoneNumber, String hashedPassword){
        if (searchUserByPhoneNumber(phoneNumber)!=null)
            return false;

        NormalUser user = new NormalUser(phoneNumber,firstName,lastName,hashedPassword);
        normalUsers.add(user);
        saveNormalUser();
        return true;
    }

    public Object login(String phoneNumber , String password){
        Object user = searchUserByPhoneNumber(phoneNumber);
        if (user== null){
            System.out.println("user not founded");
            return null;
        }

        String userType = "";
        String mainPassword = "";
        if (user instanceof NormalUser){
            userType="NORMAL";
            mainPassword = ((NormalUser) user).getPassword();
        } else if (user instanceof PrimeUser){
            userType="PRIME";
            mainPassword = ((PrimeUser) user).getPassword();
        } else if (user instanceof AdminUser){
            userType="NORMAL";
            mainPassword = ((AdminUser) user).getPassword();
        }

        if (PasswordHasher.checkerPassword(password , mainPassword)){
            System.out.println("successfully login as "+ userType);
            return user;
        }
        else{
            System.out.println("Unsuccessful login");
            return null;
        }
    }

    private Object searchUserByPhoneNumber(String phoneNumber) {
        for (NormalUser normal:normalUsers)
            if(normal.getPhoneNumber().equals(phoneNumber))
                return normal;

        for (PrimeUser prime: primeUsers)
            if (prime.getPhoneNumber().equals(phoneNumber))
                return prime;

        for (AdminUser admin: adminUsers)
            if(admin.getPhoneNumber().equals(phoneNumber))
                return admin;
        return null;
    }

    public boolean convertNormalUserToPrime(String phonNumber){
        Object user = searchUserByPhoneNumber(phonNumber);
        if (user== null || !(user instanceof NormalUser)){
            System.out.println("user normal not found");
            return false;
        }
        NormalUser normalUser = (NormalUser) user;
        PrimeUser primeUser = new PrimeUser(
                normalUser.getPhoneNumber(),
                normalUser.getFirstName(),
                normalUser.getLastName(),
                normalUser.getPassword(),
                0,0
                );

        primeUser.setRegisterDate(normalUser.getRegisterDate());
        primeUser.setUserId(normalUser.getUserId());

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

    static class UserNormalList{
        private List<NormalUser> users;
        public UserNormalList() {
        }
        public UserNormalList(List<NormalUser> users) {
            this.users = users;
        }

        public List<NormalUser> getUsers() {
            return users;
        }

        public void setUsers(List<NormalUser> users) {
            this.users = users;
        }
    }

    static class UserPrimeList{
        private List<PrimeUser> users;
        public UserPrimeList() {
        }
        public UserPrimeList(List<PrimeUser> users) {
            this.users = users;
        }

        public List<PrimeUser> getUsers() {
            return users;
        }

        public void setUsers(List<PrimeUser> users) {
            this.users = users;
        }
    }

    static class UserAdminList{
        private List<AdminUser> users;
        public UserAdminList() {
        }
        public UserAdminList(List<AdminUser> users) {
            this.users = users;
        }

        public List<AdminUser> getUsers() {
            return users;
        }

        public void setUsers(List<AdminUser> users) {
            this.users = users;
        }
    }
}
