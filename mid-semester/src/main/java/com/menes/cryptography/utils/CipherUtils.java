package com.menes.cryptography.utils;

public class CipherUtils {
    public static String getAlgorithm(String transform) {
        return transform.substring(0, transform.indexOf('/'));
    }

    public static String getMode(String transform) {
        return transform.substring(transform.indexOf('/') + 1, transform.lastIndexOf('/'));
    }

    public static String getPadding(String transform) {
        return transform.substring(transform.lastIndexOf('/') + 1);
    }
}
