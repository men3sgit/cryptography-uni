package com.menes.cryptography.algorithms;

public class DES extends SymmetricCipher {
    public DES(String transform) {
        super(transform);
        this.keySize = 8;
    }
}
