package com.menes.cryptography.gui;

import com.menes.cryptography.algorithms.HMAC;
import com.menes.cryptography.algorithms.MessageDigest;
import com.menes.cryptography.utils.Common;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HMACGUI implements AlgorithmGUI {
    JTextArea input, result;
    JTextField keyInput = new JTextField();
    private final HMAC hmac = new HMAC();

    public HMACGUI(JTextArea input, JTextArea result) {
        this.input = input;
        this.result = result;
    }

    @Override
    public JPanel renderGUI() {
        keyInput.setPreferredSize(new Dimension(350, 26));
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        JPanel keyPanel = new JPanel();
        keyPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton generateBtn = new JButton("Generate random Key");
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setHorizontalAlignment(SwingConstants.LEFT);
        generateBtn.setBackground(Common.Color.THEME);
        generateBtn.setFocusable(false);
        generateBtn.addActionListener(action -> {
            try {
                keyInput.setText(hmac.getKeyString());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        });
        generateBtn.setFocusable(false);
        generateBtn.setCursor(Common.Cursor.HAND_CURSOR);
        keyPanel.add(generateBtn);
        keyPanel.add(keyInput);
        main.add(keyPanel);
        return main;
    }

    @Override
    public void doCipher() throws NoSuchAlgorithmException {
        if (input.getText().isBlank()) return;
        result.setText(MessageDigest.hash(input.getText(), "HmacSHA256"));
    }
    public static void main(String[] args) {
        try {
            // Generate a random secret key
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecureRandom random = new SecureRandom();
            keyGenerator.init(random);
            SecretKey secretKey = keyGenerator.generateKey();

            // Convert the secret key to a byte array
            byte[] keyBytes = secretKey.getEncoded();

            // Create a Mac instance with the HmacSHA256 algorithm
            Mac mac = Mac.getInstance("HmacSHA256");

            // Initialize the Mac with the secret key
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "HmacSHA256");
            mac.init(secretKeySpec);

            // Provide the message you want to calculate the HMAC for
            String message = "This is the message for which we want to calculate the HMAC";

            // Calculate the HMAC
            byte[] hmacBytes = mac.doFinal(message.getBytes());

            // Convert the HMAC to a hexadecimal string for easy representation
            StringBuilder hmacHex = new StringBuilder();
            for (byte b : hmacBytes) {
                hmacHex.append(String.format("%02x", b));
            }

            System.out.println("HMAC (SHA-256): " + hmacHex.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
