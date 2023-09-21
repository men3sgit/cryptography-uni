package com.menes.cryptography;

import java.util.*;

public class SubstitutionCipher {
    private static final char[] ALPHABET = new char[26];

    static {
        for (byte i = 0; i < ALPHABET.length; i++) {
            ALPHABET[i] = (char) ('A' + i);
        }
    }

    public String encrypt(String plainText, String cipherText) {
        StringBuilder builder = new StringBuilder();
        for (char c : plainText.toCharArray()) {
            if (Character.isLetter(c)) {
                int index = (c - 'A');
                builder.append(cipherText.charAt(index));
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    public String decrypt(String encryptedText, String cipherText) {
        StringBuilder builder = new StringBuilder();
        for (char c : encryptedText.toCharArray()) {
            if (Character.isLetter(c)) {
                int index = cipherText.indexOf(c);
                builder.append(ALPHABET[index]);
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    public static String generateCipherText() {
        StringBuilder builder = new StringBuilder();
        int[] shuffledIndexes = generateShuffleIndexes();
        for (int index : shuffledIndexes) {
            builder.append(ALPHABET[index]);
        }
        return builder.toString();
    }

    private static int[] generateShuffleIndexes() {
        int[] array = new int[ALPHABET.length];
        for (int i = 0; i < ALPHABET.length; i++) {
            array[i] = i;
        }
        shuffle(array);
        return array;
    }

    private static void shuffle(int[] array) {
        final int timeToShuffle = 30;
        Random random = new Random();
        for (int i = 0; i < timeToShuffle; i++) {
            int x = random.nextInt(array.length);
            int y = random.nextInt(array.length);
            array[x] ^= array[y] ^ (array[y] = array[x]);
        }
    }


    public static void main(String[] args) {
        SubstitutionCipher cipher = new SubstitutionCipher();
        String cipherText = SubstitutionCipher.generateCipherText();
        String plainText = "DUONG DUY MEN";
        String encryptedText = cipher.encrypt(plainText, cipherText);
        String decryptedText = cipher.decrypt(encryptedText, cipherText);
        System.out.println(cipherText);
        System.out.println(encryptedText);
        System.out.println(decryptedText);


    }

}
