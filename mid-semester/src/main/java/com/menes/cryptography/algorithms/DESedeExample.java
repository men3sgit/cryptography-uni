package com.menes.cryptography.algorithms;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class DESedeExample {
    public static void main(String[] args) throws Exception {
        String originalText = "Hello, DESede!";
        String keyString = "00112233445566778899AABBCCDDEEFF0011223344556677"; // 24-byte key

        // Convert the key string to bytes

        // Create a DESede key from the key bytes
        KeyGenerator k = KeyGenerator.getInstance("DESede");
        k.init(112);
        SecretKey key = k.generateKey();

        // Encryption
        String encryptedText = encrypt(originalText, key);
        System.out.println("Encrypted Text: " + encryptedText);

        // Decryption
        String decryptedText = decrypt(encryptedText, key);
        System.out.println("Decrypted Text: " + decryptedText);
    }

    public static String encrypt(String originalText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        System.out.println(Arrays.toString(key.getEncoded()));
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(originalText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
