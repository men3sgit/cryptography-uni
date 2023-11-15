package com.menes.cryptography.gui;

import com.menes.cryptography.algorithms.SymmetricCipher;
import com.menes.cryptography.algorithms.SymmetricCipherFactory;
import com.menes.cryptography.utils.CharacterLimitTextField;
import com.menes.cryptography.utils.Common;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

public class SymmetricEncryptionGUI implements AlgorithmGUI {
    JPanel main, ivPanel;
    CharacterLimitTextField keyInput = new CharacterLimitTextField(), ivInput = new CharacterLimitTextField();
    JComboBox<?> algorithmOption;
    JComboBox<?> modeOption;
    JComboBox<?> paddingOption;
    JComboBox<Object> bitOption;
    JTextArea input, result;
    SymmetricCipher symmetricCipher;
    JButton ivGenerateBtn = new JButton("Generate IV");

    public SymmetricEncryptionGUI(JTextArea input, JTextArea result) {
        this.input = input;
        this.result = result;
        input.setLineWrap(true);
        result.setLineWrap(true);
    }

    @Override
    public JPanel renderGUI() {
        main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.add(getSelectCipher());
        main.add(getSelectMode());
        main.add(getKeyAndBitSelection());
        loadingSymmetric();
        main.add(getKeyInput());
        renderIVPanel();
        displayIV();
        return main;

    }

    private void loadingSymmetric() {
        String algo = algorithmOption.getSelectedItem().toString();
        String transform = String.format("%s/%s/%s", algo.equalsIgnoreCase("Triple DES") ? "DESede" : algo, formatCurrentMode(), paddingOption.getSelectedItem());
        symmetricCipher = SymmetricCipherFactory.getInstance(transform);
        keyInput.setCharacterLimit(symmetricCipher.getKeySize());
    }

    @Override
    public void encrypt() throws Exception {
        if (input.getText().isBlank()) return;
        try {
            byte[] data = input.getText().getBytes(StandardCharsets.UTF_8);
            symmetricCipher.setSecretKey(keyInput.getText());
            byte[] encryptedBytes = symmetricCipher.encrypt(data);
            result.setForeground(Color.BLACK);
            if (!symmetricCipher.mode.equalsIgnoreCase("ECB")) {
                symmetricCipher.setIv(new String(Base64.getDecoder().decode(ivInput.getText())));
            }
            result.setText(Base64.getEncoder().encodeToString(encryptedBytes));
        } catch (RuntimeException e) {
            result.setText(e.getMessage());
            result.setForeground(Color.RED);
        }
    }


    @Override
    public void decrypt() throws Exception {
        if (input.getText().isBlank()) return;
        try {
            byte[] data = input.getText().getBytes(StandardCharsets.UTF_8);
            byte[] encryptedBytes = Base64.getDecoder().decode(data);
            symmetricCipher.setSecretKey(keyInput.getText());
            byte[] decryptedBytes = symmetricCipher.decrypt(encryptedBytes);
            result.setForeground(Color.BLACK);
            if (!symmetricCipher.mode.equalsIgnoreCase("ECB")) {
                symmetricCipher.setIv(new String(Base64.getDecoder().decode(ivInput.getText())));
            }
            result.setText(new String(decryptedBytes));
        } catch (Exception e) {
            result.setText(e.getMessage());
            result.setForeground(Color.RED);
        }
    }


