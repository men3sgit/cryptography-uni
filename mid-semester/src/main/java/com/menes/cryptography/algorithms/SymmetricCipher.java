package com.menes.cryptography.algorithms;

import com.menes.cryptography.utils.Common;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import static com.menes.cryptography.utils.FileUtils.isValidFile;

public abstract class SymmetricCipher {
    protected int keySize;
    public String transform;
    public String mode;
    protected SecretKey secretKey;
    protected String algo;
    protected Cipher cipher;
    protected IvParameterSpec iv;

    public SymmetricCipher(String transform) {
        this.transform = transform;
        this.mode = transform.substring(transform.indexOf('/') + 1, transform.lastIndexOf('/'));
        this.algo = transform.substring(0, transform.indexOf('/'));
        try {
            this.cipher = Cipher.getInstance(algo);
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    private boolean keyNotFound() {
        return secretKey == null;
    }

    public byte[] encrypt(byte[] plainText) throws Exception {
        if (keyNotFound()) throw new KeyException("KEY NOT FOUND");
        if (mode.equalsIgnoreCase("ECB")) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        }
        return cipher.doFinal(plainText);
    }


    public byte[] decrypt(byte[] cipherText) throws KeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        if (keyNotFound()) throw new KeyException("KEY NOT FOUND");
        if (mode.equalsIgnoreCase("ECB")) {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        }
        return cipher.doFinal(cipherText);


    }


    public void encryptFile(String sourceFilePath, String destFilePath) throws Exception {
        if (keyNotFound()) throw new KeyException("KEY NOT FOUND");
        File destFile = new File(destFilePath);
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        if (!isValidFile(sourceFilePath, destFilePath)) {
            System.out.println("Encryption Failed.");
            return;
        }
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFilePath)); BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFilePath))) {

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            doCipher(bis, bos, cipher);
            System.out.println("Encryption completed successfully.");
        }
    }

    private void doCipher(InputStream inputStream, OutputStream outputStream, Cipher cipher) throws Exception {

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
        inputStream.close();
    }


    public void decryptFile(String sourceFilePath) throws KeyException, IOException {
        if (keyNotFound()) throw new KeyException("KEY NOT FOUND");
        String destFilePath = String.format("%s-decrypted.%s");
        File destFile = new File(destFilePath);
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        if (!isValidFile(sourceFilePath, destFilePath)) {
            System.out.println("Decryption Failed.");
            return;
        }


        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFilePath)); BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFilePath))) {

            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            doCipher(bis, bos, cipher);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SecretKey getSecretKey() throws KeyException, NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algo);
        int size;
        switch (algo) {
            case Common.Symmetric.DES:
                size = 56;
                break;
            case Common.Symmetric.TRIPLE_DES:
                size = 168;
                break;
            default:
                size = keySize * 8;
                break;
        }
        keyGenerator.init(size);
        return this.secretKey = keyGenerator.generateKey();
    }

    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public void setSecretKey(String text) {
        byte[] paddingSize = new byte[keySize];
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(bytes, 0, paddingSize, 0, bytes.length);
        this.secretKey = new SecretKeySpec(paddingSize, algo);
    }

    public int getKeySize() {
        return keySize;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    public IvParameterSpec getIv() {
        byte[] ivBytes = new byte[16];
        new SecureRandom().nextBytes(ivBytes);
        return this.iv = new IvParameterSpec(ivBytes);
    }

    public void setIv(String iv) {
        byte[] paddingSize = new byte[8];
        byte[] bytes = iv.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(bytes, 0, paddingSize, 0, bytes.length);
        this.iv = new IvParameterSpec(paddingSize);
    }


}
