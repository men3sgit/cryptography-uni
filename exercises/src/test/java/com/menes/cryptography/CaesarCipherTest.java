package com.menes.cryptography;

import junit.framework.TestCase;
import org.junit.Test;

public class CaesarCipherTest extends TestCase {
    @Test
    public void testEncrypt() {
        CaesarCipher cipher = new CaesarCipher();
        String plaintext = "Hello, World!";
        int key = 3;
        String expectedEncryptedText = "Khoor, Zruog!";

        String encryptedText = cipher.encrypt(plaintext, key);

        assertEquals(expectedEncryptedText, encryptedText);
    }

    @Test
    public void testDecrypt() {
        CaesarCipher cipher = new CaesarCipher();
        String encryptedText = "Khoor, Zruog!";
        int key = 3;
        String expectedDecryptedText = "Hello, World!";

        String decryptedText = cipher.decrypt(encryptedText, key);

        assertEquals(expectedDecryptedText, decryptedText);
    }
}