package com.menes.cryptography.gui;

import com.menes.cryptography.algorithms.MessageDigest;

import javax.swing.*;
import java.awt.*;
import java.security.NoSuchAlgorithmException;

public class MessageDigestGUI implements AlgorithmGUI {
    JComboBox<String> hashes;
    JTextArea input, result;

    public MessageDigestGUI(JTextArea input, JTextArea result) {
        this.input = input;
        this.result = result;
        hashes = new JComboBox<>(new String[]{"SHA-1", "SHA-256", "SHA-512", "MD5", "RIPEMD-160"});
        hashes.addActionListener(action -> {
            try {
                encrypt();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public JPanel renderGUI() {
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.add(getSelection());
        return main;
    }

    @Override
    public void encrypt() throws NoSuchAlgorithmException {
        if(input.getText().isBlank()) return;
        String algo = hashes.getSelectedItem().toString();
        result.setText(MessageDigest.hash(input.getText(), algo));
    }

    @Override
    public void decrypt() {

    }

    private JPanel getSelection() {
        JPanel rs = new JPanel();
        rs.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel("Select hashing method");
        rs.add(label);
        rs.add(hashes);
        return rs;
    }

}
