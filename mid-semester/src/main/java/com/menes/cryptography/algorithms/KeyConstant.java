package com.menes.cryptography.algorithms;

public interface KeyConstant {
    interface Algorithm {
        String DES = "DES";
        String TRIPLE_DES = "DESede";
        String AES = "AES";
        String RC4 = "RC4";
        String SEED = "SEED";
        String IDEA = "IDEA";
    }

    interface Padding {
        String NO = "NoPadding";
        String PKCS5 = "PKCS5Padding";
        String PKCS7 = "PKCS7Padding";
        String ISO10126 = "ISO10126Padding";
        String ZERO = "ZeroPadding";
    }

    interface Mode {
        String OBF = "OBF";
        String CBC = "CBC";
        String ECB = "ECB";
        String CFB = "CFB";
        String CTR = "CTR";
    }

}
