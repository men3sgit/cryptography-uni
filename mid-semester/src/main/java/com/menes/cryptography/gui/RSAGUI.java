package com.menes.cryptography.gui;

import com.menes.cryptography.algorithms.RSA;
import com.menes.cryptography.gui.custom.ScrollBar;
import com.menes.cryptography.gui.panels.ScreenPanel;
import com.menes.cryptography.utils.Common;
import com.menes.cryptography.gui.custom.MarginFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.Base64;
import java.util.Objects;

public class RSAGUI implements AlgorithmGUI {
    private JComboBox<Integer> keySizeOption = new JComboBox<>(new Integer[]{1024, 2048, 4096});
    private JButton generateKeyButton;
    JScrollPane privateScrollPane, publicJScrollPane;
    private JTextArea privateKey, publicKey;
    private RSA rsa = new RSA();
    private JTextArea input, result;
    private ScreenPanel screenPanel;

    public RSAGUI(ScreenPanel screenPanel) {
        this.screenPanel = screenPanel;
        this.result = screenPanel.getResult();
        this.input = screenPanel.getInput();
        int rows = screenPanel.isFileMode() ? 9 : 5;
        privateKey = new JTextArea(rows, 30);
        publicKey = new JTextArea(rows, 30);
    }

    @Override
    public JPanel renderGUI() {
        JPanel main = new JPanel();
        main.setPreferredSize(new Dimension(700, 400));
        main.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttons.add(getGenerateKeyPairPanel());
        buttons.add(getKeySizePanel());
        main.add(buttons);
        main.add(getPairKeyAreaPanel());
        return main;
    }

    private JPanel getPairKeyAreaPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(getPrivateKeyPanel());
        panel.add(MarginFactory.marginRight(20));
        panel.add(getPublicKeyPanel());
        return panel;
    }

    private JPanel getPublicKeyPanel() {
        JPanel panel = new JPanel();
        publicJScrollPane = new JScrollPane(publicKey);
        publicJScrollPane.setVerticalScrollBar(new ScrollBar());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        publicKey.setLineWrap(true);
        publicKey.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        focusTextArea(publicKey);
        JPanel label = new JPanel();
        label.setLayout(new FlowLayout(FlowLayout.LEFT));
        label.add(new JLabel("Public Key"));
        panel.add(label);
        panel.add(publicJScrollPane);
        return panel;
    }

    private JPanel getPrivateKeyPanel() {
        JPanel panel = new JPanel();
        privateScrollPane = new JScrollPane(privateKey);
        privateScrollPane.setVerticalScrollBar(new ScrollBar());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        privateKey.setLineWrap(true);
        privateKey.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        focusTextArea(privateKey);
        JPanel label = new JPanel();
        label.setLayout(new FlowLayout(FlowLayout.LEFT));
        label.add(new JLabel("Private Key"));
        panel.add(label);
        panel.add(privateScrollPane);
        return panel;
    }

    private JPanel getGenerateKeyPairPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(generateKeyButton = new JButton("Generate New Key Pair"));
        generateKeyButton.addActionListener(action -> {
            try {
                rsa.generateKeyPair((Integer) keySizeOption.getSelectedItem());
                privateKey.setText(Base64.getEncoder().encodeToString(rsa.getPrivateKey().getEncoded()));
                publicKey.setText(Base64.getEncoder().encodeToString(rsa.getPublicKey().getEncoded()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
        generateKeyButton.setForeground(Color.WHITE);
        generateKeyButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
        generateKeyButton.setBackground(Common.Color.THEME);
        generateKeyButton.setPreferredSize(new Dimension(300, 30));
        generateKeyButton.setFocusable(false);
        generateKeyButton.setCursor(Common.Cursor.HAND_CURSOR);
        return panel;
    }

    private JPanel getKeySizePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Key size: "));
        panel.add(keySizeOption);
        return panel;

    }

    public void focusTextArea(JComponent input) {
        input.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (e.getSource() == privateKey) {
                    privateScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Common.Color.THEME, 3)));
                } else if (e.getSource() == publicKey)
                    publicJScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Common.Color.THEME, 3)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (e.getSource() == privateKey) {
                    privateScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Common.Color.THEME, 1)));
                } else if (e.getSource() == publicKey)
                    publicJScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Common.Color.THEME, 1)));
            }
        });
    }


    @Override
    public void encrypt() throws Exception {
        result.setForeground(Color.BLACK);
        if (screenPanel.isFileMode()) {
            try {
                long start = System.currentTimeMillis();
                JFileChooser file = screenPanel.getFileChooser();
                if (Objects.isNull(file)) {
                    return;
                }
                rsa.encryptFile(file.getSelectedFile().getAbsolutePath(), rsa.getPublicKey());
                result.setText("Encrypted file success in " + (System.currentTimeMillis() - start) + " ms");
            } catch (Exception e) {
                result.setForeground(Color.RED);
                result.setText("Encrypted file failed: \n" + e.getMessage());
            }
            return;
        }
        if (input.getText().trim().isEmpty()) return;
        try {
            result.setForeground(Color.BLACK);
            result.setText(rsa.encrypt(input.getText(), rsa.getPublicKey()));
        } catch (Exception e) {
            result.setForeground(Color.RED);
            result.setText(e.getMessage());
        }
    }

    @Override
    public void decrypt() throws Exception {
        if (screenPanel.isFileMode()) {
            try {
                long start = System.currentTimeMillis();
                JFileChooser file = screenPanel.getFileChooser();
                if (Objects.isNull(file)) {
                    return;
                }
                rsa.decryptFile(file.getSelectedFile().getAbsolutePath(), rsa.getPublicKey());
                result.setText("Decrypted file success in " + (System.currentTimeMillis() - start) + " ms");
            } catch (Exception e) {
                result.setForeground(Color.RED);
                result.setText("Decrypted file failed: \n" + e.getMessage());
            }
            return;
        }
        if (input.getText().trim().isEmpty()) return;
        try {
            result.setForeground(Color.BLACK);
            result.setText(rsa.decrypt(input.getText(), rsa.getPrivateKey()));
        } catch (Exception e) {
            result.setForeground(Color.RED);
            result.setText(e.getMessage());
        }
    }

}
