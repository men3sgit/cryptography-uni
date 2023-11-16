package com.menes.cryptography.algorithms;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.util.encoders.Hex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class MessageDigest {
    public static String hash(String input, String algo) throws NoSuchAlgorithmException {
        if (algo.equalsIgnoreCase("RIPEMD-160")) {
            RIPEMD160Digest ripemd160Digest = new RIPEMD160Digest();
            byte[] inputData = input.getBytes();
            ripemd160Digest.update(inputData, 0, inputData.length);
            byte[] outputData = new byte[ripemd160Digest.getDigestSize()];
            ripemd160Digest.doFinal(outputData, 0);
            return Hex.toHexString(outputData);
        }
        java.security.MessageDigest digest = java.security.MessageDigest.getInstance(algo);
        byte[] hashBytes = digest.digest(input.trim().getBytes(StandardCharsets.UTF_8));
        return new BigInteger(1, hashBytes).toString(16);
    }

    public static String hash(File file, String algo) throws NoSuchAlgorithmException {
        if (algo.equalsIgnoreCase("RIPEMD-160")) {
            RIPEMD160Digest ripemd160Digest = new RIPEMD160Digest();
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                byte[] buffer = new byte[512];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    ripemd160Digest.update(buffer, 0, bytesRead);
                }
                byte[] outputData = new byte[ripemd160Digest.getDigestSize()];
                ripemd160Digest.doFinal(outputData, 0);
                return new BigInteger(1, outputData).toString(16);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance(algo);
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] buffer = new byte[512];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
                fileInputStream.close();
                byte[] hashBytes = digest.digest();
                return new BigInteger(1, hashBytes).toString(16);
            } catch (IOException e) {
                return null;
            }
        }

    }


}
