/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uasoop;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 *
 * @author Wahyu
 */
public class PasswordUtils {
    
    /**
     * Hash password menggunakan SHA-256 dengan salt
     * @param password password asli
     * @param salt salt untuk keamanan tambahan
     * @return password yang sudah di-hash
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            // Gabungkan password dengan salt
            String saltedPassword = password + salt;
            
            // Hash password
            byte[] hashedBytes = md.digest(saltedPassword.getBytes());
            
            // Convert byte array ke string hexadecimal
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Hash password dengan salt default
     * @param password password asli
     * @return password yang sudah di-hash
     */
    public static String hashPassword(String password) {
        // Gunakan salt default untuk konsistensi
        String defaultSalt = "sewa_kamera_2025";
        return hashPassword(password, defaultSalt);
    }
    
    /**
     * Verifikasi password dengan hash yang tersimpan
     * @param password password input user
     * @param hashedPassword hash password dari database
     * @return true jika password cocok
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        String hashedInput = hashPassword(password);
        return hashedInput.equals(hashedPassword);
    }
    
    /**
     * Generate salt random untuk user baru (opsional)
     * @return salt random
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        
        StringBuilder sb = new StringBuilder();
        for (byte b : salt) {
            sb.append(String.format("%02x", b));
        }
        
        return sb.toString();
    }
}
