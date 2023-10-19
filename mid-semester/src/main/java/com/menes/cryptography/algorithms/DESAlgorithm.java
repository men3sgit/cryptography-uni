package com.menes.cryptography.algorithms;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.util.Base64;

public class DESAlgorithm extends AsymmetricAlgorithm {

    @Override
    public String encrypt(String plainText) throws Exception {
        SecretKey secretKey = generateSecretKey();
        Cipher cipher = Cipher.getInstance(KeyConstant.Algorithm.DES + super.getTransformation());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    @Override
    public String decrypt(String cipherText) throws Exception {
        SecretKey secretKey = generateSecretKey();
        Cipher cipher = Cipher.getInstance(KeyConstant.Algorithm.DES + super.getTransformation());
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] encryptedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    private SecretKey generateSecretKey() throws Exception {
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KeyConstant.Algorithm.DES);
        return keyFactory.generateSecret(desKeySpec);
    }
}
