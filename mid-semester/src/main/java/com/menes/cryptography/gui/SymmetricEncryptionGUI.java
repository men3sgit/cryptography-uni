package com.menes.cryptography.gui;

import com.menes.cryptography.algorithms.DES;
import com.menes.cryptography.utils.Common;

import javax.swing.*;
import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SymmetricEncryptionGUI implements AlgorithmGUI {
    JTextField keyInput = new JTextField();
    JComboBox<?> algorithmOption;
    JComboBox<?> modeOption;
    JComboBox<Object> bitOption;
    DES des = new DES();

    public SymmetricEncryptionGUI() {
    }

    @Override
    public JPanel renderGUI() {
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.add(getSelectCipher("DES"));
        main.add(getSelectMode("OCF"));
        main.add(getKeyAndBitSelection());
        main.add(getKeyInput());
        return main;

    }

    @Override
    public void doCipher() throws NoSuchAlgorithmException {

    }

    private JPanel getSelectMode(String mode) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Select mode"));
        panel.add(new JComboBox<>(Arrays.stream(new String[]{"Electronic Codebook (ECB)"}).sorted().toArray()));

        return panel;
    }

    private JPanel getSelectCipher(String cipher) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Select block cipher name"));
        algorithmOption = new JComboBox<>(Arrays.stream(new String[]{"Triple DES", "DES", "HILL", "Vigenere", "AES", "RC4"}).sorted().toArray());
        algorithmOption.addActionListener(e -> {
            generateBitOption();
            System.out.println("a");
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
            keyInput.setText(des.generateKey());
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

    private void generateBitOption() {
        if (algorithmOption.getSelectedItem().toString().equalsIgnoreCase("DES")) {
            updateItems(new Integer[]{56});
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

        keyInput.setBorder(BorderFactory.createTitledBorder("Key"));
        keyInput.setPreferredSize(new Dimension(Common.Unit.MAIN_WIDTH, 36));
        panel.add(keyInput);

        return panel;
    }
}
