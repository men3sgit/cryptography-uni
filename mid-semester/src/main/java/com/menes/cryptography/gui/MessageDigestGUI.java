package com.menes.cryptography.gui;

import com.menes.cryptography.algorithms.MessageDigest;
import com.menes.cryptography.gui.panels.ScreenPanel;

import javax.swing.*;
import java.awt.*;
import java.security.NoSuchAlgorithmException;

public class MessageDigestGUI implements AlgorithmGUI {
    JComboBox<String> hashes;
    JTextArea input, result;
    private final ScreenPanel screenPanel;

    public MessageDigestGUI(ScreenPanel screenPanel) {
        this.screenPanel = screenPanel;
        this.input = screenPanel.getInput();
        this.result = screenPanel.getResult();
        hashes = new JComboBox<>(new String[]{"SHA-1", "SHA-224", "SHA-256", "SHA-384", "SHA-512", "SHA-512/224", "SHA-512/256", "MD5", "RIPEMD-160"});
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
        String algo = hashes.getSelectedItem().toString();
        String message = "";
        if (screenPanel.isFileMode()) {
            message = MessageDigest.hash(screenPanel.getFileChooser().getSelectedFile(), algo);
        } else if (input.getText().isBlank()) return;
        else {
            message = MessageDigest.hash(input.getText(), algo);
        }
        result.setText(message);
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
