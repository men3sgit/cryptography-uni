package com.menes.cryptography.algorithms;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SymmetricCipher {
    private String transform;
    private String mode;

    public SymmetricCipher(String transform) {
        this.transform = transform;
        mode = transform.substring(transform.indexOf('/') + 1, transform.lastIndexOf('/'));
    }

    public String encrypt(String plainText, SecretKey secretKey, String initVector) throws Exception {
        Cipher cipher = Cipher.getInstance(transform);
        if (mode.equalsIgnoreCase("ECB")) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } else {
            System.out.println(initVector);
            byte[] iv = initVector.isBlank() ? new byte[cipher.getBlockSize()] : Base64.getEncoder().encode(initVector.getBytes(StandardCharsets.UTF_8));
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        }
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
    }

    public String decrypt(String cipherText, SecretKey secretKey, String initVector) throws Exception {
        byte[] cipherBytes = Base64.getDecoder().decode(cipherText.getBytes(StandardCharsets.UTF_8));
        Cipher cipher = Cipher.getInstance(transform);
        if (mode.equalsIgnoreCase("ECB")) {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        } else {
            byte[] iv = initVector.isBlank() ? new byte[cipher.getBlockSize()] : Base64.getEncoder().encode(initVector.getBytes(StandardCharsets.UTF_8));
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        }
        return new String(cipher.doFinal(cipherBytes));
    }
}
// TODO INITVECTOR