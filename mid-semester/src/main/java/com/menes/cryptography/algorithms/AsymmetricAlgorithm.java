package com.menes.cryptography.algorithms;

public abstract class AsymmetricAlgorithm implements IAsymmetricAlgorithm {

    protected String key = "0123456789abcdef0123456789abcdef0123456789abcdef";
    protected String mode;
    protected String padding;
    protected int blockSize;
    protected String initVector;

    @Override
    public IAsymmetricAlgorithm builder() {
        return this;
    }

    @Override
    public IAsymmetricAlgorithm setMode(String mode) {
        this.mode = mode;
        return this;
    }

    @Override
    public IAsymmetricAlgorithm setPadding(String padding) {
        this.padding = padding;
        return this;
    }

    @Override
    public IAsymmetricAlgorithm setKey(String key) {
        this.key = key;
        return this;
    }

    @Override
    public IAsymmetricAlgorithm setInitVector(String initVector) {
        this.initVector = initVector;
        return this;
    }

    @Override
    public IAsymmetricAlgorithm setBlockSize(int blockSize) {
        this.blockSize = blockSize;
        return this;
    }

    @Override
    public IAsymmetricAlgorithm build() {
        return this;
    }

    protected String getTransformation() {
        StringBuilder builder = new StringBuilder();
        builder.append("/");
        builder.append(mode);
        builder.append("/");
        builder.append(padding);


        return builder.toString();
    }

    public static void main(String[] args) throws Exception {
        AsymmetricAlgorithm algorithm = new AESAlgorithm();
        algorithm.setMode(KeyConstant.Mode.CBC);
        algorithm.setPadding(KeyConstant.Padding.PKCS5);

        String plainText = "Mến đẹp trai";
        String cipherText = algorithm.encrypt(plainText);
        System.out.println(algorithm.decrypt(cipherText));
    }


}
