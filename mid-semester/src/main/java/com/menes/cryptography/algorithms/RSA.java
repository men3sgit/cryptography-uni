package com.menes.cryptography.algorithms;

import com.menes.cryptography.utils.FileUtils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class RSA {
    private Key publicKey;
    private Key privateKey;

    public RSA() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    public KeyPair generateKeyPair(int size) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(size); // You can adjust the key size as needed
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
        return keyPair;
    }

    public String encrypt(String message, Key publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes()));
    }

    public String decrypt(String encryptedMessage, Key privateKey) throws Exception {
        byte[] cipherBytes = Base64.getDecoder().decode(encryptedMessage.getBytes(StandardCharsets.UTF_8));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(cipherBytes);
        return new String(decryptedBytes);
    }

    public void encryptFile(String inputFile, Key publicKey) throws Exception {
        try {
            FileInputStream inputStream = new FileInputStream(inputFile);
            FileOutputStream outputStream = new FileOutputStream(
                    FileUtils.appendFileName(inputFile,
                            String.format("-enc-%d", System.currentTimeMillis())));

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] inputBuffer = new byte[8];
            int bytesRead;
            byte[] outputBytes;
            while ((bytesRead = inputStream.read(inputBuffer)) != -1) {
                outputBytes = cipher.update(inputBuffer, 0, bytesRead);
                if (outputBytes != null) {
                    outputStream.write(outputBytes);
                }
            }
            outputStream.flush();
            outputStream.close();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void decryptFile(String inputFile, Key privateKey) throws Exception {
        try (FileInputStream inputStream = new FileInputStream(inputFile);
             FileOutputStream outputStream = new FileOutputStream(
                     FileUtils.appendFileName(inputFile,
                             String.format("-dec-%d", System.currentTimeMillis())))) {


            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] inputBuffer = new byte[8];
            int bytesRead;
            byte[] outputBytes;
            while ((bytesRead = inputStream.read(inputBuffer)) != -1) {
                outputBytes = cipher.update(inputBuffer, 0, bytesRead);
                if (outputBytes != null) {
                    outputStream.write(outputBytes);
                }
            }
            outputStream.flush();

        }
    }

    public Key getPublicKey() {
        return publicKey;
    }

    public Key getPrivateKey() {
        return privateKey;
    }
}
