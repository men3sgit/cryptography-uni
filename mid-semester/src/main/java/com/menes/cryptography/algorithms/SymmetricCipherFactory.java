package com.menes.cryptography.algorithms;

import com.menes.cryptography.utils.CipherUtils;
import com.menes.cryptography.utils.Common;

public class SymmetricCipherFactory {
    public static SymmetricCipher getInstance(String transform) {
        String algo = CipherUtils.getAlgorithm(transform);
        switch (algo) {
            case Common.Symmetric.DES:
                return new DES(transform);
            case Common.Symmetric.TRIPLE_DES:
                return new TripleDES(transform);
            default:
                return new AES(transform);
        }
    }
}
