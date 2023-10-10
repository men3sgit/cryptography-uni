package com.menes.cryptography;

import javax.crypto.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

public class DES {
    private SecretKey secretKey;

    public SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        keyGenerator.init(56);
        secretKey = keyGenerator.generateKey();
        return secretKey;
    }

    private boolean keyNotFound() {
        return secretKey == null;
    }

    public byte[] encrypt(String plainText) throws Exception {
        if (keyNotFound()) throw new KeyException("KEY NOT FOUND");

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(plainText.getBytes());
    }

    public String decrypt(byte[] cipherText) throws Exception {
        if (keyNotFound()) throw new KeyException("KEY NOT FOUND");

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(cipherText));
    }


    public void encryptFile(String sourceFilePath, String destFilePath) throws Exception {
        if (keyNotFound()) throw new KeyException("KEY NOT FOUND");
        File destFile = new File(destFilePath);
        if(!destFile.exists()){
            destFile.createNewFile();
        }
        if (!isValidFile(sourceFilePath, destFilePath)) {
            System.out.println("Encryption Failed.");
            return;
        }
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFilePath));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFilePath))) {

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            doCipher(bis, bos, cipher);
            System.out.println("Encryption completed successfully.");
        }
    }
    // TODO: show first file which invalid
        private boolean isValidFile(String... filePaths) {
        return Arrays.stream(filePaths).allMatch(file -> {
            boolean isFile = new File(file).isFile();
            if (!isFile) {
                System.out.printf("%s is not a file!%n", file);
            }
            return isFile;
        });
    }

    // TODO: show all files which invalid
//    private boolean isValidFile(String... filePaths) {
//        boolean flag = true;
//        for (String filePath : filePaths) {
//            if (!new File(filePath).isFile()) {
//                System.out.printf("%s is not a file!%n", filePath);
//                flag = false;
//            }
//        }
//
//        return flag;
//    }

    public void decryptFile(String sourceFilePath, String destFilePath) throws Exception {
        if (keyNotFound()) throw new KeyException("KEY NOT FOUND");
        File destFile = new File(destFilePath);
        if(!destFile.exists()){
            destFile.createNewFile();
        }
        if (!isValidFile(sourceFilePath, destFilePath)) {
            System.out.println("Decryption Failed.");
            return;
        }


        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFilePath));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFilePath))) {

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            doCipher(bis, bos, cipher);
            System.out.println("Decryption completed successfully.");
        }
    }

    public String encryptToBase64(String plainText) throws Exception {
        if (keyNotFound()) throw new KeyException("KEY NOT FOUND");

        byte[] base64Bytes = Base64.getEncoder().encode(plainText.getBytes(StandardCharsets.UTF_8));
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherBase64Bytes = cipher.doFinal(base64Bytes);
        return Base64.getEncoder().encodeToString(cipherBase64Bytes);
    }

    public String decryptFromBase64(String cipherText) throws Exception {
        if (keyNotFound()) throw new KeyException("KEY NOT FOUND");

        byte[] cipherBase64Bytes = Base64.getDecoder().decode(cipherText.getBytes(StandardCharsets.UTF_8));
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] plainTextBase64Bytes = cipher.doFinal(cipherBase64Bytes);
        byte[] plainTextBytes = Base64.getDecoder().decode(plainTextBase64Bytes);
        return new String(plainTextBytes);
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

    public static void main(String[] args) throws Exception {
        DES des = new DES();
        des.generateKey();
        String plainText = "Có thể bạn cho rằng đó là vì bản thân không đủ kỷ luật, không đủ tự chủ, không đủ lý trí. Nhưng lý do này chỉ nằm trên bề mặt của sự việc. Bạn muốn kỷ luật, gặt hái thành công và có được cuộc sống hạnh phúc viên mãn, thì bạn không thể bắt đầu bằng việc bắt ép chính mình phải có ý chí hay lý trí mạnh mẽ.";
//
//        System.out.println(Arrays.toString(plainText.getBytes()));
//        byte[] cipherTextBytes = des.encrypt(plainText);
//        System.out.println(Arrays.toString(des.decrypt(cipherTextBytes).getBytes()));
//
//        // OR
//        System.out.println(plainText);
//        byte[] cipherText = des.encrypt(plainText);
//        System.out.println(des.decrypt(cipherText));


        String cipherText = des.encryptToBase64(plainText);
        System.out.println(cipherText);
        System.out.println(des.decryptFromBase64(cipherText));

        String sourceFilePath = "./assets/rse-team.jpg";
        String encryptedFilePath = "./assets/encrypted-rse-team.jpg";
        String decryptedFilePath = "./assets/decrypted-rse-team.jpg";

        des.encryptFile(sourceFilePath, encryptedFilePath);
        des.decryptFile(encryptedFilePath, decryptedFilePath);
    }
}
