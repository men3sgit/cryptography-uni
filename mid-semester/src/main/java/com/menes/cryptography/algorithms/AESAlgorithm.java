package com.menes.cryptography.algorithms;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AESAlgorithm extends AsymmetricAlgorithm {

    private static final String AES_ALGORITHM = KeyConstant.Algorithm.AES;

    public static byte[] generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
        keyGenerator.init(256); // 256 bits key size
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    @Override
    public String encrypt(String plainText) throws Exception {
        SecretKey secretKey = generateSecretKey();
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM + super.getTransformation());
        IvParameterSpec iv = generateIV(); // Initialize your IV here
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    @Override
    public String decrypt(String cipherText) throws Exception {
        SecretKey secretKey = generateSecretKey();
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM + super.getTransformation());
        IvParameterSpec iv = generateIV(); // Initialize your IV here
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] encryptedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    private SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        return new SecretKeySpec(generateAESKey(), AES_ALGORITHM);
    }

    private IvParameterSpec generateIV() {
        byte[] iv = new byte[16]; // IV size for AES CBC mode is 16 bytes
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
}
