package com.menes.cryptography.utils;

import java.awt.*;

public class Common {
    public interface Cursor {
        java.awt.Cursor HAND_CURSOR = new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR);
    }

    public interface Title {
        String BLOCK_CIPHERS = "Block ciphers (Symmetric)";
        String HMAC = "Hashing Message Authentication Code";
        String MESSAGE_DIGEST = "Message Digest - Hash functions";
        String STREAM_CIPHERS = "Stream ciphers (Symmetric) ";
        String SIGNATURE_ELECTRONIC = "";
        String BASIC_ENCRYPTION = "Basic encryption (Classical ciphers)";
    }

    public interface Padding {
        String NO = "NoPadding";
        String PKCS5 = "PKCS5Padding";
        String PKCS7 = "PKCS7Padding";
        String ISO10126 = "ISO10126Padding";
        String ZERO = "ZeroPadding";
    }

    public interface Mode {
        int ECB = 0;
        int CFB = 1;
        int OFB = 2;
        int CBC = 3;
    }

    public interface Unit {
        int MAIN_HEIGHT = 640;
        int MAIN_WIDTH = 960;
        int INPUT_HEIGHT = 300;
        int INPUT_WIDTH = 400;
        int INPUT_TEXT_SIZE = 16;
    }

    public interface Color {
        java.awt.Color THEME = new java.awt.Color(44, 58, 77);
        java.awt.Color COPIED = new java.awt.Color(50, 183, 127);
        java.awt.Color ENTERED = new java.awt.Color(213, 212, 187);
    }
}
