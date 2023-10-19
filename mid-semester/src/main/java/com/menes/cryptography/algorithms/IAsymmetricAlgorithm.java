package com.menes.cryptography.algorithms;

public interface IAsymmetricAlgorithm {
    String encrypt(String plainText) throws Exception;

    String decrypt(String cipherText) throws Exception;

    IAsymmetricAlgorithm builder();

    IAsymmetricAlgorithm setMode(String mode);

    IAsymmetricAlgorithm setPadding(String padding);

    IAsymmetricAlgorithm setKey(String key);

    IAsymmetricAlgorithm setInitVector(String initVector);

    IAsymmetricAlgorithm setBlockSize(int blockSize);

    IAsymmetricAlgorithm build();

}
