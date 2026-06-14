package Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {

    public static String hashingPassword(String password){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(byte b:encodedHash){
                String hex = String.format("%02x", b);
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkerPassword(String inputPassword , String hashedPassword){
        String inputToHash = hashingPassword(inputPassword);
        return inputToHash.equals(hashedPassword);
    }
}
