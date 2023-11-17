package com.menes.cryptography.algorithms;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class HMAC {
    private String secretKey = "mySecretKey";

    public String doCipher(String message) {
        try {

            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256Hmac.init(secretKeySpec);
            byte[] hmacBytes = sha256Hmac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hmacBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getKeyString() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        SecureRandom random = new SecureRandom();
        keyGenerator.init(random);
        SecretKey secretKey = keyGenerator.generateKey();
        return this.secretKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
}
