package com.menes.cryptography.algorithms;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyException;
import java.util.Base64;

public class DES {
    private SecretKey secretKey;

    public DES() {
        try {
            generateKey();
        } catch (Exception e) {
        }

    }

    public String generateKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
            keyGenerator.init(56);
            secretKey = keyGenerator.generateKey();
            return getKeyString();
        } catch (Exception e) {
            return "NO SUPPORT";
        }
    }

    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    private boolean keyNotFound() {
        return secretKey == null;
    }

    public String encrypt(String plainText) throws Exception {
        if (keyNotFound()) throw new KeyException("KEY NOT FOUND");

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String cipherText) throws Exception {
        if (keyNotFound()) throw new KeyException("KEY NOT FOUND");

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    public String getKeyString() {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

}
