package com.menes.cryptography.algorithms;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HMAC {
    private String secretKey = "mySecretKey";

    public String doCipher(String message, String algo) {
        try {
            // Create HMAC-SHA256 Mac instance and initialize it with the secret key
            Mac sha256Hmac = Mac.getInstance(algo);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), algo);
            sha256Hmac.init(secretKeySpec);

            // Compute the HMAC of the message
            byte[] hmacBytes = sha256Hmac.doFinal(message.getBytes(StandardCharsets.UTF_8));

            // Convert the HMAC bytes to a Base64-encoded string
            return Base64.getEncoder().encodeToString(hmacBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
