package com.example.emptyactivity;

import androidx.appcompat.app.AppCompatActivity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil {
    // Hashes a password using SHA-256
    public static String hashPassword(String password) {
        try {
            //message digest class - cryptographic hashing
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            //converts input passw. to bytes
            byte[] hashedBytes = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                //each byte converted to hexadecimal
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Verifies a password by hashing the input and comparing it to the stored hash
    public static boolean verifyPassword(String enteredPassword, String storedHash) {

        return hashPassword(enteredPassword).equals(storedHash);
    }
}
