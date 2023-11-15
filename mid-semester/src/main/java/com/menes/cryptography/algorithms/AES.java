package com.menes.cryptography.algorithms;

public class AES extends SymmetricCipher {
    public AES(String transform) {
        super(transform);
        this.keySize = 16;
    }
}
