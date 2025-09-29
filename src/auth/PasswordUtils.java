package auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {
    /**
     * Generate a random salt for password hashing
     */
    public static String generateSalt() {
        java.security.SecureRandom random = new java.security.SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        StringBuilder sb = new StringBuilder();
        for (byte b : salt) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Hash a password with salt using SHA-256
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String salted = password + salt;
            byte[] hash = md.digest(salted.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verify a password against a hash and salt
     */
    public static boolean verifyPassword(String password, String salt, String hash) {
        return hashPassword(password, salt).equals(hash);
    }
}
