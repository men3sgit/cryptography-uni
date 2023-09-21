package com.menes.cryptography;

public class CaesarCipher {
    private static final int ALPHABET_SIZE = 26;

    public String encrypt(String text, int key) {
        StringBuilder builder = new StringBuilder();
        for (char character : text.toCharArray()) {
            if (Character.isLetter(character)) {
                char encryptedCharacter = translateByKey(character, key);
                builder.append(encryptedCharacter);
            } else
                builder.append(character);
        }
        return builder.toString();
    }

    private char translateByKey(char character, int key) {
        char base = Character.isUpperCase(character) ? 'A' : 'a';
        return (char) (base + (character - base + key + ALPHABET_SIZE) % ALPHABET_SIZE);
    }

    public String decrypt(String encryptedText, int key) {
        return encrypt(encryptedText, -key);
    }
}
