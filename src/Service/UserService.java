package Service;

import Model.AdminUser;
import Model.NormalUser;
import Model.PrimeUser;
import Model.User;
import Model.UserLists.UserAdminList;
import Model.UserLists.UserNormalList;
import Model.UserLists.UserPrimeList;
import Util.LocalDateTimeDeserializer;
import Util.LocalDateTimeSerializer;
import Util.PasswordHasher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class UserService {

    private UserService() {
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        mapper.registerModule(module);
        return mapper;
    }

    // loader
    public static UserNormalList loadNormalUser() {

        ObjectMapper mapper = createObjectMapper();

        File file = new File("database/normal_users.json");
        if (!file.exists())
            return null;

        try {
            UserNormalList users = mapper.readValue(file, UserNormalList.class);
            if (users != null && users.getUsers() != null) {
                System.out.println("loaded normal user");
                return users;
            } else {
                System.out.println("can't load normal user");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static UserPrimeList loadPrimeUser() {

        ObjectMapper mapper = createObjectMapper();

        File file = new File("database/prime_users.json");

        if (!file.exists())
            return null;

        try {
            UserPrimeList users = mapper.readValue(file, UserPrimeList.class);

            if (users != null && users.getUsers() != null) {
                users.setUsers(users.getUsers());
                System.out.println("loaded prime user");
                return users;
            } else {
                System.out.println("can't load prime user");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UserAdminList loadAdminUser() {

        ObjectMapper mapper = createObjectMapper();

        File file = new File("database/admin_users.json");
        if (!file.exists())
            return null;

        try {
            UserAdminList users = mapper.readValue(file, UserAdminList.class);
            if (users != null && users.getUsers() != null) {
                System.out.println("loaded admin user");
                return users;
            } else {
                System.out.println("can't load admin user");
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // save JSON
    public static void saveNormalUser(UserNormalList normalUsers) {

        ObjectMapper mapper = createObjectMapper();

        File file = new File("database/normal_users.json");
        file.getParentFile().mkdirs();

        try {
            mapper.writeValue(file, normalUsers);
            System.out.println("save info normalUser");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void savePrimeUser(UserPrimeList primeUsers) {

        ObjectMapper mapper = createObjectMapper();

        File file = new File("database/prime_users.json");
        file.getParentFile().mkdirs();

        try {
            mapper.writeValue(file, primeUsers);
            System.out.println("save info PrimeUser");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveAdminUser(UserAdminList adminUser) {

        ObjectMapper mapper = createObjectMapper();

        File file = new File("database/admin_users.json");
        file.getParentFile().mkdirs();

        try {
            mapper.writeValue(file, adminUser);
            System.out.println("save info AdminUser");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // generate new id
    public static String getNewId(UserNormalList normalUsers, UserPrimeList primeUsers) {
        int maxId = 0;

        // check normal user

        if (normalUsers != null) {
            for (NormalUser user : normalUsers.getUsers()) {
                String id = user.getUserId();
                if (user != null) {
                    int idNumber = Integer.parseInt(id.substring(4));
                    if (idNumber > maxId)
                        maxId = idNumber;
                }
            }
        }

        // check prime user

        if (primeUsers != null) {
            for (PrimeUser user : primeUsers.getUsers()) {
                String id = user.getUserId();
                if (user != null) {
                    int idNumber = Integer.parseInt(id.substring(4));
                    if (idNumber > maxId)
                        maxId = idNumber;
                }
            }
        }

        int nextId = maxId + 1;
        return "USR_" + String.format("%06d", nextId);
    }

    private static String getNewMemberShipCode(UserPrimeList primeUsers) {
        int maxId = 0;

        if (primeUsers != null) {
            for (PrimeUser user : primeUsers.getUsers()) {
                String id = user.getMemberShipID();
                if (user != null) {
                    int idNumber = Integer.parseInt(id.substring(4));
                    if (idNumber > maxId)
                        maxId = idNumber;
                }
            }
        }

        int nextCode = maxId + 1;
        return "PRM_" + String.format("%06d", nextCode);
    }

    // operation login signUP
    public static boolean registerNormalUser(String firstName, String lastName, String phoneNumber,
            String hashedPassword, UserNormalList normalUsers, UserPrimeList primeUsers, UserAdminList adminUsers) {
        if (searchUserByPhoneNumber(phoneNumber, normalUsers, primeUsers, adminUsers) != null)
            return false;

        NormalUser user = new NormalUser(null, phoneNumber, hashedPassword, firstName, lastName, null, 0.0);

        String newId = getNewId(normalUsers, primeUsers);
        user.setUserId(newId);

        normalUsers.addUser(user);
        saveNormalUser(normalUsers);
        return true;
    }

    public static User login(String phoneNumber, String password, UserNormalList normalUsers, UserPrimeList primeUsers,
            UserAdminList adminUsers) {
        User user = searchUserByPhoneNumber(phoneNumber, normalUsers, primeUsers, adminUsers);
        if (user == null) {
            System.out.println("user not founded");
            return null;
        }

        String mainPassword = "";
        mainPassword = user.getPassword();

        if (PasswordHasher.checkerPassword(password, mainPassword)) {
            System.out.println("successfully login as " + user.getUserType().getDisplayName());
            return user;
        } else {
            System.out.println("Unsuccessful login");
            return null;
        }
    }

    public static User searchUserByPhoneNumber(String phoneNumber, UserNormalList normalUsers, UserPrimeList primeUsers,
            UserAdminList adminUsers) {

        if (normalUsers != null) {
            for (NormalUser normal : normalUsers.getUsers())
                if (normal.getPhoneNumber().equals(phoneNumber))
                    return normal;
        }

        if (primeUsers != null) {
            for (PrimeUser prime : primeUsers.getUsers())
                if (prime.getPhoneNumber().equals(phoneNumber))
                    return prime;
        }

        if (adminUsers != null) {
            for (AdminUser admin : adminUsers.getUsers())
                if (admin.getPhoneNumber().equals(phoneNumber))
                    return admin;
        }
        return null;
    }

    public static boolean convertNormalUserToPrime(String phonNumber, UserNormalList normalUsers,
            UserPrimeList primeUsers,
            UserAdminList adminUsers) {
        Object user = searchUserByPhoneNumber(phonNumber, normalUsers, primeUsers, adminUsers);
        if (user == null || !(user instanceof NormalUser)) {
            System.out.println("user normal not found");
            return false;
        }
        NormalUser normalUser = (NormalUser) user;
        PrimeUser primeUser = new PrimeUser(
                normalUser.getUserId(),
                normalUser.getPhoneNumber(),
                normalUser.getPassword(),
                normalUser.getFirstName(),
                normalUser.getLastName(),
                normalUser.getRegisterDate(),
                normalUser.getBalance(),
                null,
                0, 0);

        String newId = getNewMemberShipCode(primeUsers);
        primeUser.setMemberShipID(newId);

        normalUsers.removeUser(normalUser);
        primeUsers.addUser(primeUser);

        savePrimeUser(primeUsers);
        saveNormalUser(normalUsers);

        return true;
    }
}
