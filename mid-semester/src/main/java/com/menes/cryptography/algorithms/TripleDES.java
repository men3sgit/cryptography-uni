package com.menes.cryptography.algorithms;

public class TripleDES extends SymmetricCipher{
    public TripleDES(String transform) {
        super(transform);
        this.keySize = 24;
    }
}
