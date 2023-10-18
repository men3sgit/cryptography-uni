package com.menes.cryptography;

public class CeasarCipherV2 {
    private static final int ALPHABET_SIZE = 26;

    public String encrypt(String text, int key) {
        StringBuilder builder = new StringBuilder();
        for (char character : text.toCharArray()) {
            if (Character.isLetter(character)) {
                char encryptedChar = translate(character, key);
                builder.append(encryptedChar);
            } else {
                builder.append(character);
            }
        }
        return builder.toString();
    }

    private char translate(char character, int key) {
        char base = Character.isUpperCase(character) ? 'A' : 'a';
        int shiftedChar = (character - base + key + ALPHABET_SIZE) % ALPHABET_SIZE + base;
        return (char) shiftedChar;
    }

    public String decrypt(String encryptedText, int key) {
        return encrypt(encryptedText, -key);
    }
}
