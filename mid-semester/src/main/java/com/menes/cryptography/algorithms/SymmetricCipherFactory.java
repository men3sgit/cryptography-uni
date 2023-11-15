package com.menes.cryptography.algorithms;

import com.menes.cryptography.utils.CipherUtils;
import com.menes.cryptography.utils.Common;

public class SymmetricCipherFactory {
    public static SymmetricCipher getInstance(String transform) {
        String algo = CipherUtils.getAlgorithm(transform);
        return switch (algo) {
            case Common.Symmetric.DES -> new DES(transform);
            case Common.Symmetric.TRIPLE_DES -> new TripleDES(transform);
            default -> new AES(transform);
        };
    }
}
