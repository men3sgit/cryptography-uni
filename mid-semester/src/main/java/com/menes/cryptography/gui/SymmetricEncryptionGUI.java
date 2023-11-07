package com.menes.cryptography.gui;

import com.menes.cryptography.algorithms.SymmetricCipher;
import com.menes.cryptography.utils.Common;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class SymmetricEncryptionGUI implements AlgorithmGUI {
    JPanel main;
    JTextField keyInput = new JTextField(), ivInput = new JTextField();
    JComboBox<?> algorithmOption;
    JComboBox<?> modeOption;
    JComboBox<?> paddingOption;
    JComboBox<Object> bitOption;
    JTextArea input, result;


    public SymmetricEncryptionGUI(JTextArea input, JTextArea result) {
        this.input = input;
        this.result = result;
    }

    @Override
    public JPanel renderGUI() {
        main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.add(getSelectCipher());
        main.add(getSelectMode());
        main.add(getKeyAndBitSelection());
        main.add(getKeyInput());
        main.add(getIVPanel());
        displayIV();
        return main;

    }

    @Override
    public void encrypt() throws Exception {
        if (input.getText().isBlank()) return;
        String algo = algorithmOption.getSelectedItem().toString();
        String transform = String.format("%s/%s/%s", algo.equalsIgnoreCase("Triple DES") ? "DESede" : algo, formatCurrentMode(), paddingOption.getSelectedItem());
        SymmetricCipher cipher = new SymmetricCipher(transform);
        result.setText(cipher.encrypt(input.getText(), getSecretKey(), ivInput.getText()));
    }

    @Override
    public void decrypt() throws Exception {
        if (input.getText().isBlank()) return;
        String transform = String.format("%s/%s/%s", algorithmOption.getSelectedItem(), formatCurrentMode(), paddingOption.getSelectedItem());
        SymmetricCipher cipher = new SymmetricCipher(transform);
        result.setText(cipher.decrypt(input.getText(), getSecretKey(), ivInput.getText()));
    }

    private SecretKey getSecretKey() throws NoSuchAlgorithmException {

        return new SecretKeySpec(Base64.getDecoder().decode(keyInput.getText().getBytes(StandardCharsets.UTF_8)), algorithmOption.getSelectedItem().toString());
    }

    private JPanel getSelectMode() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Select mode"));
        panel.add(modeOption = new JComboBox<>(new String[]{
                "Electronic Codebook (ECB)"
                , "Cipher feedback (CFB)",
                "Output feedback (OFB)",
                "Cipher Block Chaining (CBC)"}));
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
        });
        panel.add(algorithmOption);

        return panel;
    }

    private JPanel getKeyAndBitSelection() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Enter key or"));
        JButton generateBtn = new JButton("Generate random Key");
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setBackground(Common.Color.THEME);
        generateBtn.setFocusable(false);
        generateBtn.addActionListener(action -> {
            try {
                String key = getGenerateKeyString();
                System.out.println(key);
                keyInput.setText(key);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        });
        generateBtn.setFocusable(false);
        generateBtn.setCursor(Common.Cursor.HAND_CURSOR);
        panel.add(generateBtn);
        bitOption = new JComboBox<>();
        generateBitOption();
        panel.add(bitOption);
        panel.add(new JLabel("bits"));
        return panel;
    }

    private String getGenerateKeyString() throws NoSuchAlgorithmException {
        String algo = algorithmOption.getSelectedItem().toString();
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algo.equalsIgnoreCase("Triple DES") ? "DESede" : algo);
        byte[] array = keyGenerator.generateKey().getEncoded();
        System.out.println(keyGenerator.getAlgorithm());
        System.out.println(Arrays.toString(array));
        keyGenerator.init((Integer) bitOption.getSelectedItem());
        return Base64.getEncoder().encodeToString(array);
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
        String algo = algorithmOption.getSelectedItem().toString();
        if (algo.equalsIgnoreCase("DES")) {
            updateItems(new Integer[]{56});
        } else if (algo.equalsIgnoreCase("Triple DES")) {
            updateItems(new Integer[]{112, 168});
        } else {
            updateItems(new Integer[]{128, 192, 256});
        }


    }

    private void updateItems(Object[] list) {
        bitOption.removeAllItems();
        Arrays.stream(list).forEach(item -> bitOption.addItem(item));

    }

    private JPanel getKeyInput() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        keyInput.setPreferredSize(new Dimension(400, 40));
        keyInput.setBorder(BorderFactory.createTitledBorder("Key"));
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

    private JPanel getIVPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        ivInput.setBorder(BorderFactory.createTitledBorder("IV (optional)"));
        ivInput.setPreferredSize(new Dimension(400, 40));
        panel.add(ivInput);
        return panel;
    }

    private void displayIV() {
        int mode = modeOption.getSelectedIndex();
        ivInput.setVisible(mode != Common.Mode.ECB);
        main.revalidate();

    }
}