    private JPanel getSelectMode() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Select mode"));
        panel.add(modeOption = new JComboBox<>(new String[]{"Electronic Codebook (ECB)", "Cipher feedback (CFB)", "Output feedback (OFB)", "Cipher Block Chaining (CBC)"}));
        modeOption.addActionListener(action -> {
            displayIV();
        });
        panel.add(getPaddingOption());
        return panel;
    }

    private JPanel getSelectCipher() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Select block cipher name"));
        algorithmOption = new JComboBox<>(Arrays.stream(new String[]{"Triple DES", "DES", "HILL", "Vigenere", "AES", "Blowfish"}).sorted().toArray());
        algorithmOption.addActionListener(e -> {
            generateBitOption();
            loadingSymmetric();
        });
        panel.add(algorithmOption);

        return panel;
    }

    private JPanel getKeyAndBitSelection() {
        JPanel panel = new JPanel();
        bitOption = new JComboBox<>();
        generateBitOption();
        bitOption.addActionListener(action -> {
            if (bitOption.getItemCount() > 1) {
                symmetricCipher.setKeySize((Integer) bitOption.getSelectedItem() / 8);
                System.out.println((Integer) bitOption.getSelectedItem() / 8);
            }
            keyInput.setCharacterLimit(symmetricCipher.getKeySize());
            System.out.println(keyInput.getCharacterLimit());
            keyInput.setText("");
        });

        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Enter key or"));
        JButton generateBtn = new JButton("Generate random Key");
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setBackground(Common.Color.THEME);
        generateBtn.setFocusable(false);
        generateBtn.addActionListener(action -> {
            try {
                String key = Base64.getEncoder().encodeToString(symmetricCipher.getSecretKey().getEncoded());
                keyInput.setText(key.substring(0, keyInput.getCharacterLimit()));
            } catch (NoSuchAlgorithmException | KeyException e) {
                throw new RuntimeException(e);
            }
        });
        generateBtn.setFocusable(false);
        generateBtn.setCursor(Common.Cursor.HAND_CURSOR);

        panel.add(generateBtn);
        panel.add(bitOption);
        panel.add(new JLabel("bits"));
        return panel;
    }


    private String formatCurrentMode() {
        return switch (modeOption.getSelectedIndex()) {
            case 1 -> "CFB";
            case 2 -> "OFB";
            case 3 -> "CBC";
            default -> "ECB";
        };
    }

    private void generateBitOption() {
        String algo = Objects.requireNonNull(algorithmOption.getSelectedItem()).toString();
        if (algo.equalsIgnoreCase("DES")) {
            updateItems(new Integer[]{56});
        } else if (algo.equalsIgnoreCase("Triple DES")) {
            updateItems(new Integer[]{192});
        } else {
            updateItems(new Integer[]{128, 192, 256});
        }
    }

    private void updateItems(Object[] list) {
        bitOption.removeAllItems();
        Arrays.stream(list).forEach(item -> bitOption.addItem(item));
        bitOption.revalidate();
    }

    private JPanel getKeyInput() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        keyInput.setPreferredSize(new Dimension(400, 40));
        keyInput.setBorder(BorderFactory.createTitledBorder("Key"));
        keyInput.setCharacterLimit(symmetricCipher.getKeySize());
        panel.add(keyInput);

        return panel;
    }

    private JPanel getPaddingOption() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Padding "));
        paddingOption = new JComboBox<>(new String[]{"PKCS5Padding", "ISO10126Padding"});
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(paddingOption);
        return panel;
    }

    private void renderIVPanel() {
        ivPanel = new JPanel();
        ivPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        ivInput.setBorder(BorderFactory.createTitledBorder("IV (optional)"));
        ivInput.setPreferredSize(new Dimension(400, 40));
        ivInput.setCharacterLimit(8);
        ivPanel.add(ivInput);
        ivGenerateBtn.addActionListener(action -> {
            String sub  = Base64.getEncoder().encodeToString(symmetricCipher.getIv().getIV()).substring(0,8);
            ivInput.setText(sub);
        });
        ivGenerateBtn.setBackground(Common.Color.THEME);
        ivGenerateBtn.setForeground(Color.WHITE);
        ivGenerateBtn.setFocusable(false);
        ivGenerateBtn.setCursor(Common.Cursor.HAND_CURSOR);
        ivPanel.add(ivGenerateBtn);
        main.add(ivPanel);
    }

    private void displayIV() {
        int mode = modeOption.getSelectedIndex();
        ivPanel.setVisible(mode != Common.Mode.ECB);
        main.revalidate();

    }

}
