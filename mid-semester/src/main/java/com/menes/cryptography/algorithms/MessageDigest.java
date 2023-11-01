package com.menes.cryptography.algorithms;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.util.encoders.Hex;

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

}
