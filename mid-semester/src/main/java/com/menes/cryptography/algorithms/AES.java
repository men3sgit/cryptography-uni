package com.menes.cryptography.algorithms;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AES {

    public static void main(String[] args) throws Exception {
        AES aes = new AES();
        String plainText = "Hello, AES!";
        int keySize = 128;
        SecretKey secretKey = aes.generateKey(keySize);
        String encrypted = aes.encrypt(plainText, secretKey);
        System.out.println("Encrypted: " + encrypted);

        String decrypted = aes.decrypt(encrypted, secretKey);
        System.out.println("Decrypted: " + decrypted);
    }

    public  SecretKey generateKey(int keySize) throws NoSuchAlgorithmException, NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(keySize);
        return keyGenerator.generateKey();
    }

    public  String encrypt(String plainText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
        byte[] iv = new byte[cipher.getBlockSize()];
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey,ivParameterSpec);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
    }

    public  String decrypt(String cipherText, SecretKey secretKey) throws Exception {
        byte[] cipherBytes = Base64.getDecoder().decode(cipherText.getBytes(StandardCharsets.UTF_8));
        Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
        byte[] iv = new byte[cipher.getBlockSize()];
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey,ivParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(cipherBytes);
        return new String(decryptedBytes);
    }
}
